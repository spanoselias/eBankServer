package dao.daoImpl;

import dao.BankAccountDAO;
import dto.BankAccount;
import dto.enums.Currency;
import dto.FetchBankAccount;
import utils.DBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class BankAccountDAOImpl implements BankAccountDAO {

    public static  BankAccountDAOImpl getInstance() {
        return sPrivateInstance;
    }

    @Override
    public long addBankAccount(final Connection conn, final BankAccount addBankAccount) throws SQLException {

        return DBUtils.writeToDB(
                conn,
                sAddBankAccount,
                pstmt -> {

                    int idx = 0;
                    pstmt.setString(++idx, addBankAccount.getOwnerName());
                    pstmt.setDouble(++idx, addBankAccount.getBalance());
                    pstmt.setInt(++idx, addBankAccount.getCurrency().getId());
                }
        );
    }

    @Override
    public Optional<FetchBankAccount> getBankAccountById(final Connection connection,
                                                    final long accountId) throws SQLException {
        return DBUtils.selectQuery(
                connection,
                sGetBankAccountById,
                pstmt -> pstmt.setLong(1, accountId),
                BankAccountDAOImpl::getBankAccountFromRs
        ).findFirst();
    }

    @Override
    public Optional<FetchBankAccount> getAndLockBankAccountById(final Connection connection,
                                                                final long accountId) throws SQLException {
        return DBUtils.selectQuery(
                connection,
                sGetAndLockBankAccountById,
                pstmt -> pstmt.setLong(1, accountId),
                BankAccountDAOImpl::getBankAccountFromRs
        ).findFirst();
    }

    @Override
    public int updateBankAccountById(Connection connection, final long accountId, final double newAmount) throws SQLException {

        return DBUtils.executeQuery(
                connection,
                sUpdateBankAccount,
                pstmt ->
                {
                    int idx = 0;
                    pstmt.setDouble(++idx, newAmount);
                    pstmt.setLong(++idx, accountId);
                });
    }

    public static FetchBankAccount getBankAccountFromRs(final ResultSet rs) throws SQLException {
        int idx = 0;

        return FetchBankAccount.create(
                rs.getLong(++idx),
                rs.getString(++idx),
                rs.getDouble(++idx),
                Currency.valueOf(rs.getInt(++idx)));
    }

    private BankAccountDAOImpl() {}

    private static BankAccountDAOImpl sPrivateInstance = new BankAccountDAOImpl();

    private static final String sAddBankAccount =
            "INSERT INTO bank_account(owner_name, balance, currency_id) " +
                    "VALUES (?, ?, ?) ";

    private static final String sGetBankAccountById =
            "SELECT ba.id, ba.owner_name, ba.balance, ba.currency_id " +
                    "FROM bank_account ba " +
                    "where ba.id = ?";

    private static final String sGetAndLockBankAccountById =
            "SELECT ba.id, ba.owner_name, ba.balance, ba.currency_id " +
                    "FROM bank_account ba " +
                    "where ba.id = ?" +
                    "for update of ba.id";

    private static final String sUpdateBankAccount =
                    "update bank_account " +
                    "set balance = ? " +
                    "where id = ?";

}
