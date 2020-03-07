package com.msastudy.coreapi.repo;


import com.msastudy.coreapi.summary.AccountRelSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRelSummaryRepository extends JpaRepository<AccountRelSummary, String> {

}
