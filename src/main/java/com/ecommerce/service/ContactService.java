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

    public void saveMessage(ContactMessage message) {
        message.setStatus("OPEN");
        message.setCreatedAt(LocalDateTime.now());
        contactRepository.save(message);
    }

    public List<ContactMessage> getAllMessages() {
        return contactRepository.findAll();
    }

    public ContactMessage getMessageById(Long id) {
        return contactRepository.findById(id).orElse(null);
    }

    public List<ContactMessage> searchMessages(String keyword) {
        return contactRepository
                .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrSubjectContainingIgnoreCase(
                        keyword, keyword, keyword);
    }

    public void markResolved(Long id) {

        ContactMessage message = contactRepository.findById(id).orElse(null);

        if (message != null) {
            message.setStatus("RESOLVED");
            contactRepository.save(message);
        }
    }

    public void deleteMessage(Long id) {
        contactRepository.deleteById(id);
    }

    public long getTotalMessages() {
        return contactRepository.count();
    }

    public long getOpenMessages() {
        return contactRepository.findAll()
                .stream()
                .filter(m -> "OPEN".equals(m.getStatus()))
                .count();
    }

    public long getResolvedMessages() {
        return contactRepository.findAll()
                .stream()
                .filter(m -> "RESOLVED".equals(m.getStatus()))
                .count();
    }

    // New Method
    public List<ContactMessage> getMyComplaints(String email) {
        return contactRepository.findByEmail(email);
    }
}