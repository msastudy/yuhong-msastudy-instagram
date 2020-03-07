package com.msastudy.coreapi.summary;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountRelSummary {
    @Id
    private String relId;

    @ManyToOne(fetch= FetchType.LAZY)
    private UserSummary follower;

    @ManyToOne(fetch=FetchType.LAZY)
    private UserSummary followed;

    private LocalDateTime createdDt;
    private boolean approved = false;

}