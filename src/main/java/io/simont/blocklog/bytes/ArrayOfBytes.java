package io.simont.blocklog.bytes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

public interface ArrayOfBytes {
    byte[] getBytes();

    byte[] getRawBytes();

    enum StringCompressor {
        ;

        public static byte[] compress(final String text) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final byte[] bytesIn;
            try {
                final OutputStream out = new DeflaterOutputStream(byteArrayOutputStream);
                bytesIn = text.getBytes(StandardCharsets.UTF_8);
                out.write(bytesIn);
                out.close();
            } catch (IOException e) {
                throw new AssertionError(e);
            }
            byte[] bytesOut =  byteArrayOutputStream.toByteArray();
            return bytesOut;
        }

        public static String decompress(final byte[] bytes) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                final OutputStream out = new InflaterOutputStream(byteArrayOutputStream);
                out.write(bytes);
                out.close();
                return new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }
    }

}
