package com.leopard.kafka.consumer;

import com.alibaba.fastjson.JSONObject;
import com.leopard.kafka.common.GlobalLock;
import com.leopard.kafka.common.Response;
import com.leopard.kafka.common.contant.RedisConstant;
import com.leopard.kafka.common.utils.RedisUtil;
import com.leopard.kafka.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * 服务消费
 */
@Slf4j
@Component
public class ServiceConsumer {


    @Autowired
    private LogService logService;

    @Autowired
    private GlobalLock globalLock;

    @Autowired
    private RedisUtil redisUtil;


    @KafkaListener(topics = {"${spring.kafka.declare-topic}"})
    public void receive(@Payload String message, @Headers MessageHeaders headers) {
        log.info("declare-topic MessageConsumer 接收到消息：" + message);
        JSONObject jsonObject = JSONObject.parseObject(message);
        String msgType = jsonObject.getString("msgType");
        String requestId = jsonObject.getString("requestId");
        if (StringUtils.isEmpty(msgType)) {
            logService.saveMsgErrorLog(requestId, msgType, "消息类型不符合处理要求", message);
            //处理完后的数据，均手动确认消费处理，不再发送重试，异常message查看消息异常表
            return;
        }

        String lockKey = RedisConstant.REPORT_MSG + requestId;
        String redisKey = RedisConstant.REPORT_MSG + msgType + ":" + requestId;
        //加锁--严格的话，还需要存储消息ID，进行幂等处理
        boolean flag = globalLock.releaseLock(lockKey, false);
        if(!flag){
            logService.saveMsgErrorLog(requestId, msgType, "消息重复且尚未消费完", message);
//            return "业务请求繁忙，请稍后再试";
            return;
        }

        if (null != redisUtil.getString(redisKey)) {
            log.info("本次信息已被消费：" + message);
            globalLock.releaseLock(lockKey, true);
            return;
        }
        if ("XXXXDX".equals(msgType)) {
            //进来的话，随意设置一个值，记录正在操作
            redisUtil.setString(redisKey, "1", 300);
            //处理业务
            Response response = new Response();
//            Response response = service.xxxxxx;
            if (!response.completed()) {
                logService.saveMsgErrorLog(requestId, msgType, response.getMessage(), message);
            }
        } else {
            logService.saveMsgErrorLog(requestId, msgType, "消息类型未找到相关处理方法", message);
        }
        //解锁
        globalLock.releaseLock(lockKey, true);
    }
}