package com.drema.dto;

import com.drema.entity.TransactionData;
import lombok.Getter;

import java.math.BigInteger;

@Getter
public class TransactionDataDto {
    private String transactionHash;
    private String fromAddress;
    private String toAddress;
    private BigInteger transactionValue;
    private BigInteger blockNumber;

    public TransactionDataDto(TransactionData transactionData) {
        this.transactionHash = transactionData.getTransactionHash();
        this.fromAddress = transactionData.getFromAddress();
        this.toAddress = transactionData.getToAddress();
        this.transactionValue = transactionData.getTransactionValue();
        this.blockNumber = transactionData.getBlockNumber();
    }

}
