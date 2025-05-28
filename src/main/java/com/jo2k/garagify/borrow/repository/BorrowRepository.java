package com.jo2k.garagify.borrow.repository;

import com.jo2k.garagify.borrow.model.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface BorrowRepository extends JpaRepository<Borrow, UUID>, JpaSpecificationExecutor<Borrow> {
    // Additional query methods can be defined here if needed
}
