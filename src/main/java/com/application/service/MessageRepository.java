package com.application.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
public interface MessageRepository extends JpaRepository<LogMessages, UUID> {

}
