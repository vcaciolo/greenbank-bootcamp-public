package it.greenbank.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "event_table")
public class Event {

    @Id
    @Column(name = "id_event", updatable = false)
    @SequenceGenerator(
            name = "event_generator",
            sequenceName = "event_table_seq",
            allocationSize = 1,
            initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_generator")
    public Long idEvent;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user", nullable = false, updatable = false)
    public User user;

    @ManyToOne
    @JoinColumn(name = "id_account", nullable = true, updatable = false)
    public Account account;

    @Column(name = "event_type", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    public EventType eventType;

    @Column(name = "creation_date", updatable = false)
    @CreationTimestamp
    public LocalDateTime creationDate;

    @Column(name = "last_update_date")
    @UpdateTimestamp
    public LocalDateTime lastUpdateDate;
    
}
