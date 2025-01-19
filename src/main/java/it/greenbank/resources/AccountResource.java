package it.greenbank.resources;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;

import it.greenbank.entities.Account;
import it.greenbank.entities.Account.Create;
import it.greenbank.entities.Account.Update;
import it.greenbank.services.AccountService;

import java.math.BigDecimal;

@Path("/{userId}/accounts")
public class AccountResource {

    @Inject
    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    AccountService accountService;

    private static final Logger LOG = Logger.getLogger(AccountResource.class);

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Create account",
        description = "Create a new account"
    )
    public Account createAccount(@PathParam("userId") Long userId, @Valid @ConvertGroup(to = Create.class) Account input) {
        LOG.infov("Creating a new account for user {0}", userId);
        return accountService.createAccount(input, userId);
    }

    @PUT
    @Path("/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Update account",
        description = "Update an existing account"
    )
    public Account updateAccount(@PathParam("userId") Long userId, @PathParam("accountId") Long accountId, @Valid @ConvertGroup(to = Update.class) Account input) {
        LOG.infov("Updating account {0} of user {1}", accountId, userId);
        return accountService.updateAccount(input, accountId, userId);
    }

    @DELETE
    @Path("/{accountId}")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(
        summary = "Delete account",
        description = "Delete an existing account"
    )
    public void deleteAccount(@PathParam("userId") Long userId, @PathParam("accountId") Long accountId) {
        LOG.infov("Deleting account {0} of user {1}", accountId, userId);
        accountService.deleteAccount(accountId, userId);
    }

    @GET
    @Path("/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get account",
        description = "Get account by given ID"
    )
    public Account getAccount(@PathParam("userId") Long userId, @PathParam("accountId") Long accountId) {
        Account account = accountService.getAccount(accountId);
        LOG.infov("ID: {0}, AMOUNT: {1}", account.idAccount, account.amount);
        return account;
    }

    @GET
    @Path("/{accountId}/balance")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get account balance",
        description = "Return the current balance of the account with given ID"
    )
    public BigDecimal getBalance(@PathParam("userId") Long userId, @PathParam("accountId") Long accountId) {
        Account account = accountService.getAccount(accountId);
        LOG.infov("ID: {0}, AMOUNT: {1}", account.idAccount, account.amount);
        return account.amount;
    }

    @PUT
    @Path("/{accountId}/deposit/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Deposit",
        description = "Deposit money to the account with given ID"
    )
    public void deposit(@PathParam("userId") Long userId, @PathParam("accountId") Long accountId, @PathParam("amount") BigDecimal amount) {
        accountService.deposit(accountId, amount, userId);
        LOG.infov("ID: {0}, AMOUNT: {1}", accountId, amount);
    }
    @PUT
    @Path("/{accountId}/withdraw/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Withdraw",
        description = "Withdraw money from the account with given ID"
    )
    public void withdraw(@PathParam("userId") Long userId, @PathParam("accountId") Long accountId, @PathParam("amount") BigDecimal amount) {
        accountService.withdraw(accountId, amount, userId);
        LOG.infov("ID: {0}, AMOUNT: {1}", accountId, amount);
    }
}
