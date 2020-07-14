package io.korti.minecoin;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Transaction {

    private String id;
    private PublicKey sender;
    private PublicKey receiver;
    private float value;
    private byte[] signature;

    private List<TransactionInput> inputs = new ArrayList<>();
    private List<TransactionOutput> outputs = new ArrayList<>();

    private static int sequence = 0;

    public static Transaction getGenesisTransaction(Wallet coinBase, BlockChain blockChain) {
        Transaction transaction = new Transaction(coinBase.getPublicKey(), coinBase.getPublicKey(), 1000f, null);
        transaction.generateSignature(coinBase.getPrivateKey());
        transaction.id = "0";
        TransactionOutput output = new TransactionOutput(transaction.receiver, transaction.value, transaction.id);
        transaction.outputs.add(output);
        blockChain.addUTXO(output);

        return transaction;
    }

    public Transaction(PublicKey sender, PublicKey receiver, float value, List<TransactionInput> inputs) {
        this.sender = sender;
        this.receiver = receiver;
        this.value = value;
        this.inputs = inputs;
    }

    public String getId() {
        return id;
    }

    private String calculateHash() {
        sequence++;
        return Util.toSHA256(Util.keyToString(this.sender) + Util.keyToString(this.receiver) + value + sequence);
    }

    public boolean processTransaction(BlockChain blockChain) {
        if(!verifySignature()) {
            System.out.println("#Transaction signature failed to verify.");
            return false;
        }

        for(TransactionInput i : inputs) {
            i.setUTXO(blockChain.getUTXOs().get(i.getTransactionOutputID()));
        }

        float inputsValue = getInputsValue();
        if(inputsValue < BlockChain.MIN_TRANSACTION) {
            System.out.println("Inputs value to small for a transaction. Current inputs value: " + inputsValue);
            return false;
        }

        float newBalance = inputsValue - value;
        this.id = calculateHash();
        outputs.add(new TransactionOutput(this.receiver, value, this.id));
        outputs.add(new TransactionOutput(this.sender, newBalance, this.id));

        this.outputs.forEach(blockChain::addUTXO);

        this.inputs.stream().filter(i -> i.getUTXO() != null).forEach(i -> blockChain.removeUTXO(i.getUTXO().getId()));

        return true;
    }

    private float getInputsValue() {
        return this.inputs.stream().filter(i -> i.getUTXO() != null)
                .map(i -> i.getUTXO().getValue()).reduce(0f, (total, value) -> total += value);
    }

    private float getOutputsValue() {
        return this.outputs.stream().map(TransactionOutput::getValue).reduce(0f, (total, value) -> total += value);
    }

    public void generateSignature(PrivateKey key) {
        String data = Util.keyToString(this.sender) + Util.keyToString(this.receiver) + value;
        this.signature = Util.applySHA256RSASig(key, data);
    }

    public boolean verifySignature() {
        String data = Util.keyToString(this.sender) + Util.keyToString(this.receiver) + value;
        return Util.verifySHA256RSASig(this.sender, data, this.signature);
    }
}
