package it.greenbank.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import it.greenbank.entities.Event;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EventRepository implements PanacheRepository<Event> {
    
}
