package dto;

import dto.enums.Currency;

public final class FetchBankAccount extends BankAccount {

    public static FetchBankAccount create(final long accountId,
                                          final String ownerName,
                                          final double balance,
                                          final Currency currency) {
        return new FetchBankAccount(accountId, ownerName, balance, currency);
    }

    public long getAccountId() {
        return accountId;
    }

    private FetchBankAccount(final long accountId,
                             final String ownerName,
                             final double balance,
                             final Currency currency
    ) {
        super(ownerName, balance, currency);
        this.accountId = accountId;
    }

    private final long accountId;
}
