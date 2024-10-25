package com.drema.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Entity
@Getter
@Setter
public class TransactionData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionHash;
    private String fromAddress;
    private String toAddress;
    private BigInteger transactionValue;
    private BigInteger blockNumber;
}
