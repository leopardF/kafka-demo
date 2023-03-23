package com.leopard.kafka.entity.messageinfo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 消息异常记录表
 *
 * @author leopard
 */
@Data
@Table(name = "message_error_log")
public class MessageErrorLog implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    private Long id;
    /**
     * 唯一标识
     */
    private String requestId;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 异常备注信息
     */
    private String errorMemo;

    /**
     * 消息类型
     */
    private String messageType;
    /**
     * 消息内容
     */
    private String message;
    /**
     * 是否已经重新发送：0-为重发，1-重发一次，以此类推
     */
    private Integer againSend;

}
