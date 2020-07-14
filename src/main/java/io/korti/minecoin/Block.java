package io.korti.minecoin;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

public class Block {

    private String hash;
    private String prevHash;
    private long timestamp;
    private int nonce;

    private final List<Transaction> transactions = new ArrayList<>();
    private String merkelRoot = "";

    private String checkCache = "";

    public Block(String prevHash) {
        this.prevHash = prevHash;
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
        return Util.toSHA256(prevHash + timestamp + nonce + merkelRoot);
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

    public boolean addTransaction(BlockChain blockChain, Transaction transaction) {
        if(transaction == null) {
            return false;
        }

        if(!prevHash.equals("0") && !transaction.processTransaction(blockChain)) {
            System.out.println("# Transaction failed to process. Discarded.");
            return false;
        }

        this.transactions.add(transaction);
        this.merkelRoot = Util.getMerkelRoot(this.transactions);

        System.out.println("# Transaction successfully added to the block.");
        return true;
    }

    @Override
    public String toString() {
        return "Block{" +
                "hash='" + hash + '\'' +
                ", prevHash='" + prevHash + '\'' +
                ", merkelRoot='" + merkelRoot + '\'' +
                '}';
    }
}
