package com.application.service;

import com.application.persistence.ChannelRepository;
import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;
import com.application.service.exceptions.SlackMessageNotSentException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class SlackControllerTest {

    @InjectMocks
    @Resource
    SlackChannelController slackChannelController;


    @Mock
    ChannelRepository channelRepository;
    @Mock
    SlackIntegration slackIntegration;


    SlackChannel slackChannel;


    @BeforeEach
    public void setUp()  {
        slackChannel=new SlackChannel();
        MockitoAnnotations.openMocks(this);

    }


    @Test
    public void createChannelTestSuccess() throws ChannelAlreadyExitsInDataBaseException, SlackMessageNotSentException {
        slackChannelController.createChannel(slackChannel);
        Mockito.verify(channelRepository).createChannel(slackChannel);
        Mockito.verify(slackIntegration).sendMessage(slackChannel,"New channel has been created");

    }
//

    @Test
    public void deleteChannelTestSuccess() throws ChannelAlreadyExitsInDataBaseException, SlackMessageNotSentException, ChannelNotExitsInDataBaseException {
        slackChannelController.createChannel(slackChannel);
        slackChannelController.updateChannel(slackChannel);
        Mockito.verify(channelRepository).updateChannel(slackChannel);
        Mockito.verify(slackIntegration).sendMessage(slackChannel,"Channel's status has been updated");

    }



}
