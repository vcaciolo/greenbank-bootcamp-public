package it.greenbank.services;

import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import it.greenbank.entities.Account;
import it.greenbank.entities.Card;
import it.greenbank.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ExportService {

    @ConfigProperty(name = "greenbank.export-to-file.path")
    String exportToFilePath;

    @Inject
    UserService userService;

    @Inject
    AccountService accountService;

    @Inject
    CardService cardService;

    private final static String USER_ID = "USER_ID";
    private final static String USERNAME = "USERNAME";
    private final static String FIRSTNAME = "FIRSTNAME";
    private final static String LASTNAME = "LASTNAME";
    private final static String EMAIL = "EMAIL";

    private final static String ACCOUNTS = "ACCOUNTS";

    private final static String ACCOUNT_ID = "ACCOUNT_ID";
    private final static String AMOUNT = "AMOUNT";

    private final static String CARDS = "CARDS";

    private final static String CARD_NUMBER = "CARD_NUMBER";
    private final static String CARD_ACTIVE = "CARD_ACTIVE";

    public void exportToFile() throws Exception {
        List<User> users = userService.getAllUsers();

        final Map<User, List<Account>> userToAccountsMap = users.stream()
                .collect(Collectors.toMap(Function.identity(), u -> accountService.getAccountsByUser(u.idUser)));

        FileWriter writer = new FileWriter(exportToFilePath);
        writer.append("");
        writer.flush();

        for (Map.Entry<User, List<Account>> entry : userToAccountsMap.entrySet()) {
            User user = entry.getKey();

            StringBuffer stringBuffer = new StringBuffer();
            
            String userRow = USER_ID + ": " + entry.getKey().idUser + ", " +
                    USERNAME + ": " + entry.getKey().username + ", " +
                    FIRSTNAME + ": " + entry.getKey().firstname + ", " +
                    LASTNAME + ": " + entry.getKey().lastname + ", " +
                    EMAIL + ": " + entry.getKey().email;

            stringBuffer.append(userRow);
            stringBuffer.append(" - ");
            stringBuffer.append(ACCOUNTS);

            stringBuffer.append(" [");
            
            for (Account account : entry.getValue()) {
                stringBuffer.append("{");
                
                String accountRow = ACCOUNT_ID + ": " + account.idAccount + ", " +
                    AMOUNT + ": " + account.amount;

                stringBuffer.append(accountRow);

                List<Card> cards = cardService.getAllAccountCards(account.idAccount);
                if (!cards.isEmpty()) {
                    stringBuffer.append(", ");

                    stringBuffer.append(CARDS);
                    stringBuffer.append(" [");
    
                    for (Card card : cards) {
                        stringBuffer.append("{");
    
                        String activeValue = null;
                        if (card.active && card.locked) {
                            activeValue = "LOCKED";
                        } else if (card.active && !card.locked) {
                            activeValue = "ACTIVE";
                        } else {
                            activeValue = "DISABLED";
                        }
    
                        String cardRow = CARD_NUMBER + ": " + card.number + ", " +
                            CARD_ACTIVE + ": " + activeValue;
    
                        stringBuffer.append(cardRow);
    
                        stringBuffer.append("}");
                    }

                    stringBuffer.append("]");
                }

                stringBuffer.append("}");
            }

            stringBuffer.append("]");

            stringBuffer.append(System.lineSeparator());

            if(!stringBuffer.isEmpty())
            writer.append(stringBuffer);
            writer.flush();
        }
    }

}
