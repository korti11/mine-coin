package io.korti.minecoin;

import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Wallet {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    private final BlockChain blockChain;
    private final Map<String, TransactionOutput> UTXOs = new HashMap<>();

    public Wallet(BlockChain blockChain) {
        this.blockChain = blockChain;
        generateKeyPair();
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(2048, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public float getBalance() {
        float total = 0;
        List<TransactionOutput> outputs = blockChain.getUTXOs().values().stream()
                .filter(UTXO -> UTXO.isMine(this.publicKey)).collect(Collectors.toList());
        for(TransactionOutput output : outputs) {
            UTXOs.put(output.getId(), output);
            total += output.getValue();
        }
        return total;
    }

    public Transaction sendFunds(PublicKey receiver, float value) {
        if(getBalance() < value) {
            System.out.println("# Not enough funds to send transaction. Transaction discarded.");
            return null;
        }

        List<TransactionInput> inputs = new ArrayList<>();

        float total = 0;
        for(TransactionOutput output : UTXOs.values()) {
            total += output.getValue();
            inputs.add(new TransactionInput(output.getId()));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(this.publicKey, receiver, value, inputs);
        newTransaction.generateSignature(privateKey);

        inputs.forEach(input -> UTXOs.remove(input.getTransactionOutputID()));
        return newTransaction;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "balance=" + getBalance() +
                '}';
    }
}
