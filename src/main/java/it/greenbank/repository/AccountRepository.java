package it.greenbank.repository;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import it.greenbank.entities.Account;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {

    public Account updateAccount(Account input) {
        update("amount = ?1 where id = ?2", input.amount, input.idAccount);
        return findById(input.idAccount);
    }

    public List<Account> getAccountsByUser(Long idUser) {
        return find("userId = ?1", idUser).list();
    }
    
}
