package com.drema.controller;

import com.drema.dto.TransactionDataDto;
import com.drema.entity.TransactionData;
import com.drema.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/findByHash")
    public ResponseEntity getTransactionData(@RequestParam("hash") String transactionHash) {
        TransactionData transactionData = transactionService.findTransactionByHash(transactionHash);

        if (transactionData != null) {
            TransactionDataDto dataDto = new TransactionDataDto(transactionData);
            return ResponseEntity.ok(dataDto);
        }
        return ResponseEntity.badRequest().build();
    }

}
