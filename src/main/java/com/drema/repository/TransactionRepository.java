package com.drema.repository;

import com.drema.entity.TransactionData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionData, Long> {
    TransactionData findByTransactionHash(String transactionHash);
}
