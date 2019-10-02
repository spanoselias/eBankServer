package dto;

import dto.enums.Currency;

import java.time.LocalDateTime;

public class Transaction {

    public static Transaction create(final long sourceBankAccountId,
                                     final long destinationBankAccountId,
                                     final double amount,
                                     final Currency currency,
                                     final LocalDateTime dateCreated,
                                     final String status,
                                     final String trasactionType) {

        return new Transaction(sourceBankAccountId, destinationBankAccountId, amount, currency, dateCreated, status, trasactionType);
    }

    public long getSourceBankAccountId() {
        return sourceBankAccountId;
    }

    public long getDestinationBankAccountId() {
        return destinationBankAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public String getStatus() {
        return status;
    }

    public String getTrasactionType() {
        return trasactionType;
    }

    public Transaction(final long sourceBankAccountId,
                       final long destinationBankAccountId,
                       final double amount,
                       final Currency currency,
                       final LocalDateTime dateCreated,
                       final String status,
                       final String trasactionType) {

        this.sourceBankAccountId = sourceBankAccountId;
        this.destinationBankAccountId = destinationBankAccountId;
        this.amount = amount;
        this.currency = currency;
        this.dateCreated = dateCreated;
        this.status = status;
        this.trasactionType = trasactionType;
    }

    private final long sourceBankAccountId;
    private final long destinationBankAccountId;
    private final double amount;
    private final Currency currency;
    private final LocalDateTime dateCreated;
    private final String status;
    private final String trasactionType;

}
