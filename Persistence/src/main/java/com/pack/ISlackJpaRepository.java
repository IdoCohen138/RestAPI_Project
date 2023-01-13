package com.pack;

import com.pack.exceptions.ChannelNotExitsInDataBaseException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Component("ISlackJpaRepository")

public interface ISlackJpaRepository extends JpaRepository<SlackChannel, UUID> {

    @Transactional
    @Modifying
    @Query("update SlackChannel sc set sc.status = :status where sc.id = :id")
    void updateChannel(@Param("id") UUID id, @Param("status") EnumStatus status) throws ChannelNotExitsInDataBaseException;

    List<SlackChannel> findAll(Specification<SlackChannel> spec);
}

