package com.casaleff.addition.repository;

import com.casaleff.addition.model.PaidItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaidItemRepository extends JpaRepository<PaidItem, Long> {
}
