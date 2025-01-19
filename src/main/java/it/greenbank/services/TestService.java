package it.greenbank.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.jboss.logging.Logger;

import io.quarkus.runtime.util.StringUtil;
import it.greenbank.entities.Account;
import it.greenbank.entities.Card;
import it.greenbank.entities.CardType;
import it.greenbank.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TestService {

    @Inject
    public TestService(UserService userService, AccountService accountService, CardService cardService) {
        this.userService = userService;
        this.accountService = accountService;
        this.cardService = cardService;
    }

    UserService userService;
    AccountService accountService;
    CardService cardService;

    private static final Logger LOG = Logger.getLogger(TestService.class);

    @Transactional
    public Long measurement(int howManyRuns) {
        Instant start = Instant.now();

        for (int i = 1; i <= howManyRuns; i++) {
            LOG.infov("Run [{0}] -- (1/14) --> Creating new user", i);
            User user = userService.createUser(getUser(i, null));

            LOG.infov("Run [{0}] -- (2/14) --> Changing user status to false", i);
            userService.changeStatus(user.idUser, false);
            LOG.infov("Run [{0}] -- (3/14) --> Changing user status to true", i);
            userService.changeStatus(user.idUser, true);
            LOG.infov("Run [{0}] -- (4/14) --> Updating user's email", i);
            userService.updateUser(user.idUser, getUser(i, "newEmail@email.it"));

            LOG.infov("Run [{0}] -- (5/14) --> Creating new account", i);
            Account account = accountService.createAccount(getAccount(i), user.idUser);

            LOG.infov("Run [{0}] -- (6/14) --> Deposit account", i);
            accountService.deposit(account.idAccount, BigDecimal.valueOf(100.00), user.idUser);
            LOG.infov("Run [{0}] -- (7/14) --> Withdraw account", i);
            accountService.withdraw(account.idAccount, BigDecimal.valueOf(40.00), user.idUser);
            LOG.infov("Run [{0}] -- (8/14) --> Updating account", i);
            accountService.updateAccount(getAccount(i), account.idAccount, user.idUser);

            LOG.infov("Run [{0}] -- (9/14) --> Creating new card", i);
            Card card = cardService.emitNewCard(getCard(user, account, i), account.idAccount, user.idUser);

            LOG.infov("Run [{0}] -- (10/14) --> Checking card PIN", i);
            cardService.checkPin(card.idCard, account.idAccount, user.idUser, "12345");
            LOG.infov("Run [{0}] -- (11/14) --> Checking card expiry date", i);
            cardService.isExpired(card.idCard, account.idAccount, user.idUser);

            LOG.infov("Run [{0}] -- (12/14) --> Destroying card", i);
            cardService.destroyCard(card.idCard, account.idAccount, user.idUser);
            LOG.infov("Run [{0}] -- (13/14) --> Deleting account", i);
            accountService.deleteAccount(account.idAccount, user.idUser);;
            LOG.infov("Run [{0}] -- (14/14) --> Deleting user", i);
            userService.deleteUser(user.idUser);
        }

        Instant finish = Instant.now();

        LOG.infov("Measurement service finished in {0} milliseconds ({1} seconds)", Duration.between(start, finish).toMillis(), Duration.between(start, finish).toSeconds());
        return Duration.between(start, finish).toMillis();
    }

    private User getUser(int i, String newEmail) {
        User user = new User();
        user.email = !StringUtil.isNullOrEmpty(newEmail) ? newEmail : "email" + i + "@email.it";
        user.username = "username" + i;
        user.lastname = "lastname" + i;
        user.firstname = "firstname" + i;
        return user;
    }

    private Account getAccount(int i) {
        Account account = new Account();
        account.amount = BigDecimal.valueOf(new Random().nextDouble(20000 - 1000 - 1) + 1000);
        return account;
    }

    private Card getCard(User user, Account account, int i) {
        Card Card = new Card();
        Card.accountId = account.idAccount;
        Card.cardType = CardType.MASTERCARD;
        Card.expireDate = LocalDate.now().plus(3, ChronoUnit.YEARS);
        Card.number = generateCardNumber();
        Card.locked = false;
        Card.pin = "12345";
        return Card;
    }

    private String generateCardNumber() {
        Random rand = new Random();
        StringBuilder builder = new StringBuilder();

        for (int i = 1; i <= 16; i++) {
            builder.append(rand.nextInt(10));
        }

        return builder.toString();
    }
    
}
