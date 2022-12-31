//package com.application.persistence;
//
//import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
//import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;
//import com.application.service.EnumStatus;
//import com.application.service.SlackRepository;
//import com.application.service.SlackChannel;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Timestamp;
//import java.util.*;
//import java.util.stream.Stream;
//import static org.junit.jupiter.api.Assertions.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ChannelSlackRepositoryTest {
//    SlackChannel slackChannel;
//    @Autowired
//    SlackRepository channelSlackRepository;
//    List<SlackChannel> slackChannels;
//
//    public static Stream<Arguments> enumStatus() {
//
//        return Stream.of(
//                Arguments.of(EnumStatus.ENABLED, EnumStatus.DISABLED),
//                Arguments.of(EnumStatus.DISABLED, EnumStatus.ENABLED)
//        );
//    }
//
//
//    @BeforeEach
//    public void setUp() throws IOException {
//        createSlackChannel();
//        slackChannels = new ArrayList<>();
//
//    }
//
//
//    @Test
//    void createChannelTestSuccess() throws ChannelNotExitsInDataBaseException {
//        assertEquals(0, channelSlackRepository.getAllChannels().size());
//        assertDoesNotThrow(() -> channelSlackRepository.createChannel(slackChannel));
//        slackChannels.add(slackChannel);
//        slackChannel.setCreated_at(new Timestamp(System.currentTimeMillis()));
//        assertEquals(channelSlackRepository.getAllChannels(), slackChannels);
//        assertEquals((channelSlackRepository.getChannel(slackChannel.getId()).getCreated_at()).toLocalDateTime().withNano(0), slackChannel.getCreated_at().toLocalDateTime().withNano(0));
//        assertDoesNotThrow(() -> channelSlackRepository.deleteChannel(slackChannel.getId()));
//        slackChannels.remove(slackChannel);
//
//    }
//
//
//    @Test
//    void CreateChannelAlreadyExistFail() {
//        assertDoesNotThrow(() -> channelSlackRepository.createChannel(slackChannel));
//        assertThrows(ChannelAlreadyExitsInDataBaseException.class, () -> channelSlackRepository.createChannel(slackChannel));
//        assertDoesNotThrow(() -> channelSlackRepository.deleteChannel(slackChannel.getId()));
//
//    }
//
//    @Test
//    void deleteChannelTestSuccess() {
//        assertDoesNotThrow(() -> channelSlackRepository.createChannel(slackChannel));
//        assertDoesNotThrow(() -> channelSlackRepository.deleteChannel(slackChannel.getId()));
//        assertEquals(0, channelSlackRepository.getAllChannels().size());
//    }
//
//    @Test
//    void deleteChannel_no_slack_channel_to_delete_TestFail() {
//        assertEquals(0, channelSlackRepository.getAllChannels().size());
//        assertThrows(ChannelNotExitsInDataBaseException.class, () -> {
//            channelSlackRepository.deleteChannel(slackChannel.getId());
//        });
//    }
//
//
//    @Test
//    void getSpecificChannelTestSuccess() {
//        assertDoesNotThrow(() -> channelSlackRepository.createChannel(slackChannel));
//        SlackChannel channelReturn =
//                assertDoesNotThrow(() -> channelSlackRepository.getChannel(slackChannel.getId()));
//        assertEquals(channelReturn, slackChannel);
//        assertDoesNotThrow(() -> channelSlackRepository.deleteChannel(slackChannel.getId()));
//        slackChannels.remove(slackChannel);
//
//    }
//
//    @Test
//    void getSpecificChannel_no_slack_channel_to_get_TestFail() {
//        assertEquals(channelSlackRepository.getAllChannels().size(), 0);
//        assertThrows(ChannelNotExitsInDataBaseException.class, () -> {
//            channelSlackRepository.getChannel(slackChannel.getId());
//        });
//    }
//
//
//    @ParameterizedTest
//    @MethodSource("enumStatus")
//    void getChannels_byFilter_TestSuccess(EnumStatus status1, EnumStatus status2) {
//        assertDoesNotThrow(() -> channelSlackRepository.createChannel(slackChannel));
//        assertDoesNotThrow(() -> channelSlackRepository.updateChannel(slackChannel.getId(), status1));
//        slackChannel.setStatus(status1);
//        List<SlackChannel> slackChannelreturn =
//                assertDoesNotThrow(() -> channelSlackRepository.getChannels(status1));
//        slackChannels.add(slackChannel);
//        assertEquals(slackChannelreturn, slackChannels);
//        assertDoesNotThrow(() -> channelSlackRepository.deleteChannel(slackChannel.getId()));
//        slackChannels.remove(slackChannel);
//
//
//    }
//
//    @ParameterizedTest
//    @MethodSource("enumStatus")
//    void getChannels_byFilter_enable_return_nothing_TestSuccess(EnumStatus status1, EnumStatus status2) {
//        assertDoesNotThrow(() -> channelSlackRepository.createChannel(slackChannel));
//        assertDoesNotThrow(() -> channelSlackRepository.updateChannel(slackChannel.getId(), status1));
//        slackChannel.setStatus(status1);
//        List<SlackChannel> slackChannelreturn =
//                assertDoesNotThrow(() -> channelSlackRepository.getChannels(status2));
//        slackChannels.add(slackChannel);
//        assertNotEquals(slackChannelreturn, slackChannels);
//        assertEquals(slackChannelreturn.size(), 0);
//        assertDoesNotThrow(() -> channelSlackRepository.deleteChannel(slackChannel.getId()));
//        slackChannels.remove(slackChannel);
//
//    }
//
//
//    @Test
//    void getAllChannelsTestSuccess() {
//        assertDoesNotThrow(() -> channelSlackRepository.createChannel(slackChannel));
//        slackChannels.add(slackChannel);
//        List<SlackChannel> slackChannelreturn_enable =
//                assertDoesNotThrow(() -> channelSlackRepository.getAllChannels());
//        assertEquals(slackChannelreturn_enable, slackChannels);
//        assertDoesNotThrow(() -> channelSlackRepository.deleteChannel(slackChannel.getId()));
//        slackChannels.remove(slackChannel);
//
//
//    }
//
//    @Test
//    void getAllChannels_return_nothing_TestSuccess() {
//        List<SlackChannel> slackChannelreturn = channelSlackRepository.getAllChannels();
//        assertEquals(slackChannelreturn, slackChannels);
//    }
//
//
//    @ParameterizedTest
//    @MethodSource("enumStatus")
//    public void updateChannelStatusTestSuccess(EnumStatus status1, EnumStatus status2) throws ChannelNotExitsInDataBaseException {
//        assertDoesNotThrow(() -> channelSlackRepository.createChannel(slackChannel));
//        assertDoesNotThrow(() -> channelSlackRepository.updateChannel(slackChannel.getId(), status1));
//        slackChannel.setStatus(status1);
//        slackChannels.add(slackChannel);
////        Date d=new Date();
//        slackChannel.setModified_at(new Timestamp(System.currentTimeMillis()));
//        assertEquals(channelSlackRepository.getChannel(slackChannel.getId()).getStatus(), slackChannel.getStatus());
//        assertEquals((channelSlackRepository.getChannel(slackChannel.getId()).getModified_at()).toLocalDateTime().withNano(0), slackChannel.getModified_at().toLocalDateTime().withNano(0));
//        assertDoesNotThrow(() -> channelSlackRepository.deleteChannel(slackChannel.getId()));
//        slackChannels.remove(slackChannel);
//
//
//    }
//
//
//    private void createSlackChannel() throws IOException {
//        slackChannel = new SlackChannel();
//        slackChannel.setId(UUID.randomUUID());
//        String webhook = readFromProperties();
//        slackChannel.setWebhook(webhook);
//        slackChannel.setChannelName("test");
//        slackChannel.setStatus(EnumStatus.ENABLED);
//
//    }
//
//
//    private String readFromProperties() throws IOException {
//        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
//        Properties properties = new Properties();
//        properties.load(inputStream);
//        return properties.getProperty("webhook_message_api");
//    }
//}