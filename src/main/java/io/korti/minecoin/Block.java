package io.korti.minecoin;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

public class Block {

    private String hash;
    private String prevHash;
    private String data;
    private long timestamp;
    private int nonce;

    private String checkCache = "";

    public Block(String prevHash, String data) {
        this.prevHash = prevHash;
        this.data = data;
        this.timestamp = LocalDate.now().getLong(ChronoField.EPOCH_DAY);

        this.hash = calculateHash();    // This should always be the last statement.
    }

    public String getHash() {
        return hash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public String calculateHash() {
        return Util.toSHA256(prevHash + timestamp + nonce + data);
    }

    public boolean mineBlock(int difficulty) {
        if(mined(difficulty)) {
            return true;
        }

        nonce++;
        this.hash = calculateHash();
        return mined(difficulty);
    }

    public boolean mined(int difficulty) {
        if(checkCache.length() != difficulty) {
            checkCache = new String(new char[difficulty]).replace("\0", "0");
        }

        return this.hash.substring(0, difficulty).equals(checkCache);
    }

    @Override
    public String toString() {
        return "Block{" +
                "hash='" + hash + '\'' +
                ", prevHash='" + prevHash + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
