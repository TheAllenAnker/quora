package com.allenanker.quora.dao;

import com.allenanker.quora.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, createdDate, userId, commentCount ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Update({"update ", TABLE_NAME, " set commentCount=#{commentCount} where id=#{questionId}"})
    int updateCommentCount(@Param("questionId") int questionId, @Param("commentCount") int commentCount);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " WHERE id=#{qid}"})
    Question selectQuestionById(@Param("qid") int qid);

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{title}, #{content}, #{createdDate}, " +
            "#{userId}, #{commentCount})"})
    int addQuestion(Question question);

    List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                                          @Param("limit") int limit);
}
