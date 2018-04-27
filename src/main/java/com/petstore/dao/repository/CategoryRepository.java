package com.petstore.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.petstore.dao.entities.CategoryEntity;

@Transactional
@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
