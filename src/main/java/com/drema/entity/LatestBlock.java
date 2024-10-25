package com.drema.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class LatestBlock {
    @Id
    private Long latestBlockId;
}
