package com.petstore.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.petstore.dao.entities.TagEntity;

@Transactional
@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

}
