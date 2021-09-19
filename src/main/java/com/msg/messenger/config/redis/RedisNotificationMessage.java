package com.msg.messenger.config.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisNotificationMessage {

    private Long sourceId;
}

