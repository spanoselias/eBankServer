import dao.BankAccountDAO;
import dao.BankTransactionDao;
import dao.daoImpl.BankAccountDAOImpl;
import dao.daoImpl.BankTransactionDaoImpl;
import dto.BankAccount;
import dto.FetchBankAccount;
import dto.FetchTransaction;
import dto.enums.Currency;
import dto.enums.TransactionType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import samples.SampleObjects;
import service.TransferMoneyService;
import utils.H2Ds;
import webservices.responses.TransactionReponse;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class TransferMoneyServiceTests {

    @Before
    public void setUp() throws Exception {
        conn = H2Ds.getConnection();
    }

    @After
    public void afterEachTest() throws Exception {
        conn.rollback();
    }

    @Test
    public void shouldTransferMoneyBetweenTwoAccounts() throws SQLException, ReflectiveOperationException {

        final BankAccount generatedFirstBankAcc = SampleObjects.sampleAddBankAccount;
        final BankAccount generatedSecondBankAcc =
                BankAccount.create("SecondName",
                        50,
                        Currency.USD);

        // Create new two accounts
        final long firstBankAccId =
                bankAccDAO.addBankAccount(conn, generatedFirstBankAcc);

        final long secondBankAccId =
                bankAccDAO.addBankAccount(conn, generatedSecondBankAcc);

        tranferMoneyService.transferMoney(
                conn,
                firstBankAccId,
                secondBankAccId,
                50);

        final FetchBankAccount fetchFirstBankAccount =
                bankAccDAO.getBankAccountById(conn, firstBankAccId).get();

        final FetchBankAccount fetchSecondBankAccount =
                bankAccDAO.getBankAccountById(conn, secondBankAccId).get();


        final double expectedFirstAccountNewBalance = 50;
        final double expectedSecondAccountNewBalance = 104.5;

        Assert.assertEquals(expectedFirstAccountNewBalance, fetchFirstBankAccount.getBalance(), 0);
        Assert.assertEquals(expectedSecondAccountNewBalance, fetchSecondBankAccount.getBalance(), 0);

    }

    @Test
    public void shouldCheckThatTransactionsAreGeneratedCorrectly() throws SQLException, ReflectiveOperationException {

        final BankAccount generatedFirstBankAcc = SampleObjects.sampleAddBankAccount;
        final BankAccount generatedSecondBankAcc =
                BankAccount.create("SecondName",
                        50,
                        Currency.USD);

        // Create new two accounts
        final long firstBankAccId =
                bankAccDAO.addBankAccount(conn, generatedFirstBankAcc);

        final long secondBankAccId =
                bankAccDAO.addBankAccount(conn, generatedSecondBankAcc);

        final TransactionReponse transactionReponse =
                tranferMoneyService.transferMoney(
                        conn,
                        firstBankAccId,
                        secondBankAccId,
                        50);

        // Retrieve all the transactions of the first account.
        final List<FetchTransaction> firstBankAccTransactionList =
                bankTransactionDao.getBankTransactionBySourceId(conn, firstBankAccId)
                        .collect(Collectors.toList());

        // Retrieve all the transactions of the second account.
        final List<FetchTransaction> secondBankAccTransactionList =
                bankTransactionDao.getBankTransactionBySourceId(conn, secondBankAccId)
                        .collect(Collectors.toList());

        final FetchBankAccount fetchSecondBankAccount =
                bankAccDAO.getBankAccountById(conn, secondBankAccId).get();

        final double expectedFirstAccTransferAmount = 50;
        final double expectedSecondAccountRetrievedAmount = 54.5;

        Assert.assertEquals(1, firstBankAccTransactionList.size());
        Assert.assertEquals(expectedFirstAccTransferAmount, firstBankAccTransactionList.get(0).getAmount(), 0);
        Assert.assertEquals(TransactionType.WITHDRAWAL.toString(), firstBankAccTransactionList.get(0).getTrasactionType().toString());

        Assert.assertEquals(1, secondBankAccTransactionList.size());
        Assert.assertEquals(expectedSecondAccountRetrievedAmount, secondBankAccTransactionList.get(0).getAmount(), 0);
        Assert.assertEquals(TransactionType.DEPOSIT.toString(), secondBankAccTransactionList.get(0).getTrasactionType().toString());

    }

    private static Connection conn;
    private final static BankAccountDAO bankAccDAO = BankAccountDAOImpl.getInstance();
    private final static BankTransactionDao bankTransactionDao = BankTransactionDaoImpl.getInstance();
    private final static TransferMoneyService tranferMoneyService = TransferMoneyService.create(bankTransactionDao, bankAccDAO);
}
