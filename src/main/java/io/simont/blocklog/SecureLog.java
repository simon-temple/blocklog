package io.simont.blocklog;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class SecureLog {

    private static final int PREFIX = 2;
    private static final String PREFIX_STRING = new String(new char[PREFIX]).replace('\0', '0');

    private final Store<SerialBlock> store;

    private String previousHash;

    public SecureLog(final String filePath) {
        store = Store.of(SerialBlock.class).from(filePath);
        init();
    }

    public void restart() {
        store.reset();
        init();
    }

    public void close() {
        store.close();
    }

    private void init() {
        final Optional<SerialBlock> first = store
                .reverse()
                .findFirst();

        if (first.isPresent()) {
            previousHash = Block.fromSerialBlock(first.get()).getHash();
        } else {
            final Block genesisBlock = new Block("Genesis Block", "0", System.currentTimeMillis());
            previousHash = genesisBlock.mineBlock(PREFIX);
            store.put(genesisBlock.toSerialBlock());
        }
    }

    public void add(final String data) {
        final Block block = new Block(data, previousHash, System.currentTimeMillis());
        previousHash = block.mineBlock(PREFIX);
        store.put(block.toSerialBlock());
    }

    public boolean isValid() {
        AtomicReference<String> previousHash = new AtomicReference<>("0");
        Optional<SerialBlock> badBlock = store.all().filter(sb -> {
            final Block b = Block.fromSerialBlock(sb);
            final boolean isValid = b.getHash().equals(b.calculateBlockHash())
                    && previousHash.get().equals(b.getPreviousHash())
                    && b.getHash().substring(0, PREFIX).equals(PREFIX_STRING);
            previousHash.set(b.getHash());
            return !isValid;
        }).findFirst();

        return !badBlock.isPresent();
    }

    public void dump() {
        store.all().forEach(sb -> {
            final Block b = Block.fromSerialBlock(sb);
            System.out.println(b);
        });
    }

}
