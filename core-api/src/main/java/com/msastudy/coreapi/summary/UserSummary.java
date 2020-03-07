package com.msastudy.coreapi.summary;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSummary {
    @Id
    private String userId;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String password;
    private String nickname;
    @Setter
    private boolean isActive = true;
    @Setter
    private boolean isVerified = false;
    private boolean isPrivate = false;
    private String emailVerificationCode;

    @OneToMany(fetch = FetchType.LAZY)
    private List<AccountRelSummary> accountsFollowed = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    private List<AccountRelSummary> accountsFollowing = new ArrayList<>();

    public void updateUsername(String username){
        if(!username.trim().equals("")){
            this.username = username;
        }
    }
    public void updateNickname(String nickname){
        if(!nickname.trim().equals("")){
            this.nickname = nickname;
        }
    }

}