package io.simont.blocklog;

import io.simont.blocklog.bytes.ArrayOfBytes;
import io.simont.blocklog.bytes.Bytes512;
import io.simont.blocklog.bytes.Bytes64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

// Block chain inspired hashed block
class Block {

    private static Logger logger = Logger.getLogger(Block.class.getName());

    private String hash;
    private final String previousHash;
    private final String data;
    private final long timeStamp;
    private int nonce;

    Block(final String data, final String previousHash, final long timeStamp) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.hash = calculateBlockHash();
    }

    String mineBlock(final int prefix) {
        final String prefixString = new String(new char[prefix]).replace('\0', '0');
        while (!hash.substring(0, prefix)
                .equals(prefixString)) {
            nonce++;
            hash = calculateBlockHash();
        }
        return hash;
    }

    String calculateBlockHash() {
        final String dataToHash = previousHash + timeStamp + nonce + data;
        final MessageDigest digest;
        byte[] bytes = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
        }
        final StringBuilder buffer = new StringBuilder();
        assert bytes != null;
        for (byte b : bytes) {
            buffer.append(String.format("%02x", b));
        }
        return buffer.toString();
    }

    String getHash() {
        return this.hash;
    }

    String getPreviousHash() {
        return this.previousHash;
    }

    SerialBlock toSerialBlock() {
        return new SerialBlock(Bytes64.build(hash), Bytes64.build(previousHash), Bytes512.build(data), timeStamp, nonce);
    }

    static Block fromSerialBlock(final SerialBlock serialBlock) {
        final Block b = new Block(ArrayOfBytes.StringCompressor.decompress(serialBlock.baData.getBytes()),
                new String(serialBlock.baPreviousHash.getBytes(), StandardCharsets.UTF_8), serialBlock.timeStamp);
        b.nonce = serialBlock.nonce;
        b.hash = new String(serialBlock.baHash.getBytes(), StandardCharsets.UTF_8);
        return b;
    }

    @Override
    public String toString() {
        return "Block{" +
                "hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", data='" + data + '\'' +
                ", timeStamp=" + timeStamp +
                ", nonce=" + nonce +
                '}';
    }


}