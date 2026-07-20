package com.ecommerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.ContactMessage;
import com.ecommerce.repository.ContactRepository;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    // Save Customer Message
    public void saveMessage(ContactMessage message) {

        message.setStatus("OPEN");
        message.setCreatedAt(LocalDateTime.now());

        contactRepository.save(message);
    }

    // Get All Messages
    public List<ContactMessage> getAllMessages() {

        return contactRepository.findAll();
    }

    // Get Message By Id
    public ContactMessage getMessageById(Long id) {

        return contactRepository.findById(id).orElse(null);
    }

    // Search Messages
    public List<ContactMessage> searchMessages(String keyword) {

        return contactRepository
                .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrSubjectContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword);
    }

    // Mark as Resolved
    public void markResolved(Long id) {

        ContactMessage message = contactRepository.findById(id).orElse(null);

        if (message != null) {

            message.setStatus("RESOLVED");

            contactRepository.save(message);
        }
    }

    // Delete Message
    public void deleteMessage(Long id) {

        contactRepository.deleteById(id);
    }
    // Total Messages
public long getTotalMessages() {

    return contactRepository.count();
}

// Open Messages
public long getOpenMessages() {

    return contactRepository.findAll()
            .stream()
            .filter(m -> "OPEN".equals(m.getStatus()))
            .count();
}

// Resolved Messages
public long getResolvedMessages() {

    return contactRepository.findAll()
            .stream()
            .filter(m -> "RESOLVED".equals(m.getStatus()))
            .count();
}

}