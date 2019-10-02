package webservices;

import dto.FetchBankAccount;
import utils.H2Ds;
import webservices.responses.BankAccountReponse;
import webservices.responses.TransactionReponse;
import dao.BankAccountDAO;
import dao.BankTransactionDao;
import dao.daoImpl.BankAccountDAOImpl;
import dao.daoImpl.BankTransactionDaoImpl;
import dto.BankAccount;
import dto.FetchTransaction;
import service.BankAccountService;
import service.TransferMoneyService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Path("/bankaccount")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class Webservices {

    @GET
    @Path("{" + sGetBankAccountById + "}")
    public Response fetchBankAccountDetails(@PathParam(sGetBankAccountById) final Long id) throws IOException, SQLException {

        try (final Connection conn = H2Ds.getConnection()) {

            final BankAccount bankAccount = sBankAccountService.getBankAccountById(conn, id)
                    .orElseThrow(() -> new WebApplicationException(
                            String.format("The given id: %d does not belong to any bank account.", id), Status.NOT_FOUND));

            return Response.ok(bankAccount).build();

        } catch (final Throwable ex) {

            /*No need to rollback here since we do not perform any write operation.*/
            return Response.status(Status.NOT_FOUND)
                    .entity(ex.getMessage())
                    .build();
        }
    }

    @GET
    @Path("{" + sGetBankAccountById + "}" + sGetAllTransactionsById)
    public Response fetchAllTransactionByAccountId(@PathParam(sGetBankAccountById) final Long id) throws IOException, SQLException {

        try (final Connection conn = H2Ds.getConnection()) {

            sBankAccountService.getBankAccountById(conn, id)
                    .orElseThrow(() -> new WebApplicationException(
                            String.format("The given id: %d does not belong to any bank account.", id), Status.NOT_FOUND));

            final List<FetchTransaction> transactionList =
                    sTransactionAccountDao.getBankTransactionBySourceId(conn, id)
                            .collect(Collectors.toList());

            return Response.ok(transactionList).build();

        } catch (final Throwable ex) {

            /*No need to rollback here since we do not perform any write operation.*/
            return Response.status(Status.NOT_FOUND)
                    .entity(ex.getMessage())
                    .build();
        }
    }

    @POST
    @Path(sBankAccount)
    public Response addBankAccount(final BankAccount addBankAccount) throws SQLException, IOException, ClassNotFoundException, InterruptedException {

        try (final Connection conn = H2Ds.getConnection()) {
            final long bankAccountId = sBankAccountService.addBankAccount(conn, addBankAccount);

            conn.commit();

            return Response.ok(BankAccountReponse.create(bankAccountId)).build();

        } catch (final Throwable ex) {

            return Response.serverError()
                    .entity(ex.getMessage())
                    .build();
        }
    }

    @POST
    @Path(sBankAccountTransfer)
    public Response transferMoney(@QueryParam(sFromBankAccountId) final Long fromBankAccountId,
                                  @QueryParam(sToBankAccountId) Long toBankAccountId,
                                  @QueryParam(sMoneyToTranferInFromCurrency) final Double amountToTransfer) throws SQLException, IOException, ClassNotFoundException {

        // We use two try here in order to have the possibility
        // to rollback the transaction if something go wrong.
        try (final Connection conn = H2Ds.getConnection()) {

            // Check for the existence of from bank account
           final FetchBankAccount fromBankAccount =
                   bankAccountDao.getBankAccountById(conn, fromBankAccountId)
                    .orElseThrow(() -> new WebApplicationException(
                            String.format("The from account id: %d does not belong to any bank account.", fromBankAccountId), Status.NOT_FOUND));

            if (fromBankAccount.getBalance() < amountToTransfer) {
                throw new WebApplicationException(
                        String.format("Not enough money. The available balance is: %.2f", fromBankAccount.getBalance()), Status.BAD_REQUEST);
            }

            // Check for the existance of from account
            bankAccountDao.getBankAccountById(conn, toBankAccountId)
                    .orElseThrow(() -> new WebApplicationException(
                            String.format("The to bank account id: %d does not belong to any bank account.", toBankAccountId), Status.NOT_FOUND));

            final TransactionReponse transactionReponse =
                    tranferMoneyService.transferMoney(
                            conn,
                            fromBankAccountId,
                            toBankAccountId,
                            amountToTransfer);

            conn.commit();

            return Response.ok(transactionReponse).build();

        } catch (final Throwable ex) {

            return Response.serverError()
                    .entity(ex.getMessage()).build();
        }
    }

    private static final String sBankAccount = "/add";
    private static final String sBankAccountTransfer = "/transfer";
    private static final String sGetBankAccountById = "id";
    private static final String sGetAllTransactionsById = "/transactions";

    private final static String sFromBankAccountId = "fromId";
    private final static String sToBankAccountId = "toId";
    private final static String sMoneyToTranferInFromCurrency = "moneyToTranfer";

    private final static BankAccountService sBankAccountService =
            BankAccountService.create(BankAccountDAOImpl.getInstance());

    private final static BankAccountDAO bankAccountDao =
            BankAccountDAOImpl.getInstance();

    private final static BankTransactionDao sTransactionAccountDao =
            BankTransactionDaoImpl.getInstance();

    private final static TransferMoneyService tranferMoneyService =
            TransferMoneyService.create(sTransactionAccountDao, bankAccountDao);

}
