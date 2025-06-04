package com.jo2k.garagify.organisation.persistence.persistence.repository;

import com.jo2k.garagify.organisation.persistence.persistence.model.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganisationRepository extends JpaRepository<Organisation, Integer> {
}

