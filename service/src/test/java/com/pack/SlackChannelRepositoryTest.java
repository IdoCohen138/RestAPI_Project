package com.pack;

import com.pack.exceptions.ChannelNotExitsInDataBaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

@EntityScan(basePackages = {"com.pack"})
@ComponentScan(basePackages = {"com.pack"})
@EnableAutoConfiguration
@SpringBootConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class})
public class SlackChannelRepositoryTest {
    SlackChannel slackChannel;

    @Autowired @Qualifier("slackRepository")
    SlackRepository channelSlackRepository;

    List<SlackChannel> slackChannels;

    public static Stream<Arguments> enumStatus() {

        return Stream.of(
                Arguments.of(EnumStatus.ENABLED, EnumStatus.DISABLED),
                Arguments.of(EnumStatus.DISABLED, EnumStatus.ENABLED)
        );
    }

    @BeforeEach
    public void setUp() throws IOException {
        slackChannel = createSlackChannel();
        slackChannels = new ArrayList<>();
    }

    @Test
    void createChannelTestSuccess()  {
        assertEquals(0, channelSlackRepository.findAll().size());
        assertDoesNotThrow(() -> channelSlackRepository.save(slackChannel));
        slackChannels.add(slackChannel);
        assertEquals(channelSlackRepository.findAll(), slackChannels);
        SlackChannel addedChannel = channelSlackRepository.findById(slackChannel.getId()).get();
        assertNotNull((addedChannel.getCreated_at()));
        assertDoesNotThrow(() -> channelSlackRepository.delete(slackChannel));
        slackChannels.remove(slackChannel);
    }

    @Test
    void CreateChannelAlreadyExistFail() throws  IOException {
        assertDoesNotThrow(() -> channelSlackRepository.saveAndFlush(slackChannel));
        SlackChannel duplicate = createSlackChannel();
        assertThrows(DataIntegrityViolationException.class, () -> channelSlackRepository.saveAndFlush(duplicate));
        assertDoesNotThrow(() -> channelSlackRepository.delete(slackChannel));
    }

    @Test
    void deleteChannelTestSuccess() {
        assertDoesNotThrow(() -> channelSlackRepository.save(slackChannel));
        assertDoesNotThrow(() -> channelSlackRepository.delete(slackChannel));
        assertEquals(0, channelSlackRepository.findAll().size());
    }

    @Test
    void getSpecificChannelTestSuccess() {
        assertDoesNotThrow(() -> channelSlackRepository.save(slackChannel));
        SlackChannel channelReturn = channelSlackRepository.getById(slackChannel.getId());
        assertNotNull(channelReturn);
        assertDoesNotThrow(() -> channelSlackRepository.delete(slackChannel));
        slackChannels.remove(slackChannel);
    }

    @Test
    void getSpecificChannel_no_slack_channel_to_get_TestFail() {
        assertEquals(channelSlackRepository.findAll().size(), 0);
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            channelSlackRepository.getById(slackChannel.getId());
        });
    }

    @ParameterizedTest
    @MethodSource("enumStatus")
    void getChannels_byFilter_TestSuccess(EnumStatus status1, EnumStatus status2) {
        assertDoesNotThrow(() -> channelSlackRepository.save(slackChannel));
        assertDoesNotThrow(() -> channelSlackRepository.updateChannel(slackChannel.getId(), status1));
        slackChannel.setStatus(status1);
        Specification<SlackChannel> spec = (root, query, builder) ->
                builder.equal(root.get("status"), status1);
        List<SlackChannel> slackChannelreturn =
                assertDoesNotThrow(() -> channelSlackRepository.findAll(spec));
        slackChannels.add(slackChannel);
        assertEquals(slackChannelreturn, slackChannels);
        assertDoesNotThrow(() -> channelSlackRepository.delete(slackChannel));
        slackChannels.remove(slackChannel);


    }

    @ParameterizedTest
    @MethodSource("enumStatus")
    void getChannels_byFilter_enable_return_nothing_TestSuccess(EnumStatus status1, EnumStatus status2) {
        assertDoesNotThrow(() -> channelSlackRepository.save(slackChannel));
        assertDoesNotThrow(() -> channelSlackRepository.updateChannel(slackChannel.getId(), status1));
        slackChannel.setStatus(status1);
        Specification<SlackChannel> spec = (root, query, builder) ->
                builder.equal(root.get("status"), status2);

        List<SlackChannel> slackChannelreturn =
                assertDoesNotThrow(() -> channelSlackRepository.findAll(spec));
        slackChannels.add(slackChannel);
        assertNotEquals(slackChannelreturn, slackChannels);
        assertEquals(slackChannelreturn.size(), 0);
        assertDoesNotThrow(() -> channelSlackRepository.delete(slackChannel));
        slackChannels.remove(slackChannel);

    }


    @Test
    void getAllChannelsTestSuccess() {
        assertDoesNotThrow(() -> channelSlackRepository.save(slackChannel));
        slackChannels.add(slackChannel);
        List<SlackChannel> slackChannelreturn_enable =
                assertDoesNotThrow(() -> channelSlackRepository.findAll());
        assertEquals(slackChannelreturn_enable, slackChannels);
        assertDoesNotThrow(() -> channelSlackRepository.delete(slackChannel));
        slackChannels.remove(slackChannel);


    }

    @Test
    void getAllChannels_return_nothing_TestSuccess() {
        List<SlackChannel> slackChannelreturn = channelSlackRepository.findAll();
        assertEquals(slackChannelreturn, slackChannels);
    }


    @ParameterizedTest
    @MethodSource("enumStatus")
    public void updateChannelStatusTestSuccess(EnumStatus status1, EnumStatus status2) throws ChannelNotExitsInDataBaseException {
        assertDoesNotThrow(() -> channelSlackRepository.save(slackChannel));
        assertDoesNotThrow(() -> channelSlackRepository.updateChannel(slackChannel.getId(), status1));
        slackChannel.setStatus(status1);
        slackChannels.add(slackChannel);
        SlackChannel addedChannel = channelSlackRepository.findById(slackChannel.getId()).get();

        assertEquals(addedChannel.getStatus(), slackChannel.getStatus());
        assertNotNull((addedChannel.getModified_at()));

        assertDoesNotThrow(() -> channelSlackRepository.delete(slackChannel));
        slackChannels.remove(slackChannel);
    }

    private SlackChannel createSlackChannel() throws IOException {
        SlackChannel slackChannel;
        slackChannel = new SlackChannel();
        String webhook = readFromProperties();
        slackChannel.setWebhook(webhook);
        slackChannel.setChannelName("test");
        slackChannel.setStatus(EnumStatus.ENABLED);
        slackChannel.setLogMessages(new HashSet<>());
        return slackChannel;

    }

    private String readFromProperties() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties.getProperty("webhook_message_api");
    }
}