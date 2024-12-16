package com.example.demoProject.repository;

import com.example.demoProject.model.Borrowing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowingRepository extends MongoRepository<Borrowing ,Integer> {
 public Borrowing findByBorrowingId(int id);
}
