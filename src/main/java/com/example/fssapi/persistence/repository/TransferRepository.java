package com.example.fssapi.persistence.repository;

import com.example.fssapi.persistence.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, UUID> {

}
