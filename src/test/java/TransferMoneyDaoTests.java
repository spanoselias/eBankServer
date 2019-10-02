import samples.SampleObjects;
import dao.BankAccountDAO;
import dao.BankTransactionDao;
import dao.daoImpl.BankAccountDAOImpl;
import dao.daoImpl.BankTransactionDaoImpl;
import dto.FetchTransaction;
import dto.Transaction;
import dto.enums.TransactionType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.H2Ds;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class TransferMoneyDaoTests {

    @Before
    public void setUp() throws Exception {
        conn = H2Ds.getConnection();
    }

    @After
    public void afterEachTest() throws Exception {
     conn.rollback();
    }

    @Test
    public void shouldCreateTransactionAccountCorrectly() throws SQLException {

        final Transaction saTransaction =  SampleObjects.sampleAddTransaction;

        final long transactionId =
                bankTransactionDao.addBankTransaction(conn, saTransaction);

        final List<FetchTransaction> transactionsRetrieved =
                bankTransactionDao.getBankTransactionBySourceId(conn, 1)
                        .collect(Collectors.toList());

        final FetchTransaction firstTransaction = transactionsRetrieved.get(0);
        Assert.assertEquals(1, transactionsRetrieved.size());

        Assert.assertEquals(transactionId, firstTransaction.getTransactionId());
        Assert.assertEquals(saTransaction.getSourceBankAccountId(), firstTransaction.getSourceBankAccountId());
        Assert.assertEquals(saTransaction.getDestinationBankAccountId(), firstTransaction.getDestinationBankAccountId());
        Assert.assertEquals(saTransaction.getAmount(), firstTransaction.getAmount(), 0);
        Assert.assertEquals(TransactionType.valueOf(Integer.parseInt(saTransaction.getTrasactionType())).toString(), firstTransaction.getTrasactionType());
    }

    static Connection conn;

    private final static BankTransactionDao bankTransactionDao = BankTransactionDaoImpl.getInstance();
    private final static BankAccountDAO bankAccDAO = BankAccountDAOImpl.getInstance();
}
