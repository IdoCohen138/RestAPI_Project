package com.application.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<LogMessages, UUID> {

}
