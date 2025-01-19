package it.greenbank.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import it.greenbank.entities.Card;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CardRepository implements PanacheRepository<Card> {
    
}
