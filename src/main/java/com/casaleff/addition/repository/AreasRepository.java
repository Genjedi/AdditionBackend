package com.casaleff.addition.repository;

import com.casaleff.addition.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreasRepository extends JpaRepository<Area, Integer> {
}
