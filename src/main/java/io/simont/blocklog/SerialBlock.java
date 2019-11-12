package io.simont.blocklog;

import io.simont.blocklog.bytes.Bytes512;
import io.simont.blocklog.bytes.Bytes64;

class SerialBlock {

    Bytes64 baHash;
    Bytes64 baPreviousHash;
    Bytes512 baData;
    long timeStamp;
    int nonce;

    SerialBlock(final Bytes64 baHash, final Bytes64 baPreviousHash, final Bytes512 baData, final long timeStamp, final int nonce) {
        this.baHash = baHash;
        this.baPreviousHash = baPreviousHash;
        this.baData = baData;
        this.timeStamp = timeStamp;
        this.nonce = nonce;
    }

    SerialBlock() {
    }

}
