package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.entity.ContactMessage;

@Repository
public interface ContactRepository extends JpaRepository<ContactMessage, Long> {

    List<ContactMessage> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrSubjectContainingIgnoreCase(
            String name,
            String email,
            String subject);

}