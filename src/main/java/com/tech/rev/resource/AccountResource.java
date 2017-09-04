package com.tech.rev.resource;

import com.tech.rev.dao.AccountDAO;
import com.tech.rev.model.Account;
import com.tech.rev.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {
    Logger logger = LoggerFactory.getLogger(AccountResource.class);

    private AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @GET
    public Response getAccountList() {
        logger.debug(String.format("Listing all accounts..."));

        return Response.ok(accountService.listAccounts()).build();
    }

    @POST
    public Response createAccount(Account account) {
        logger.debug(String.format("Creating account for %s", account.getAccountHolder()));

        return Response
                .status(Response.Status.CREATED)
                .entity(accountService.createAccount(account))
                .build();
    }

    @GET
    @Path("/{account_no}")
    public Response getAccountByAccountNo(@PathParam("account_no") String accountNo) {
        logger.debug(String.format("Getting account by account number: %s", accountNo));

        return Response
                .ok(accountService.fetchByAccountNumber(accountNo))
                .build();
    }

    @PUT
    @Path("/{account_no}")
    public Response updateAccountByAccountNo(
            @PathParam("account_no") String accountNo,
            Account accountUpdates) {
        logger.debug(String.format("Updating account with account holder name: ∞s", accountUpdates.getAccountHolder()));

        Account updatedAccount = accountService.updateAccountByAccountNo(accountNo, accountUpdates);

        return Response
                .status(Response.Status.CREATED)
                .entity(updatedAccount)
                .build();
    }
}
