package io.simont.blocklog.utils;


import io.simont.blocklog.bytes.Bytes512;
import io.simont.blocklog.bytes.Bytes64;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.function.BiFunction;

public enum Types {

    BYTE(Byte.TYPE, 1, (buffer, index, object) -> buffer.put(index, (byte) object), ByteBuffer::get),
    SHORT(Short.TYPE, 2, (buffer, index, object) -> buffer.putShort(index, (short) object), ByteBuffer::getShort),
    INT(Integer.TYPE, 4, (buffer, index, object) -> buffer.putInt(index, (int) object), ByteBuffer::getInt),
    LONG(Long.TYPE, 8, (buffer, index, object) -> buffer.putLong(index, (long) object), ByteBuffer::getLong),
    FLOAT(Float.TYPE, 4, (buffer, index, object) -> buffer.putFloat(index, (float) object), ByteBuffer::getFloat),
    DOUBLE(Double.TYPE, 8, (buffer, index, object) -> buffer.putDouble(index, (double) object), ByteBuffer::getDouble),
    CHAR(Character.TYPE, 2, (buffer, index, object) -> buffer.putChar(index, (char) object), ByteBuffer::getChar),
    BOOLEAN(Boolean.TYPE, 1, (buffer, index, object) -> buffer.put(index, fromBoolean((boolean) object)), (buffer, index) -> toBoolean(buffer.get(index))),
    BYTES64(Bytes64.class, Bytes64.SIZE, (buffer, index, object) -> {
        final byte[] bytes = ((Bytes64) object).getRawBytes();
        int idx = index;
        for (int i = 0; i < Bytes64.SIZE; i++) {
            buffer.put(idx++, bytes[i]);
        }
    }, (buffer, index) -> {
        final byte[] bytes = new byte[Bytes64.SIZE];
        int idx = index;
        for (int i = 0; i < Bytes64.SIZE; i++) {
            bytes[i] = buffer.get(idx++);
        }
        return new Bytes64(bytes);
    }),
    BYTES512(Bytes512.class, Bytes512.SIZE, (buffer, index, object) -> {
        final byte[] bytes = ((Bytes512) object).getRawBytes();
        int idx = index;
        for (int i = 0; i < Bytes512.SIZE; i++) {
            buffer.put(idx++, bytes[i]);
        }
    }, (buffer, index) -> {
        final byte[] bytes = new byte[Bytes512.SIZE];
        int idx = index;
        for (int i = 0; i < Bytes512.SIZE; i++) {
            bytes[i] = buffer.get(idx++);
        }
        return new Bytes512(bytes);
    });

    private final Class<?> type;
    private final int size;
    private transient TriConsumer<ByteBuffer, Integer, Object> writeField;
    private transient BiFunction<ByteBuffer, Integer, Object> readField;

    Types(Class<?> type, int size, TriConsumer<ByteBuffer, Integer, Object> writeField, BiFunction<ByteBuffer, Integer, Object> readField) {
        this.type = type;
        this.size = size;
        this.writeField = writeField;
        this.readField = readField;
    }

    public static Types of(Class<?> type) {
        String name = type.getName().toUpperCase();
        // Remove package name if it exists
        if (name.contains(".")) {
            name = name.substring(name.lastIndexOf(".") + 1);
        }
        return Types.valueOf(name);
    }

    public static byte fromBoolean(boolean b) {
        return (byte) (b ? 0 : 1);
    }

    public static boolean toBoolean(byte s) {
        return s == 0;
    }

    public static <T> int offSetForClass(Class<T> fooClass) {
        return Classes.supportedFixedLengthFields(fooClass)
                .map(Field::getType)
                .map(Types::of)
                .mapToInt(Types::getSize)
                .sum();
    }

    public int getSize() {
        return size;
    }

    public TriConsumer<ByteBuffer, Integer, Object> getWriteField() {
        return writeField;
    }

    public BiFunction<ByteBuffer, Integer, Object> getReadField() {
        return readField;
    }

    public interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }
}
