package com.pack;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Component("messageRepository")
public interface MessageRepository extends JpaRepository<LogMessages, UUID> {

}
