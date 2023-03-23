package com.leopard.kafka.dao.mapper;

import com.leopard.kafka.common.TyyxMapper;
import com.leopard.kafka.entity.messageinfo.MessageErrorLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采样用户表
 *
 * @author leopard
 */
@Mapper
public interface MessageErrorLogMapper extends TyyxMapper<MessageErrorLog> {

    /**
     * 插入数据
     */
    @Insert(
            "insert into message_error_log ( request_id, create_time, error_memo, message_type, message )" +
                    " VALUES  ( #{requestId} , now() , #{errorMemo} , #{messageType} , #{message} )"
    )
    int insertSimpleData(MessageErrorLog messageErrorLog);

}
