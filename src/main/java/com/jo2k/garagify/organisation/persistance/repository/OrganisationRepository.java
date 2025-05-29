package com.jo2k.garagify.organisation.persistance.repository;

import com.jo2k.garagify.organisation.persistance.model.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganisationRepository extends JpaRepository<Organisation, Integer> {
}

