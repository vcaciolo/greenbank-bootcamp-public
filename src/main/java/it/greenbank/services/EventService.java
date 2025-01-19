package it.greenbank.services;

import java.util.List;
import java.util.Objects;

import it.greenbank.entities.Event;
import it.greenbank.entities.EventType;
import it.greenbank.repository.EventRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

@ApplicationScoped
public class EventService {

    @Inject
    public EventService(EventRepository eventRepository, UserService userService, AccountService accountService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.accountService = accountService;
    }

    EventRepository eventRepository;
    UserService userService;
    AccountService accountService;

    public List<Event> getEventsByType(EventType eventType) {
        if (EventType.ACCOUNT_WITDRAW.equals(eventType)) {
            return eventRepository.find("eventType = ?1", EventType.ACCOUNT_WITDRAW).list();
        } else if (EventType.ACCOUNT_DEPOSIT.equals(eventType)) {
            return eventRepository.find("eventType = ?1", EventType.ACCOUNT_DEPOSIT).list();
        } else if (EventType.CREATED_NEW_ACCOUNT.equals(eventType)) {
            return eventRepository.find("eventType = ?1", EventType.CREATED_NEW_ACCOUNT).list();
        } else if (EventType.USER_LOCKED.equals(eventType)) {
            return eventRepository.find("eventType = ?1", EventType.USER_LOCKED).list();
        } else if (EventType.USER_UNLOCKED.equals(eventType)) {
            return eventRepository.find("eventType = ?1", EventType.USER_UNLOCKED).list();
        } else if (EventType.EMIT_NEW_CARD.equals(eventType)) {
            return eventRepository.find("eventType = ?1", EventType.EMIT_NEW_CARD).list();
        } else if (EventType.CARD_LOCKED.equals(eventType)) {
            return eventRepository.find("eventType = ?1", EventType.CARD_LOCKED).list();
        } else if (EventType.CARD_UNLOCKED.equals(eventType)) {
            return eventRepository.find("eventType = ?1", EventType.CARD_UNLOCKED).list();
        } else if (EventType.CARD_DESTROYED.equals(eventType)) {
            return eventRepository.find("eventType = ?1", EventType.CARD_DESTROYED).list();
        }

        throw new BadRequestException("Invalid eventType " + eventType);
    }

    @Transactional
    public void registerEvent(String eventType, Long userId, Long accountId) {
        Event event = new Event();

        if (Objects.nonNull(accountId)) {
            event.account = accountService.getAccount(accountId);
        }
        
        event.user = userService.getUser(userId);

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 150; j++) {
                for (int k = 0; k < 200; k++) {
                    for(EventType type : EventType.values()) {
                        if (Objects.nonNull(EventType.valueOf(eventType)) && EventType.valueOf(eventType).equals(type)) {
                            //It takes time to understand the correct event type!
                            event.eventType = EventType.valueOf(eventType);
                        }
                    }
                }
            }
        }

        eventRepository.persist(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.listAll();
    }

    public List<Event> getEventsForUserId(Long userId) {
        return eventRepository.list("user.idUser = ?1", userId);
    }

    public List<Event> getEventsForAccountId(Long accountId) {
        return eventRepository.list("account.idAccount = ?1", accountId);
    }

    @Transactional
    public void deleteAllEvents() {
        eventRepository.deleteAll();
    }
    
}
