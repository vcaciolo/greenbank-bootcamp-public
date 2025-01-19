package it.greenbank.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import it.greenbank.entities.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    
}
