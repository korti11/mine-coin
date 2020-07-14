package io.korti.minecoin;

public class TransactionInput {

    private final String transactionOutputID;
    private TransactionOutput UTXO;

    public TransactionInput(String transactionOutputID) {
        this.transactionOutputID = transactionOutputID;
    }

    public String getTransactionOutputID() {
        return transactionOutputID;
    }

    public TransactionOutput getUTXO() {
        return UTXO;
    }

    public void setUTXO(TransactionOutput UTXO) {
        this.UTXO = UTXO;
    }
}
