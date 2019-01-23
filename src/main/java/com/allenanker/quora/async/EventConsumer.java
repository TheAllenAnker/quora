package com.allenanker.quora.async;

import com.alibaba.fastjson.JSON;
import com.allenanker.quora.util.JedisAdapter;
import com.allenanker.quora.util.RedisKeyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    private HashMap<EventType, List<EventHandler>> handlersMap = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        // get all implementations of interface EventHandler
        Map<String, EventHandler> handlers = applicationContext.getBeansOfType(EventHandler.class);
        if (handlers != null) {
            for (Map.Entry<String, EventHandler> entry : handlers.entrySet()) {
                List<EventType> supportedEventTypes = entry.getValue().getSupportedEvents();
                for (EventType eventType : supportedEventTypes) {
                    if (!handlersMap.containsKey(eventType)) {
                        handlersMap.put(eventType, new ArrayList<>());
                    }
                    handlersMap.get(eventType).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(() -> {
            while (true) {
                String key = RedisKeyUtils.getEventQueueKey();
                List<String> events = jedisAdapter.brpop(0, key);
                for (String event : events) {
                    if (event.equals(key)) {
                        continue;
                    }
                    EventModel eventModel = JSON.parseObject(event, EventModel.class);
                    if (handlersMap.containsKey(eventModel.getType())) {
                        for (EventHandler handler : handlersMap.get(eventModel.getType())) {
                            handler.doHandle(eventModel);
                        }
                    } else {
                        logger.error("error: unsupported event");
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
