package com.allenanker.quora.service;

import com.allenanker.quora.dao.MessageDAO;
import com.allenanker.quora.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;

    @Autowired
    SensitiveWordsService sensitiveWordsService;

    public int addMessage(Message message) {
        message.setContent(sensitiveWordsService.filterBySensitiveWords(message.getContent()));
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationMessages(String conversationId, int offset, int limit) {
        return messageDAO.selectMessagesByConversationId(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDAO.getConversationList(userId, offset, limit);
    }

    public int getUnreadMessageCount(String conversationId) {
        return messageDAO.getUnreadMessageCount(conversationId);
    }
}
