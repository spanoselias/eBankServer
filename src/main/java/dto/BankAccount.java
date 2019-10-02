package dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dto.enums.Currency;

public class BankAccount {

    @JsonCreator
    public static BankAccount create(@JsonProperty(value = "ownerName", required = true) final String ownerName,
                                     @JsonProperty(value = "balance", required = true) final double balance,
                                     @JsonProperty(value = "currency", required = true) final Currency currency) {

        return new BankAccount(ownerName, balance, currency);
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Double getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }


    BankAccount(final String ownerName,
                final Double balance,
                final Currency currency) {
        this.ownerName = ownerName;
        this.balance = balance;
        this.currency = currency;

    }

    private String ownerName;
    private Double balance;
    private Currency currency;
}
