package webservices.responses;

public class TransactionReponse {

    private TransactionReponse(long fromBankAccountTrasactionId, long toBankAccountTrasactionId) {
        this.fromBankAccountTrasactionId = fromBankAccountTrasactionId;
        this.toBankAccountTrasactionId = toBankAccountTrasactionId;
    }

    public long getFromBankAccountTrasactionId() {
        return fromBankAccountTrasactionId;
    }

    public long getToBankAccountTrasactionId() {
        return toBankAccountTrasactionId;
    }

    private final long fromBankAccountTrasactionId;
    private final long toBankAccountTrasactionId;

    public static TransactionReponse create(long fromBankAccountTrasactionId, long toBankAccountTrasactionId) {
        return new TransactionReponse(fromBankAccountTrasactionId, toBankAccountTrasactionId);
    }
}
