package com.allenanker.quora.dao;

import com.allenanker.quora.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " userId, entityId, entityType, content, createdDate, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Select({"select count(id) from ", TABLE_NAME, " WHERE entityId=#{entityId} and " +
            "entityType=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{userId}, #{entityId}, #{entityType}, " +
            "#{content}, #{createdDate}, #{status})"})
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " WHERE entityId=#{entityId} and " +
            "entityType=#{entityType}"})
    List<Comment> selectCommentsByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id}"})
    int deleteComment(@Param("id") int id, @Param("status") int status);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " WHERE id=#{commentId}"})
    Comment selectCommentById(int commentId);
}
