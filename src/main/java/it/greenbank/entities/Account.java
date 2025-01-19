package it.greenbank.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Null;
import jakarta.validation.groups.Default;

@Entity
@Table(name = "account_table")
public class Account {

    @Id
    @Column(name = "id_account", nullable = false, updatable = false)
    @SequenceGenerator(
            name = "account_generator",
            sequenceName = "account_table_seq",
            allocationSize = 1,
            initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
    @Null(groups = {Create.class, Update.class})
    public Long idAccount;

    @Column(name = "amount", nullable = false, columnDefinition = "bigint default 0")
    public BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "id_user", nullable = false, updatable = false)
    @Null(groups = {Create.class, Update.class})
    public Long userId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountId", fetch = FetchType.EAGER)
    @Null(groups = {Create.class, Update.class})
    public Set<Card> cards;

    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    public Boolean active = true;

    @Column(name = "creation_date", updatable = false)
    @CreationTimestamp
    @Null(groups = {Create.class, Update.class})
    public LocalDateTime creationDate;

    @Column(name = "last_update_date")
    @UpdateTimestamp
    @Null(groups = {Create.class, Update.class})
    public LocalDateTime lastUpdateDate;

    public interface Create extends Default {}

    public interface Update extends Default {}
    
}
