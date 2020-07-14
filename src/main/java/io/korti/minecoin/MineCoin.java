package io.korti.minecoin;

public class MineCoin {

    public static void main(String[] args) {

        BlockChain mineChain = new BlockChain();

        Block genesisBlock = new Block("0", "This is the genesis block.");

        Block currentBlock = genesisBlock;
        for(int i = 0; i < 3; i++) {
            System.out.println("Working on block: " + i);
                while (!currentBlock.mineBlock(BlockChain.DIFFICULTY));
                mineChain.addBlock(currentBlock);
            currentBlock = new Block(currentBlock.getHash(), "This is the " + (i + 1) + ". block.");
        }

        System.out.println("Is mine chain valid: " + mineChain.isChainValid());
        System.out.println(mineChain);
    }

}
