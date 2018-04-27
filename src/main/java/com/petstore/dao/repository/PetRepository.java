package com.petstore.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.petstore.dao.entities.PetEntity;

@Transactional
@Repository
public interface PetRepository extends JpaRepository<PetEntity, Long> {

}
