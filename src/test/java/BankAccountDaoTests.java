import samples.SampleObjects;
import dao.BankAccountDAO;
import dao.daoImpl.BankAccountDAOImpl;
import dto.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.H2Ds;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class BankAccountDaoTests {

    private static DataSource ds;

    @Before
    public void setUp() throws Exception {

        conn = H2Ds.getConnection();

        Assert.assertNotNull(conn);
    }

    @After
    public void afterEachTest() throws Exception {
        conn.rollback();
    }

    @Test
    public void shouldCreateBankAccountCorrectly() throws SQLException {

        final BankAccount saBankAcc = SampleObjects.sampleAddBankAccount;

        final long accountId = bankAccDAO.addBankAccount(conn, saBankAcc);

        final FetchBankAccount bankAccountRetrieved = bankAccDAO.getBankAccountById(conn, accountId).orElseThrow(RuntimeException::new);

        Assert.assertEquals(accountId, bankAccountRetrieved.getAccountId());
        Assert.assertEquals(bankAccountRetrieved.getCurrency(), saBankAcc.getCurrency());
        Assert.assertEquals(bankAccountRetrieved.getOwnerName(), saBankAcc.getOwnerName());
        Assert.assertEquals(bankAccountRetrieved.getBalance(), saBankAcc.getBalance());
    }

    @Test
    public void shouldCreateAndUpdateBankAccountCorrectly() throws SQLException {

        final BankAccount saBankAcc = SampleObjects.sampleAddBankAccount;

        final long accountId = bankAccDAO.addBankAccount(conn, saBankAcc);

        final FetchBankAccount bankAccountBeforeUpdate = bankAccDAO.getBankAccountById(conn, accountId).orElseThrow(RuntimeException::new);
        Assert.assertEquals(accountId, bankAccountBeforeUpdate.getAccountId());

        final int rowUpdated = bankAccDAO.updateBankAccountById(conn, accountId, 5.0);

        final BankAccount bankAccountAfterUpdate = bankAccDAO.getBankAccountById(conn, accountId).orElseThrow(RuntimeException::new);

        Assert.assertEquals(1, rowUpdated);
        Assert.assertEquals(5.0, bankAccountAfterUpdate.getBalance(), 0);
    }

    private static Connection conn;
    private final static BankAccountDAO bankAccDAO = BankAccountDAOImpl.getInstance();
}
