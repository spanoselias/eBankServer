package dto;

import dto.enums.Currency;

import java.time.LocalDateTime;

public class FetchTransaction extends Transaction {

    public static FetchTransaction create(final long transactionId,
                                          final long sourceBankAccountId,
                                          final long destinationBankAccountId,
                                          final double amount,
                                          final Currency currency,
                                          final LocalDateTime dateCreated,
                                          final String status,
                                          final String trasactionType) {

        return new FetchTransaction(transactionId, sourceBankAccountId, destinationBankAccountId, amount, currency, dateCreated, status, trasactionType);
    }

    public long getTransactionId() {
        return transactionId;
    }

    private FetchTransaction(final long transactionId,
                             final long sourceBankAccountId,
                             final long destinationBankAccountId,
                             final double amount,
                             final Currency currency,
                             final LocalDateTime dateCreated,
                             final String status,
                             final String trasactionType) {

        super(sourceBankAccountId, destinationBankAccountId, amount, currency, dateCreated, status, trasactionType);
        this.transactionId = transactionId;
    }

    private final long transactionId;

}
