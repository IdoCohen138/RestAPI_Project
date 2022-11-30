package com.example.demo;

import com.example.demo.presentationLayer.Exceptions.ChannelNotExitsInDataBaseException;
import org.apache.logging.log4j.core.util.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTest {
    RestTemplate restTemplate;
    String url;
    HttpHeaders headers;
    @LocalServerPort
    private int port;

    @BeforeEach
    public void Setup() {
        restTemplate = new RestTemplate();
        url = "http://localhost:" + port + "/channels";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void PostTest(String requestJson) {
        // build the request
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        // send POST request
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        Assertions.assertEquals(response.getStatusCode(),HttpStatus.OK);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);

    }

    private static Stream<Arguments> webhooks() {
        return Stream.of(
                Arguments.of("{\"webhook\":\"https://hooks.slack.com/services/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\",\"channelName\":\"liorchannel\"}","?webhook=https://hooks.slack.com/services/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"),
                Arguments.of("{\"webhook\":\"Webhook_1\",\"channelName\":\"shanichannel\"}","?webhook=Webhook_1"),
                Arguments.of("{\"webhook\":\"Webhook_2\",\"channelName\":\"stam\"}","?webhook=Webhook_2")
        );
    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void PutTest(String requestJson) {
        //post for the channels will be in the database
        HttpEntity<String> entity_ = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity_, String.class);

        // build the request
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        // send POST request
       this.restTemplate.put(url, entity);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);

    }
    @ParameterizedTest
    @MethodSource("webhooks")
    public void DeleteTest(String requestJson) {
        //post for the channels will be in the database
        HttpEntity<String> entity_ = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity_, String.class);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<Void> resp = restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
        Assertions.assertEquals(resp.getStatusCode(),HttpStatus.OK);

    }

    @ParameterizedTest
    @MethodSource("webhooks")
    public void GetSpecificTest(String requestJson,String Url) {
        //post for the channels will be in the database
        HttpEntity<String> entity_ = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity_, String.class);

        HttpEntity<String> entity__ = new HttpEntity<>(requestJson, headers);
        String urll=url+Url;
        ResponseEntity<String> entity=this.restTemplate.exchange(urll,HttpMethod.GET,entity__,String.class);

        Assertions.assertEquals(entity.getStatusCode(),HttpStatus.OK);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }
    @ParameterizedTest
    @MethodSource("enable")
    public void GetByStatus_enableTest(String requestJson,String Url) {
        //post for the channels will be in the database
        HttpEntity<String> entity_ = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity_, String.class);
        //restTemplate.put(url, entity_, String.class);
        String urll=url+Url;
        HttpEntity<String> entity__ = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> entity=this.restTemplate.exchange(urll,HttpMethod.GET,entity__,String.class);
        Assertions.assertEquals(entity.getStatusCode(),HttpStatus.OK);
        restTemplate.exchange(url, HttpMethod.DELETE, entity_, Void.class);
    }
    private static Stream<Arguments> enable() {
        return Stream.of(
                Arguments.of("{\"webhook\":\"https://hooks.slack.com/services/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\",\"channelName\":\"liorchannel\"}","?status=Enable"),
                Arguments.of("{\"webhook\":\"Webhook_1\",\"channelName\":\"shanichannel\"}","?status=Enable"),
                Arguments.of("{\"webhook\":\"Webhook_2\",\"channelName\":\"stam\"}","?status=Enable")
        );
    }
    @ParameterizedTest
    @MethodSource("disable")
    public void GetByStatus_disableTest(String requestJson,String Url) {
        //post for the channels will be in the database
        HttpEntity<String> entity_ = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity_, String.class);
        restTemplate.put(url, entity_, String.class);
        String urll=url+Url;
        HttpEntity<String> entity__ = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> entity=this.restTemplate.exchange(urll,HttpMethod.GET,entity__,String.class);
        Assertions.assertEquals(entity.getStatusCode(),HttpStatus.OK);
        restTemplate.exchange(url, HttpMethod.DELETE, entity_, Void.class);
    }
    private static Stream<Arguments> disable() {
        return Stream.of(
                Arguments.of("{\"webhook\":\"https://hooks.slack.com/services/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\",\"channelName\":\"liorchannel\"}","?status=Disable"),
                Arguments.of("{\"webhook\":\"Webhook_1\",\"channelName\":\"shanichannel\"}","?status=Disable"),
                Arguments.of("{\"webhook\":\"Webhook_2\",\"channelName\":\"stam\"}","?status=Disable")
        );
    }


}
//
//    // request body parameters
//
//    //map1.put("userId", 1);
////    map.put("title", "Spring Boot 101");
////    map.put("body", "A powerful tool for building web apps.");
//
//    // build the request
//    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
//
//    // send POST request
//    ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

//
////    @Test
////    void createChannelTest() throws IOException, InterruptedException, NoSuchFieldException, IllegalAccessException {
////        HttpRequest request = HttpRequest.newBuilder()
////                .uri(URI.create("http://webcode.me"))
////                .GET()
////                .build();
////
////        HttpResponse<Void> response = client.send(request,
////                HttpResponse.BodyHandlers.discarding());
////
////        Map<String, String> json = new LinkedHashMap<>();
////        json.put("firstchannel", "https://hooks.slack.com/services/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");
////        Assertions.assertTrue(myclient.CreateChannelTest(json));
////        System.out.println(response.statusCode());
////
////    }
//
//
//}
//
////    @Test
////    void httpRequestTestWithSwager() throws FileNotFoundException {
////        InputStream inputStream = new FileInputStream(new File("C:\\Users\\liork\\springBoot\\springBoot\\src\\test\\java\\com\\example\\demo\\IdoCohen138-HelloWorldTry2-1.0.0-resolved.yaml"));
////        Yaml yaml = new Yaml();
////        Map<String, Object> data = yaml.load(inputStream);
//////		System.out.println(data);
////        ObjectMapper om = new ObjectMapper();
////        Map relevantData = om.convertValue(data.get("paths"), Map.class);
//////		System.out.println(relevantData);
////        Set methods = relevantData.keySet();
//////		System.out.println(methods);
////        String firstMethod = methods.stream().findFirst().get().toString();
//////		System.out.println(firstMethod);
////
////        assert(this.restTemplate.getForObject("http://localhost:" + port + firstMethod + "?name=Ido",
////                String.class).equals("Hello Ido!"));
////        assert(this.restTemplate.getForObject("http://localhost:" + port + firstMethod ,
////                String.class).equals("Hello None!"));
////    }
//
