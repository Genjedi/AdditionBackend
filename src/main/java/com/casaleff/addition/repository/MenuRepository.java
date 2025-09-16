package com.casaleff.addition.repository;

import com.casaleff.addition.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    public Optional<Menu> findByName(String name);
}
