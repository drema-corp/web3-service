package com.drema.service;

import com.drema.entity.LatestBlock;
import com.drema.repository.LatestBlockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlockService {
    private final LatestBlockRepository blockRepository;

    public LatestBlock findLatestBlock() {
        List<LatestBlock> allBlocks = blockRepository.findAll();

        if (!allBlocks.isEmpty()) {
            return allBlocks.get(0);
        }

        return null;
    }

    public void save(EthBlock ethBlock) {
        LatestBlock latestBlock = new LatestBlock();
        latestBlock.setLatestBlockId(ethBlock.getId());
        blockRepository.save(latestBlock);

        log.debug("Saved block: {}", ethBlock.getId());
    }
}
