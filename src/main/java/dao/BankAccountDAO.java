package dao;

import dto.BankAccount;
import dto.FetchBankAccount;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface BankAccountDAO {

    /**
     * Returns the id of the bank account that is created
     *
     * @param conn the connection for the database
     * @param addBankAccount Bank Account object that will be inserted in the database.
     * @return It returned the bank account id of the bank account that is created.
     */
    long addBankAccount(final Connection conn, final BankAccount addBankAccount) throws SQLException;

    /**
     * Returns Fetch bank Account object by bank account id.
     *
     * @param connection the connection for the database
     * @param accountId Bank Account object id
     * @return Optional<FetchBankAccount> object with given id. If the given id does not exist
     *         in the db, it returned optional.empty.
     */
    Optional<FetchBankAccount> getBankAccountById(final Connection connection, final long accountId) throws SQLException;

    /**
     * Returns Fetch and lock the bank Account object for the given bank account id.
     * This is used when we perform transaction for the given id, and we lock it in order
     * to avoid any inconsistency. We use select for update approach which is an efficient
     * method for locking since the database does a row-level lock granularity.
     *
     * @param connection the connection for the database
     * @param accountId Bank Account object id
     * @return Optional<FetchBankAccount> object with given id. If the given id does not exist
     *         in the db, it returned optional.empty.
     */
    Optional<FetchBankAccount> getAndLockBankAccountById(final Connection connection, final long accountId) throws SQLException;

    /**
     * Given a bank account id and the new balance, it updates the balance of account with the new balance.
     *
     * @param connection the connection for the database
     * @param accountId for the Bank Account object which the amount will be updated
     * @param newAmount the new amount of the given bank account that will be updated
     * @return it returns the number of rows which are updated.
     */
    int updateBankAccountById(final Connection connection, final long accountId, final double newAmount) throws SQLException;
}
