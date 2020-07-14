package io.korti.minecoin;

import java.util.ArrayList;
import java.util.List;

public class BlockChain {

    public static int DIFFICULTY = 4;

    private final List<Block> blocks = new ArrayList<>();

    public void addBlock(Block block) {
        this.blocks.add(block);
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
