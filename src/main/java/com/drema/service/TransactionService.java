package com.drema.service;

import com.drema.entity.LatestBlock;
import com.drema.entity.TransactionData;
import com.drema.repository.TransactionRepository;
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

    private final TransactionRepository repository;
    private final BlockService blockService;

    private ExecutorService executorService;
    private final Web3j web3j;
    private Disposable subscription;


    public TransactionService(TransactionRepository repository, BlockService blockService, @Value("${node.address}") String nodeAddress) {
        this.repository = repository;
        this.blockService = blockService;
        this.web3j = Web3j.build(new HttpService(nodeAddress));
    }

    @PostConstruct
    public void startService() {
        log.info("Starting transaction service");
        if (executorService == null || executorService.isShutdown()) {
            executorService = Executors.newSingleThreadExecutor();
        }
        executorService.submit(this::readTransaction);
    }

    public void readTransaction() {

        //Will be better use some additional storage for latest block: redis for example.
        DefaultBlockParameter defaultBlockParameter = getLatestBlock();
        try {
            subscription = web3j.replayPastAndFutureBlocksFlowable(defaultBlockParameter, true)
                    .subscribe(ethBlock -> {
                        ethBlock.getBlock().getTransactions().forEach(tx -> {
                            Transaction transaction = (Transaction) tx.get();
                            log.debug("Transaction: {}", transaction);

                            saveTransactionData(transaction);
                        });
                        blockService.save(ethBlock);

                    });
        } catch (Exception e) {
            log.error("Error when reading transaction: {}", e.getMessage());
        }
    }

    private void saveTransactionData(Transaction transaction) {
        TransactionData entity = new TransactionData();
        entity.setTransactionHash(transaction.getHash());
        entity.setFromAddress(transaction.getFrom());
        entity.setToAddress(transaction.getTo());
        entity.setTransactionValue(transaction.getValue());
        entity.setBlockNumber(transaction.getBlockNumber());

        repository.save(entity);
    }

    private DefaultBlockParameter getLatestBlock() {
        LatestBlock latestBlock = blockService.findLatestBlock();
        long blockNumber = 0;
        if (latestBlock != null) {
            blockNumber = latestBlock.getLatestBlockId();
        }

        return DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber));
    }

    public void stopService() {
        log.info("Stopping transaction service");
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
        executorService.shutdown();
    }

    public void pauseService(long waitTimeMillis) {
        log.info("Pausing transaction service");
        stopService();
        try {
            Thread.sleep(waitTimeMillis);
        } catch (InterruptedException e) {
            log.error("Error when pausing service: {}", e.getMessage());
        }
        startService();
    }

    public TransactionData findTransactionByHash(String hash) {
        return repository.findByTransactionHash(hash);
    }
}
