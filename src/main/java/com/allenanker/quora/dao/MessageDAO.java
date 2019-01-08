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

    @Select({"select count(id) from ", TABLE_NAME, " WHERE conversationId=#{conversationId}"})
    int getMessageCount(@Param("conversationId") String conversationId);

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{fromId}, #{toId}, #{content}, " +
            "#{hasRead}, #{conversationId}, #{createdDate})"})
    int addMessage(Message message);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " WHERE conversationId=#{conversationId} order by desc " +
            "limit #{offset}, #{limit}"})
    List<Message> selectMessagesByConversationId(@Param("conversationId") String conversationId,
                                                 @Param("offset") int offset,
                                                 @Param("limit") int limit);
}
