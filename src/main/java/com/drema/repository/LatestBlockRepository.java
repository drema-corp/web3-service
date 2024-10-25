package com.drema.repository;

import com.drema.entity.LatestBlock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LatestBlockRepository extends JpaRepository<LatestBlock, Long> {

}
