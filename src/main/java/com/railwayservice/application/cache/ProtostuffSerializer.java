package com.railwayservice.application.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Protostuff序列化，基于Google的Protobuf。
 *
 * @author Ewing
 * @date 2017/3/30
 */
public class ProtostuffSerializer<T> implements RedisSerializer<T> {
    private RuntimeSchema<T> schema;

    /**
     * 快速初始化的构造方法。
     */
    public ProtostuffSerializer(Class<T> clazz) {
        schema = RuntimeSchema.createFrom(clazz);
    }

    @Override
    public byte[] serialize(T object) throws SerializationException {
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        return ProtostuffIOUtil.toByteArray(object, schema, buffer);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        T object = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, object, schema);
        return object;
    }
}
