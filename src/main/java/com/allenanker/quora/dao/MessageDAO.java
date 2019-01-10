package com.allenanker.quora.dao;

import com.allenanker.quora.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " fromId, toId, content, hasRead, conversationId, createdDate ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;
    String ANY_SELECT_FIELDS = " count(id) as id, any_value(fromId) as fromId, any_value(toId) as toId, any_value" +
            "(content) as content, any_value(hasRead) as hasRead, any_value(conversationId) as conversationId, " +
            "any_value(createdDate) as createdDate";

    @Select({"select count(id) from ", TABLE_NAME, " WHERE conversationId=#{conversationId}"})
    int getMessageCount(@Param("conversationId") String conversationId);

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{fromId}, #{toId}, #{content}, " +
            "#{hasRead}, #{conversationId}, #{createdDate})"})
    int addMessage(Message message);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " WHERE conversationId=#{conversationId} order by id " +
            "desc limit #{offset}, #{limit}"})
    List<Message> selectMessagesByConversationId(@Param("conversationId") String conversationId,
                                                 @Param("offset") int offset,
                                                 @Param("limit") int limit);

    @Select({"select ", ANY_SELECT_FIELDS, " from ", TABLE_NAME, " where fromId=#{userId} or toId=#{userId}" +
            " group by conversationId order by any_value(createdDate) desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);
}
