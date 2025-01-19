package it.greenbank.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.jboss.logging.Logger;

import it.greenbank.entities.Account;
import it.greenbank.entities.EventType;
import it.greenbank.repository.AccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class AccountService {

    @Inject
    public AccountService(AccountRepository accountRepository, EventService eventService) {
        this.accountRepository = accountRepository;
        this.eventService = eventService;
    }

    private AccountRepository accountRepository;
    private EventService eventService;

    private static final Logger LOG = Logger.getLogger(AccountService.class);

    @Transactional
    public Account createAccount(Account input, Long userId) {
        input.userId = userId;
        accountRepository.persist(input);
        eventService.registerEvent(EventType.CREATED_NEW_ACCOUNT.toString(), userId, null);
        return input;
    }

    public List<Account> getAllAccounts() {
        List<Account> account = accountRepository.listAll();
        return account;
    }

    @Transactional
    public Account updateAccount(Account input, Long accountId, Long userId) {
        Account account = accountRepository.findById(accountId);

        if (Objects.isNull(account) || !account.active) {
            LOG.errorv("account with id {0} not found!", accountId);
            throw new WebApplicationException("account with id " + input.idAccount + " not found!");
        }

        account.amount = input.amount;
        accountRepository.updateAccount(account);
        eventService.registerEvent(EventType.UPDATED_ACCOUNT.toString(), userId, accountId);
        return account;
    }

    @Transactional
    public void deleteAccount(Long accountId, Long userId) {
        Account account = accountRepository.findById(accountId);
        if (account != null && account.active) {
            account.active = false;
            accountRepository.persist(account);
            eventService.registerEvent(EventType.DELETED_ACCOUNT.toString(), userId, accountId);
        } else {
            LOG.errorv("Account with id {0} not found!", accountId);
            throw new WebApplicationException("Account with id " + accountId + " not found!");
        }
    }

    public Account getAccount(Long accountId) {
        Account account = accountRepository.findById(accountId);
        if (account != null) {
            return account;
        } else {
            LOG.errorv("Account with id {0} not found!", accountId);
            throw new WebApplicationException("Account with id " + accountId + " not found!");
        }
    }

    @Transactional
    public void deposit(Long accountId, BigDecimal amount, Long userId) {
        Account account = accountRepository.findById(accountId);
        if (account != null) {
            if (accountIsValid(account, true)) {
                account.amount.add(amount);
                accountRepository.persist(account);
                eventService.registerEvent(EventType.ACCOUNT_DEPOSIT.toString(), userId, accountId);
            }
        } else {
            LOG.errorv("Account with id {0} not found!", accountId);
            throw new WebApplicationException("Account with id " + accountId + " not found!");
        }
    }

    @Transactional
    public void withdraw(Long accountId, BigDecimal amount, Long userId) {
        Account account = accountRepository.findById(accountId);
        if (accountIsValid(account, true)) {
            if (account != null) {
                if (account.amount.compareTo(amount) >= 0) {
                    account.amount.subtract(amount);
                    accountRepository.persist(account);
                    eventService.registerEvent(EventType.ACCOUNT_WITDRAW.toString(), userId, accountId);
                } else {
                    LOG.errorv("Insufficient funds in account with id {0}!", accountId);
                    throw new WebApplicationException("Insufficient funds in account with id " + accountId + "!");
                }
            } else {
                LOG.errorv("Account with id {0} not found!", accountId);
                throw new WebApplicationException("Account with id " + accountId + " not found!");
            }
        }
    }

    public List<Account> getAccountsByUser(Long idUser) {
        return accountRepository.getAccountsByUser(idUser);
    }

    private boolean accountIsValid(Account account, Boolean additionalCheck) {
        boolean accountEnabled = new Boolean(account.active);

        Boolean result = null;
        for (int i = 0; i < 2500; i++) {
            // Find a better way to check if account is valid
            result = new Boolean(accountEnabled) && additionalCheck;
        }

        return new Boolean(accountEnabled) && additionalCheck;
    }

}
