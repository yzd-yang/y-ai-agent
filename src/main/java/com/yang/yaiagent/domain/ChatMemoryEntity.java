package com.yang.yaiagent.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("chat_memory")   // 指定表名
public class ChatMemoryEntity {

    @TableId(type = IdType.AUTO)   // 自增主键
    private Long id;

    private String conversationId;

    private byte[] messagesJson;

    private LocalDateTime lastUpdated;
}