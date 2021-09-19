package com.msg.messenger.config.redis;

import com.msg.messenger.helper.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

@Slf4j
public class RedisReceiver
    implements MessageListener {

    @Override
    public void onMessage(final Message message, final byte[] pattern) {
        log.info("Message: {}", message);

        RedisNotificationMessage redisMessage = JsonHelper.readString(message.toString(),
                                                                      RedisNotificationMessage.class);
    }
}
