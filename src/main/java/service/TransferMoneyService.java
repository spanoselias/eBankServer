package service;

import webservices.responses.TransactionReponse;
import dao.BankAccountDAO;
import dao.BankTransactionDao;
import dto.Transaction;
import dto.BankAccount;
import dto.enums.TransactionType;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TransferMoneyService {

    public static TransferMoneyService create(final BankTransactionDao bankTransactionDao,
                                              final BankAccountDAO bankAccountDAO) {
        return new TransferMoneyService(bankTransactionDao, bankAccountDAO);
    }

    public TransactionReponse transferMoney(final Connection conn,
                                            final long fromBankAccountId,
                                            final long toBankAccountId,
                                            final double amountToTransfer) throws SQLException {

        // We lock the specific bank account since it should be thread-safe.
        // We use row-level lock granularity using select for update.
        final BankAccount fromBankAccount =
                bankAccountDAO.getAndLockBankAccountById(
                        conn,
                        fromBankAccountId).get();

        final BankAccount toBankAccount =
                bankAccountDAO.getAndLockBankAccountById(
                        conn,
                        toBankAccountId).get();

        final double sourceNewBalance = fromBankAccount.getBalance() - amountToTransfer;

        final double amountToTranferToBankAccountCurrency =
                ConversionRatesService.getConversationRate(fromBankAccount.getCurrency(), toBankAccount.getCurrency()) * amountToTransfer;

        final double destinationNewBalance = toBankAccount.getBalance() + amountToTranferToBankAccountCurrency;

        // Update the from bank account with the new balance.
        bankAccountDAO.updateBankAccountById(conn, fromBankAccountId, sourceNewBalance);

        // Update the to bank account with the new balance.
        bankAccountDAO.updateBankAccountById(conn, toBankAccountId, destinationNewBalance);

        final long fromTransactionId =
                bankTransactionDao.addBankTransaction(
                        conn,
                        Transaction.create(
                                fromBankAccountId,
                                toBankAccountId,
                                amountToTransfer,
                                fromBankAccount.getCurrency(),
                                LocalDateTime.now(),
                                "Approved",
                                String.valueOf(TransactionType.WITHDRAWAL.getId())));

        final long toTransactionId =
                bankTransactionDao.addBankTransaction(
                        conn,
                        Transaction.create(
                                toBankAccountId,
                                fromBankAccountId,
                                amountToTranferToBankAccountCurrency,
                                toBankAccount.getCurrency(),
                                LocalDateTime.now(),
                                "Approved",
                                String.valueOf(TransactionType.DEPOSIT.getId())));

        return TransactionReponse.create(fromTransactionId, toTransactionId);
    }

    private TransferMoneyService(final BankTransactionDao bankTransactionDao,
                                 final BankAccountDAO bankAccountDAO) {
        this.bankTransactionDao = bankTransactionDao;
        this.bankAccountDAO = bankAccountDAO;
    }

    private final BankTransactionDao bankTransactionDao;
    private final BankAccountDAO bankAccountDAO;
}
