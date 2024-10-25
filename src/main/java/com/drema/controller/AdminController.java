package com.drema.controller;

import com.drema.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final TransactionService transactionService;

    @GetMapping("/start")
    public void startService() {
        transactionService.startService();
    }

    @GetMapping("/stop")
    public void stopService() {
        transactionService.stopService();
    }

    @GetMapping("/pause")
    public void pauseService(@RequestParam long millis) {
        transactionService.pauseService(millis);
    }

}
