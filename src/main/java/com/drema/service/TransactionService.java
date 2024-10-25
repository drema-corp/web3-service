package com.drema.service;

import io.reactivex.disposables.Disposable;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class TransactionService {

    private ExecutorService executorService;
    private final Web3j web3j;
    private Disposable subscription;


    public TransactionService( @Value("${node.address}") String nodeAddress) {
        this.web3j = Web3j.build(new HttpService(nodeAddress));
    }

    @PostConstruct
    public void startService() {
        if (executorService == null || executorService.isShutdown()) {
            executorService = Executors.newSingleThreadExecutor();
        }
        executorService.submit(this::readTransaction);
    }

    public void readTransaction() {
        DefaultBlockParameter defaultBlockParameter = DefaultBlockParameter.valueOf(BigInteger.valueOf(0));
        try {
            subscription = web3j.replayPastAndFutureBlocksFlowable(defaultBlockParameter, true)
                    .subscribe(ethBlock -> {
                        ethBlock.getBlock().getTransactions().forEach(tx -> {
                            Transaction transaction = (Transaction) tx.get();
                        });
                    });
        } catch (Exception e) {
            log.error("Error when reading transaction: {}", e.getMessage());
        }
    }

    public void stopService() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
        executorService.shutdown();
    }

    public void pauseService(long waitTimeMillis) {
        stopService();
        try {
            Thread.sleep(waitTimeMillis);
        } catch (InterruptedException e) {
            log.error("Error when pausing service: {}", e.getMessage());
        }
        startService();
    }
}
