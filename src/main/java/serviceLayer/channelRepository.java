package serviceLayer;
import presentationLayer.Exceptions.ChannelAlreadyExitsInDataBaseException;
import presentationLayer.Exceptions.ChannelNotExitsInDataBaseException;

import java.util.ArrayList;

public interface channelRepository {

    void createChannel(SlackChannel slackChannel) throws ChannelAlreadyExitsInDataBaseException;
    void updateChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;
    void deleteChannel(SlackChannel slackChannel) throws ChannelNotExitsInDataBaseException;
    SlackChannel getSpecificChannel(String webhook) throws ChannelNotExitsInDataBaseException;
    ArrayList<?> getChannels(String filter);
}
