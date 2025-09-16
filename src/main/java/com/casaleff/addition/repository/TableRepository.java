package com.casaleff.addition.repository;

import com.casaleff.addition.model.MyTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<MyTable, Integer> {
    public Optional<MyTable> findByNumber(String number);
}
