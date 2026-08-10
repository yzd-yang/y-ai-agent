package com.yang.yaiagent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.yaiagent.domain.ChatMemoryEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMemoryMapper extends BaseMapper<ChatMemoryEntity> {
    // 基础 CRUD 方法已由 BaseMapper 提供，无需额外定义
}