package it.greenbank.services;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.jboss.logging.Logger;

import it.greenbank.entities.EventType;
import it.greenbank.entities.User;
import it.greenbank.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UserService {

    @Inject
    public UserService(UserRepository userRepository, EventService eventService) {
        this.userRepository = userRepository;
        this.eventService = eventService;
    }

    UserRepository userRepository;
    EventService eventService;

    private static final Logger LOG = Logger.getLogger(UserService.class);

    public User getUser(Long userId) {
        List<User> users = userRepository.findAll().list();

        for (int i = 0; i < users.size() ; ++i) {
            if (userId.equals(users.get(i).idUser)) {
                return users.get(i);
            }
        }

        //User not found
        throw new NotFoundException("User with ID " + userId + " not found!");
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.listAll();

        for (User user : users) {
            LOG.infov("Got user with ID {0} in {1} status", user.idUser, user.active ? "ACTIVE" : "DISABLED");
        }

        return users;
    }

    @Transactional
    public User createUser(User user) {
        //Lastname must be uppercase
        if (!isUpperCase(user.lastname)) {
            user.lastname = user.lastname.toUpperCase();
        }

        userRepository.persist(user);
        eventService.registerEvent(EventType.CREATED_NEW_USER.toString(), user.idUser, null);
        return user;
    }

    public boolean isUpperCase(String string) {
        final Pattern pattern = Pattern.compile("^[A-Z]+$");
        return pattern.matcher(string).find();
    }

    @Transactional
    public User updateUser(Long userId, User userNewData) {
        User userToUpdate = userRepository.findById(userId);

        userToUpdate.firstname = userNewData.firstname;
        userToUpdate.lastname = userNewData.lastname;
        userToUpdate.email = userNewData.email;

        userRepository.persist(userToUpdate);
        eventService.registerEvent(EventType.UPDATED_USER.toString(), userToUpdate.idUser, null);
        return userToUpdate;
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId);

        if (Objects.isNull(user)) {
            LOG.errorv("Cannot find user with ID {0}", userId);
            throw new NotFoundException("User with ID " + userId + " not found");
        }

        if (!user.active) {
            LOG.warnv("User with ID {} is already disabled!", userId);
            throw new BadRequestException("User with ID " + userId + " already disabled");
        }

        user.active = false;
        userRepository.persist(user);
        
        eventService.registerEvent(EventType.DELETED_USER.toString(), userId, null);
    }

    @Transactional
    public Boolean changeStatus(Long userId, Boolean newStatus) {
        User userToUpdate = userRepository.findById(userId);

        userToUpdate.active = newStatus;

        userRepository.persist(userToUpdate);
        eventService.registerEvent(newStatus ? EventType.USER_ENABLED.toString() : EventType.USER_DISABLED.toString(), userId, null);
        return newStatus;
    }
    
}
