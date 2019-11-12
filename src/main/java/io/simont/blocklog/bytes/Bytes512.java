package io.simont.blocklog.bytes;

import java.nio.ByteBuffer;

public class Bytes512 implements ArrayOfBytes {

    public static final int SIZE = 514;

    private final byte[] rawBytes;

    public static Bytes512 build(final String strIn) {
        final byte[] bytes = StringCompressor.compress(strIn);
        assert bytes.length <= SIZE - 2;

        byte[] lenBytes = new byte[]{
                (byte) ((bytes.length >> 8) & 0xff),
                (byte) ((bytes.length) & 0xff)
        };

        return new Bytes512(ByteBuffer.allocate(SIZE).put(lenBytes).put(bytes).array());
    }

    public Bytes512(final byte[] bytesIn) {
        assert bytesIn.length <= SIZE;
        rawBytes = ByteBuffer.allocate(SIZE).put(bytesIn).array();
    }

    @Override
    public byte[] getRawBytes() {
        return rawBytes;
    }

    @Override
    public byte[] getBytes() {
        final int len = (0xff & rawBytes[0]) << 8 | (0xff & rawBytes[1]);
        return ByteBuffer.allocate(len).put(rawBytes, 2, len).array();
    }
}
