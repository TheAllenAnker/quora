package com.allenanker.quora.async;

import com.alibaba.fastjson.JSONObject;
import com.allenanker.quora.util.JedisAdapter;
import com.allenanker.quora.util.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtils.getEventQueueKey();
            jedisAdapter.lpush(key, json);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
