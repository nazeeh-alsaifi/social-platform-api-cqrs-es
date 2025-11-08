package com.example.simple_cqrs.command.repository;

import com.example.simple_cqrs.command.domain.EventStoreEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventStoreJpaRepository extends JpaRepository<EventStoreEntry, UUID> {


    List<EventStoreEntry> findByAggregateIdOrderByVersionAsc(UUID aggregateId);

}
