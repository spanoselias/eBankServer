package dao.daoImpl;

import dao.BankTransactionDao;
import dto.Transaction;
import dto.enums.Currency;
import dto.FetchTransaction;
import dto.enums.TransactionType;
import utils.DBUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public final class BankTransactionDaoImpl implements BankTransactionDao {

    public static BankTransactionDao getInstance() {
        return sPrivateInstance;
    }

    @Override
    public long addBankTransaction(final Connection conn,
                                   final Transaction addTransaction) throws SQLException {
        return DBUtils.writeToDB(
                conn,
                sAddBankTransaction,
                pstmt -> {

                    int idx = 0;
                    pstmt.setLong(++idx, addTransaction.getSourceBankAccountId());
                    pstmt.setLong(++idx, addTransaction.getDestinationBankAccountId());
                    pstmt.setDouble(++idx, addTransaction.getAmount());
                    pstmt.setInt(++idx, addTransaction.getCurrency().getId());
                    pstmt.setTimestamp(++idx, Timestamp.valueOf(addTransaction.getDateCreated().withNano(0)));
                    pstmt.setString(++idx, addTransaction.getStatus());
                    pstmt.setString(++idx, addTransaction.getTrasactionType());
                }
        );
    }

    @Override
    public Stream<FetchTransaction> getBankTransactionBySourceId(Connection connection, long bankAccountId) throws SQLException {
        return DBUtils.selectQuery(
                connection,
                sGetBankTransaction,
                pstmt -> pstmt.setLong(1, bankAccountId),
                rs -> {
                    int idx = 0;

                    return FetchTransaction.create(
                            rs.getLong(++idx),
                            rs.getLong(++idx),
                            rs.getLong(++idx),
                            rs.getDouble(++idx),
                            Currency.valueOf(rs.getInt(++idx)),
                            LocalDateTime.parse(rs.getString(++idx), sDateTimeFormatter),
                            rs.getString(++idx),
                            TransactionType.valueOf(rs.getInt(++idx)).toString());
                }
        );
    }

    private BankTransactionDaoImpl() {}

    private static BankTransactionDao sPrivateInstance = new BankTransactionDaoImpl();

    private static final DateTimeFormatter sDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String sAddBankTransaction =
            "insert into Transaction(from_account_id, to_account_id, amount, currency_id, creation_date, status_id, type_id) " +
                    "values (?, ?, ?, ?, ?, ?, ?) ";

    private static final String sGetBankTransaction =
            "select id, from_account_id, to_account_id, amount, currency_id, creation_date, status_id, type_id " +
                    "from Transaction  " +
                    "where from_account_id = ? " +
                    "order by creation_date desc";

}
