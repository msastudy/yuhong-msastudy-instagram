package com.msastudy.coreapi.repo;


import com.msastudy.coreapi.summary.UserSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSummaryRepository extends JpaRepository<UserSummary, String> {

}
