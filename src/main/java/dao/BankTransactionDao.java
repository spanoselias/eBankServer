package dao;

import dto.Transaction;
import dto.FetchTransaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Stream;

public interface BankTransactionDao {

    /**
     * It creates a new transaction and returns the id of the new transaction created
     *
     * @param conn the connection for the database
     * @param addTransaction Transaction object that will be inserted in the database.
     * @return It returned the id of the new transaction that is created.
     */
    long addBankTransaction(final Connection conn, final Transaction addTransaction) throws SQLException;

    /**
     * Given a bank account id, it fetches all the transactions related to the given id.
     *
     * @param connection the connection for the database
     * @param bankAccountId the id of a specific bank account.
     * @return It returns a stream that contains all the transaction for the given bank account id.
     */
    Stream<FetchTransaction> getBankTransactionBySourceId(final Connection connection, final long bankAccountId) throws SQLException;

}
