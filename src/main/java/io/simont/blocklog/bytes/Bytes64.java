package io.simont.blocklog.bytes;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Bytes64 implements ArrayOfBytes {

    public static final int SIZE = 65;

    private final byte[] rawBytes;

    public static Bytes64 build(final String strIn) {
        final byte[] bytes = strIn.getBytes(StandardCharsets.UTF_8);
        assert bytes.length <= SIZE - 1;
        final byte len = (byte) bytes.length;
        return new Bytes64(ByteBuffer.allocate(SIZE).put(len).put(bytes).array());
    }

    public Bytes64(final byte[] bytesIn) {
        assert bytesIn.length <= SIZE;
        rawBytes = ByteBuffer.allocate(SIZE).put(bytesIn).array();
    }

    @Override
    public byte[] getRawBytes() {
        return rawBytes;
    }

    @Override
    public byte[] getBytes() {
        final int len = ((int) rawBytes[0]) & 0xFF;
        return ByteBuffer.allocate(len).put(rawBytes, 1, len).array();
    }
}
