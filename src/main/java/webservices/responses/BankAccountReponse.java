package webservices.responses;

public class BankAccountReponse {

    public static BankAccountReponse create(final long bankAccountIdCreated) {
        return new BankAccountReponse(bankAccountIdCreated);
    }

    public long getBankAccountIdCreated() {
        return bankAccountIdCreated;
    }

    private BankAccountReponse(final long bankAccountIdCreated) {
        this.bankAccountIdCreated = bankAccountIdCreated;
    }

    private final long bankAccountIdCreated;

}
