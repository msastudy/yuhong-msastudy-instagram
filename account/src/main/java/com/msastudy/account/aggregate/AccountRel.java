package com.msastudy.account.aggregate;

import org.axonframework.modelling.command.EntityId;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class AccountRel {
    @EntityId
    @Id
    private String relId;

    @ManyToOne(fetch= FetchType.LAZY)
    private AccountAggregate follower;

    @ManyToOne(fetch=FetchType.LAZY)
    private AccountAggregate followed;

    private LocalDateTime createdDt;
    private boolean approved = false;

}
