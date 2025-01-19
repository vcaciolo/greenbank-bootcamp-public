package it.greenbank.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.groups.Default;

@Entity
@Table(name = "user_table")
public class User {

    @Id
    @Column(name = "id_user", updatable = false)
    @SequenceGenerator(
            name = "user_generator",
            sequenceName = "user_table_seq",
            allocationSize = 1,
            initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @Null(groups = {Create.class, Update.class})
    public Long idUser;

    @Column(name = "username")
    @NotBlank(groups = {Create.class})
    @Null(groups = {Update.class})
    public String username;

    @Column(name = "email")
    @NotBlank(groups = {Create.class, Update.class})
    public String email;

    @Column(name = "firstname")
    @NotBlank(groups = {Create.class, Update.class})
    public String firstname;

    @Column(name = "lastname")
    @NotBlank(groups = {Create.class, Update.class})
    public String lastname;

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
