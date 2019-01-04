package com.allenanker.quora.controller;

import com.allenanker.quora.model.Comment;
import com.allenanker.quora.model.EntityType;
import com.allenanker.quora.model.HostHolder;
import com.allenanker.quora.service.CommentService;
import com.allenanker.quora.service.SensitiveWordsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    SensitiveWordsService sensitiveWordsService;

    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {
        try {
            if (hostHolder.getUser() != null) {
                Comment comment = new Comment();
                comment.setContent(HtmlUtils.htmlEscape(sensitiveWordsService.filterBySensitiveWords(content)));
                comment.setCreatedDate(new Date());
                comment.setEntityType(EntityType.ENTITY_QUESTION);
                comment.setEntityId(questionId);
                comment.setUserId(hostHolder.getUser().getId());
                commentService.addComment(comment);
            } else {
                return "redirect:/loginPage";
            }
        } catch (Exception e) {
            logger.error("Add comment error: " + e.getMessage());
        }

        return "redirect:/question/" + questionId;
    }
}
