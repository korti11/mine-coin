package io.korti.minecoin;

import java.util.*;

public class BlockChain {

    public static int DIFFICULTY = 4;
    public static float MIN_TRANSACTION = 0.01f;

    private final List<Block> blocks = new ArrayList<>();
    private final Map<String, TransactionOutput> UTXOs = new HashMap<>();

    public void addBlock(Block block) {
        this.blocks.add(block);
    }

    public void addUTXO(TransactionOutput output) {
        this.UTXOs.put(output.getId(), output);
    }

    public void removeUTXO(String id) {
        this.UTXOs.remove(id);
    }

    public Map<String, TransactionOutput> getUTXOs() {
        return Collections.unmodifiableMap(this.UTXOs);
    }

    public Block lastBlock() {
        return blocks.get(blocks.size() - 1);
    }

    public boolean isChainValid() {
        Block curBlock;
        Block prevBlock;

        for (int i = 1; i < blocks.size(); i++) {
            curBlock = this.blocks.get(i);
            prevBlock = this.blocks.get(i - 1);

            if(!curBlock.getHash().equals(curBlock.calculateHash())) {
                System.out.println("Current block hash is not equal.");
                return false;
            }

            if(!prevBlock.getHash().equals(curBlock.getPrevHash())) {
                System.out.println("Previous block hash is not equal.");
                return false;
            }

            if(!curBlock.mined(DIFFICULTY)) {
                System.out.println("Current block hasn't been mined.");
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "BlockChain{" +
                "blocks=" + blocks +
                '}';
    }
}
