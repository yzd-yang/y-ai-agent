package com.yang.yaiagent.chatmemory;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yang.yaiagent.domain.ChatMemoryEntity;
import com.yang.yaiagent.mapper.ChatMemoryMapper;
import com.yang.yaiagent.utls.KryoSerializer;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class MyBatisPlusChatMemoryRepository implements ChatMemoryRepository {

    private final ChatMemoryMapper mapper;

    public MyBatisPlusChatMemoryRepository(ChatMemoryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        // 使用 Kryo 序列化为字节数组
        byte[] data = KryoSerializer.serialize(messages);

        ChatMemoryEntity entity = new ChatMemoryEntity();
        entity.setConversationId(conversationId);
        entity.setMessagesJson(data);   // byte[]

        LambdaQueryWrapper<ChatMemoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMemoryEntity::getConversationId, conversationId);
        ChatMemoryEntity existing = mapper.selectOne(wrapper);

        if (existing != null) {
            existing.setMessagesJson(data);
            mapper.updateById(existing);
        } else {
            mapper.insert(entity);
        }
    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        LambdaQueryWrapper<ChatMemoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMemoryEntity::getConversationId, conversationId);
        ChatMemoryEntity entity = mapper.selectOne(wrapper);
        if (entity == null || entity.getMessagesJson() == null) {
            return Collections.emptyList();
        }
        // 反序列化 byte[] 为 List<Message>
        return KryoSerializer.deserialize(entity.getMessagesJson(), List.class);
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        LambdaQueryWrapper<ChatMemoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMemoryEntity::getConversationId, conversationId);
        mapper.delete(wrapper);
    }

    @Override
    public List<String> findConversationIds() {
        LambdaQueryWrapper<ChatMemoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ChatMemoryEntity::getConversationId);
        List<ChatMemoryEntity> entities = mapper.selectList(wrapper);
        return entities.stream()
                .map(ChatMemoryEntity::getConversationId)
                .toList();
    }
}