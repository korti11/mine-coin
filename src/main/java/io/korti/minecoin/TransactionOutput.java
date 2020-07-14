package io.korti.minecoin;

import java.security.PublicKey;

public class TransactionOutput {

    private final String id;
    private final PublicKey receiver;
    private final float value;
    private final String parentTransactionID;

    public TransactionOutput(PublicKey receiver, float value, String parentTransactionID) {
        this.receiver = receiver;
        this.value = value;
        this.parentTransactionID = parentTransactionID;

        this.id = Util.toSHA256(Util.keyToString(this.receiver) + value + parentTransactionID);
    }

    public String getId() {
        return id;
    }

    public float getValue() {
        return value;
    }

    public boolean isMine(PublicKey key) {
        return receiver.equals(key);
    }
}
