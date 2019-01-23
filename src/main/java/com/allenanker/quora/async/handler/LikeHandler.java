package com.allenanker.quora.async.handler;

import com.allenanker.quora.async.EventHandler;
import com.allenanker.quora.async.EventModel;
import com.allenanker.quora.async.EventType;
import com.allenanker.quora.model.Message;
import com.allenanker.quora.model.User;
import com.allenanker.quora.service.MessageService;
import com.allenanker.quora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(0);
        message.setToId(eventModel.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUserById(eventModel.getActorId());
        message.setContent("User " + user.getName() + " just liked your comment on question " + eventModel.getConfig(
                "questionId"));

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportedEvents() {
        ArrayList<EventType> list = new ArrayList<>();
        list.add(EventType.LIKE);

        return list;
    }
}
