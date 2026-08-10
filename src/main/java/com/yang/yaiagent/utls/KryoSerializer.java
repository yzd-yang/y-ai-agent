package com.yang.yaiagent.utls;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.messages.Message;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class KryoSerializer {

    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        // 关闭引用跟踪（提升性能，若对象图无循环引用）
        kryo.setReferences(false);
        // 不强制注册，允许未注册的类（但推荐注册，此处为简化）
        kryo.setRegistrationRequired(false);
        // 设置实例化策略（支持无默认构造函数的类）
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

        // 预先注册常用类（优化性能，避免每次写入类名）
        kryo.register(ArrayList.class);
        // 注册 Message 子类（根据你的 Spring AI 版本调整）
        kryo.register(org.springframework.ai.chat.messages.UserMessage.class);
        kryo.register(org.springframework.ai.chat.messages.AssistantMessage.class);
        kryo.register(org.springframework.ai.chat.messages.SystemMessage.class);
        // 如果有其他 Message 子类，也需注册

        return kryo;
    });

    public static byte[] serialize(Object obj) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             Output output = new Output(baos)) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeClassAndObject(output, obj);
            output.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Kryo 序列化失败", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        if (data == null || data.length == 0) {
            return null;
        }
        try (Input input = new Input(data)) {
            Kryo kryo = kryoThreadLocal.get();
            Object obj = kryo.readClassAndObject(input);
            if (clazz.isInstance(obj)) {
                return (T) obj;
            }
            throw new RuntimeException("反序列化类型不匹配，期望: " + clazz.getName() + ", 实际: " + obj.getClass().getName());
        } catch (Exception e) {
            throw new RuntimeException("Kryo 反序列化失败", e);
        }
    }
}