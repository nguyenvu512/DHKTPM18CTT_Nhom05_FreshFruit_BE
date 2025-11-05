package com.example.fruitshop_be.repository;

import com.example.fruitshop_be.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,String> {
    Account findByUsername(String username);
}
