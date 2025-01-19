package it.greenbank.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

@Entity
@Table(name = "card_table")
public class Card {

    @Id
    @Column(name = "id_card", updatable = false)
    @SequenceGenerator(
            name = "card_generator",
            sequenceName = "card_table_seq",
            allocationSize = 1,
            initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_generator")
    @Null(groups = {Create.class})
    public Long idCard;

    @Column(name = "id_account", nullable = false, updatable = false)
    @Null(groups = {Create.class})
    public Long accountId;

    @Column(name = "card_number", nullable = false, updatable = false)
    @NotBlank(groups = {Create.class})
    @Size(min = 16, max = 16, groups = {Create.class})
    public String number;

    @Column(name = "expire_date", nullable = false, updatable = false)
    @NotNull(groups = {Create.class})
    public LocalDate expireDate;    //Date format is yyyy-MM-dd

    @Column(name = "card_type", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = {Create.class})
    public CardType cardType;

    @Column(name = "wrong_attempts", columnDefinition = "boolean default 0")
    @Min(value = 0, groups = {Create.class})
    @Max(value = 3, groups = {Create.class})
    public Long wrongAttempts = 0L;

    @Column(name = "card_pin")
    @NotBlank(groups = {Create.class})
    @Size(min = 4, max = 5, groups = {Create.class})
    public String pin;

    @Column(name = "locked", nullable = false, columnDefinition = "boolean default false")
    public Boolean locked = false;

    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    public Boolean active = true;

    @Column(name = "creation_date", updatable = false)
    @CreationTimestamp
    @Null(groups = {Create.class})
    public LocalDateTime creationDate;

    @Column(name = "last_update_date")
    @UpdateTimestamp
    @Null(groups = {Create.class})
    public LocalDateTime lastUpdateDate;

    public interface Create extends Default {}
    
}
