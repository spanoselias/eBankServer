package samples;

import dto.BankAccount;
import dto.enums.Currency;
import dto.FetchBankAccount;
import dto.Transaction;

import java.time.LocalDateTime;

public class SampleObjects {

    public final static BankAccount sampleAddBankAccount =
            FetchBankAccount.create(
                    1,
                    "Elias",
                    100,
                    Currency.EUR);

    public final static Transaction sampleAddTransaction =
            Transaction.create(
                    1,
                    2,
                    100,
                    Currency.EUR,
                    LocalDateTime.now().withNano(0),
                    "Approved",
                    "1");

}
