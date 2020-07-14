package io.korti.minecoin;

import java.util.Random;

public class MineCoin {

    public static void main(String[] args) {

        BlockChain mineChain = new BlockChain();
        Wallet coinBase = new Wallet(mineChain);
        Wallet walletA = new Wallet(mineChain);
        Wallet walletB = new Wallet(mineChain);

        Transaction genesisTransaction = Transaction.getGenesisTransaction(coinBase, mineChain);
        Block genesisBlock = new Block("0");

        genesisBlock.addTransaction(mineChain, genesisTransaction);
        while (!genesisBlock.mineBlock(BlockChain.DIFFICULTY));
        mineChain.addBlock(genesisBlock);

        Transaction transaction;
        Block curBlock = new Block(genesisBlock.getHash());

        transaction = coinBase.sendFunds(walletA.getPublicKey(), 275f);
        curBlock.addTransaction(mineChain, transaction);
        transaction = coinBase.sendFunds(walletB.getPublicKey(), 365f);
        curBlock.addTransaction(mineChain, transaction);

        while (!curBlock.mineBlock(BlockChain.DIFFICULTY));

        mineChain.addBlock(curBlock);

        Random random = new Random();
        for(int i = 2; i < 6; i++) {
            curBlock = new Block(mineChain.lastBlock().getHash());
            transaction = walletA.sendFunds(walletB.getPublicKey(), random.nextFloat() * 100f);
            curBlock.addTransaction(mineChain, transaction);
            transaction = walletA.sendFunds(walletB.getPublicKey(), random.nextFloat() * 100f);
            curBlock.addTransaction(mineChain, transaction);

            while (!curBlock.mineBlock(BlockChain.DIFFICULTY));

            mineChain.addBlock(curBlock);
        }

        System.out.println("Is block chain valid: " + mineChain.isChainValid());
        System.out.println(mineChain);
        System.out.println(coinBase);
        System.out.println(walletA);
        System.out.println(walletB);

    }

}
