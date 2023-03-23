package com.leopard.kafka.service;

import com.leopard.kafka.dao.mapper.MessageErrorLogMapper;
import com.leopard.kafka.entity.messageinfo.MessageErrorLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 日志数据处理
 *
 * @author leopard
 */
@Slf4j
@Service
public class LogService {

    @Autowired
    private MessageErrorLogMapper messageErrorLogMapper;


    /**
     * 保存异常消息数据
     *
     * @param requestId   唯一标识
     * @param messageType 消息类型
     * @param errorMemo   异常备注
     * @param message     消息字符串内容
     */
    public void saveMsgErrorLog(String requestId, String messageType, String errorMemo, String message) {

        MessageErrorLog errorLog = new MessageErrorLog();
        errorLog.setRequestId(requestId);
        errorLog.setErrorMemo(errorMemo);
        errorLog.setMessageType(messageType);
        errorLog.setMessage(message);
        messageErrorLogMapper.insertSimpleData(errorLog);
    }


}
