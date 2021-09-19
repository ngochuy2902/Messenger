package com.msg.messenger.config.redis;

import com.msg.messenger.helper.JsonHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    public RedisPublisher(final RedisTemplate<String, Object> redisTemplate,
                          final ChannelTopic channelTopic) {
        this.redisTemplate = redisTemplate;
        this.channelTopic = channelTopic;
    }


    public void publish(final RedisNotificationMessage message) {
        redisTemplate.convertAndSend(channelTopic.getTopic(),
                                     Objects.requireNonNull(JsonHelper.convertObjectToString(message)));
    }
}
