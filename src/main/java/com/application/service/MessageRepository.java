package com.application.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public interface MessageRepository extends JpaRepository<LogMessages, UUID> {

}
