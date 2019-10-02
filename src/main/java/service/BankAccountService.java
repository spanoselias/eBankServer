package service;

import dao.BankAccountDAO;
import dto.BankAccount;
import dto.FetchBankAccount;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class BankAccountService {

    public static BankAccountService create(final BankAccountDAO bankAccountDAO) {
        return new BankAccountService(bankAccountDAO);
    }

    public Optional<FetchBankAccount> getBankAccountById(final Connection conn, final long id) throws SQLException {
        return bankAccountDAO.getBankAccountById(conn, id);
    }

    public long addBankAccount(final Connection conn, final BankAccount addBankAccount) throws SQLException {
        return bankAccountDAO.addBankAccount(conn, addBankAccount);
    }

    private BankAccountService(final BankAccountDAO bankAccountDAO) {
        this.bankAccountDAO = bankAccountDAO;
    }

    private final BankAccountDAO bankAccountDAO;

}
