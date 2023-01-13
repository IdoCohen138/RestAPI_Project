package com.pack;

import com.pack.*;
import com.pack.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.pack.exceptions.ChannelNotExitsInDataBaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.annotation.Resource;
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

    @Autowired
    Persistent persistent;

    List<SlackChannel> slackChannels;



    public static Stream<Arguments> enumStatus() {

        return Stream.of(
                Arguments.of(EnumStatus.ENABLED, EnumStatus.DISABLED),
                Arguments.of(EnumStatus.DISABLED, EnumStatus.ENABLED)
        );
    }

    @BeforeEach
    public void setUp() throws IOException, ChannelNotExitsInDataBaseException {
        slackChannel = createSlackChannel();
        slackChannels = new ArrayList<>();
        deleteAllDataBase();
    }


    @Test
    void createChannelTestSuccess() throws ChannelNotExitsInDataBaseException {

        assertEquals(0, persistent.getAllChannels().size());
        assertDoesNotThrow(() -> persistent.saveChannel(slackChannel));
        slackChannels.add(slackChannel);
        assertEquals(persistent.getAllChannels(), slackChannels);
        SlackChannel addedChannel = persistent.getChannelbyID(slackChannel.getId());
        assertNotNull((addedChannel.getCreated_at()));
        assertDoesNotThrow(() -> persistent.deleteChannel(slackChannel.getId()));
        slackChannels.remove(slackChannel);
    }

    @Test
    void CreateChannelAlreadyExistFail() throws  IOException {
        assertEquals(0, persistent.getAllChannels().size());
        assertDoesNotThrow(() -> persistent.saveChannel(slackChannel));
        SlackChannel duplicate = createSlackChannel();
        assertThrows(ChannelAlreadyExitsInDataBaseException.class, () ->  persistent.saveChannel(duplicate));
        assertDoesNotThrow(() -> persistent.deleteChannel(slackChannel.getId()));
    }

    @Test
    void deleteChannelTestSuccess() {
        assertDoesNotThrow(() -> persistent.saveChannel(slackChannel));
        assertDoesNotThrow(() -> persistent.deleteChannel(slackChannel.getId()));
        assertEquals(0, persistent.getAllChannels().size());
    }

    @Test
    void getSpecificChannelTestSuccess() throws ChannelNotExitsInDataBaseException {
        assertDoesNotThrow(() -> persistent.saveChannel(slackChannel));
        SlackChannel channelReturn = persistent.getChannelbyID(slackChannel.getId());
        assertNotNull(channelReturn);
        assertDoesNotThrow(() -> persistent.deleteChannel(slackChannel.getId()));
        slackChannels.remove(slackChannel);
    }

    @Test
    void getSpecificChannel_no_slack_channel_to_get_TestFail() {
        assertEquals(0, persistent.getAllChannels().size());
        assertThrows(ChannelNotExitsInDataBaseException.class, () -> {
            persistent.getChannelbyID(slackChannel.getId());
        });
    }

    @ParameterizedTest
    @MethodSource("enumStatus")
    void getChannels_byFilter_TestSuccess(EnumStatus status1, EnumStatus status2) {
        assertEquals(0, persistent.getAllChannels().size());
        assertDoesNotThrow(() -> persistent.saveChannel(slackChannel));
        assertDoesNotThrow(() -> persistent.updateChannel(slackChannel.getId(), status1));
        slackChannel.setStatus(status1);
        List<SlackChannel> slackChannelreturn=persistent.getAllChannelsbyStatus(status1);
        slackChannels.add(slackChannel);
        assertEquals(slackChannelreturn, slackChannels);
        assertDoesNotThrow(() -> persistent.deleteChannel(slackChannel.getId()));
        slackChannels.remove(slackChannel);


    }

    @ParameterizedTest
    @MethodSource("enumStatus")
    void getChannels_byFilter_enable_return_nothing_TestSuccess(EnumStatus status1, EnumStatus status2) {
        assertEquals(0, persistent.getAllChannels().size());
        assertDoesNotThrow(() -> persistent.saveChannel(slackChannel));
        assertDoesNotThrow(() -> persistent.updateChannel(slackChannel.getId(), status1));
        List<SlackChannel> slackChannelreturn =
                assertDoesNotThrow(() -> persistent.getAllChannelsbyStatus(status2));
        slackChannels.add(slackChannel);
        assertNotEquals(slackChannelreturn, slackChannels);
        assertEquals(slackChannelreturn.size(), 0);
        assertDoesNotThrow(() -> persistent.deleteChannel(slackChannel.getId()));
        slackChannels.remove(slackChannel);

    }


    @Test
    void getAllChannelsTestSuccess() {
        assertEquals(0, persistent.getAllChannels().size());
        assertDoesNotThrow(() -> persistent.saveChannel(slackChannel));
        slackChannels.add(slackChannel);
        List<SlackChannel> slackChannelreturn_enable =
                assertDoesNotThrow(() -> persistent.getAllChannels());
        assertEquals(slackChannelreturn_enable, slackChannels);
        assertDoesNotThrow(() -> persistent.deleteChannel(slackChannel.getId()));
        slackChannels.remove(slackChannel);


    }

    @Test
    void getAllChannels_return_nothing_TestSuccess() {
        assertEquals(0, persistent.getAllChannels().size());
        List<SlackChannel> slackChannelreturn = persistent.getAllChannels();
        assertEquals(slackChannelreturn, slackChannels);
    }


    @ParameterizedTest
    @MethodSource("enumStatus")
    public void updateChannelStatusTestSuccess(EnumStatus status1, EnumStatus status2) throws ChannelNotExitsInDataBaseException {
        assertEquals(0, persistent.getAllChannels().size());
        assertDoesNotThrow(() -> persistent.saveChannel(slackChannel));
        assertDoesNotThrow(() -> persistent.updateChannel(slackChannel.getId(), status1));
        slackChannel.setStatus(status1);
        slackChannels.add(slackChannel);
        SlackChannel addedChannel = persistent.getChannelbyID(slackChannel.getId());
        assertEquals(addedChannel.getStatus(), slackChannel.getStatus());
        assertNotNull((addedChannel.getModified_at()));
        assertDoesNotThrow(() -> persistent.deleteChannel(slackChannel.getId()));
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

    private void deleteAllDataBase() throws ChannelNotExitsInDataBaseException {
        List<SlackChannel> arraylist=persistent.getAllChannels();
        for (int i=0;i<arraylist.size();i++){
            persistent.deleteChannel(arraylist.get(i).getId());
        }
    }

}