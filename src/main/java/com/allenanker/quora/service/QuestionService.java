package com.allenanker.quora.service;

import com.allenanker.quora.dao.QuestionDAO;
import com.allenanker.quora.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    private QuestionDAO questionDAO;

    public Question findQuestionById(int qid) {
        return questionDAO.selectQuestionById(qid);
    }

    public int addQuestion(Question question) {
        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public List<Question> selectLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }
}
