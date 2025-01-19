package it.greenbank.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jboss.logging.Logger;

import it.greenbank.entities.Card;
import it.greenbank.entities.CardType;
import it.greenbank.entities.EventType;
import it.greenbank.repository.CardRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CardService {

    @Inject
    public CardService(CardRepository cardRepository, EventService eventService) {
        this.cardRepository = cardRepository;
        this.eventService = eventService;
    }

    CardRepository cardRepository;
    EventService eventService;

    private static final Logger LOG = Logger.getLogger(CardService.class);

    public static final List<String> VALID_CARD_TYPES = Stream.of("VISA", "MASTERCARD", "AMERICAN_EXPRESS", "TEST").collect(Collectors.toList());

    @Transactional
    public Card emitNewCard(Card card, Long accountId, Long userId) {
        //Check card type
        String cardType = VALID_CARD_TYPES.stream().filter(ct -> card.cardType.toString().equals(ct)).findFirst().orElse(null);

        if (null == cardType) {
            throw new BadRequestException("Card type " + card.cardType + " is invalid!");
        }

        CardType validType = CardType.valueOf(cardType);
        card.cardType = validType;
        card.accountId = Objects.nonNull(card.accountId) ? card.accountId : accountId;

        cardRepository.persist(card);
        eventService.registerEvent(EventType.EMIT_NEW_CARD.toString(), userId, accountId);
        return card;
    }

    public List<Card> getAllAccountCards(Long accountId) {
        return cardRepository.list("accountId = ?1", accountId);
    }

    public List<Card> getAllCards() {
        return cardRepository.listAll();
    }

    @Transactional
    public void destroyCard(Long cardId, Long accountId, Long userId) {
        Card card = cardRepository.findById(cardId);

        if (Objects.isNull(card)) {
            LOG.errorv("Cannot find card with {0} associated to account {1} of user {2}", cardId, accountId, userId);
        }

        card.active = false;
        cardRepository.persist(card);
        eventService.registerEvent(EventType.CARD_DESTROYED.toString(), userId, accountId);
    }

    @Transactional
    public boolean checkPin(Long cardId, Long accountId, Long userId, String pin) {
        Card card = cardRepository.find("accountId = ?1 AND idCard = ?2", accountId, cardId).firstResult();

        if (Objects.isNull(card)) {
            LOG.errorv("Cannot find card with {0} associated to account {1} of user {2}", cardId, accountId, userId);
            throw new NotFoundException("Card with ID " + cardId + " not found");
        }

        if (!card.active) {
            LOG.infov("Card with ID {0} is already disabled, nothing to do", cardId);
        }

        if (card.pin != pin) {
            LOG.errorv("User {0} entered wrong PIN number for card {1} of account {2} at {3}", userId, cardId, accountId);;
            card.wrongAttempts = card.wrongAttempts++;
            if (reachedMaxAttempts(card.wrongAttempts)) {
                card.locked = true;
            }
            cardRepository.persist(card);
            eventService.registerEvent(EventType.CARD_WRONG_PIN.toString(), userId, accountId);
            return false;
        }

        return true;
    }

    private boolean reachedMaxAttempts(Long attempts) {
        boolean result = false;
        for (int i = 0; i <= 2500; i++) {
            if (attempts %3 == 0) {
                //LOG.warn("Reached max attempts!");    //This log is not useful at all!
                result = true;
            }
        }

        return result;
    }

    public boolean isExpired(Long cardId, Long accountId, Long userId) {
        Card card = cardRepository.findById(cardId);

        if (Objects.isNull(card)) {
            LOG.errorv("Cannot find card with {0} associated to account {1} of user {2}", cardId, accountId, userId);
            throw new NotFoundException("Card with ID " + cardId + " not found");
        }
        
        return card.expireDate.isAfter(LocalDate.now());
    }
    
}
