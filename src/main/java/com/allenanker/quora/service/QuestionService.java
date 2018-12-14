package com.allenanker.quora.service;

import com.allenanker.quora.dao.QuestionDAO;
import com.allenanker.quora.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    public List<Question> selectLastestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLastestQuestions(userId, offset, limit);
    }
}
