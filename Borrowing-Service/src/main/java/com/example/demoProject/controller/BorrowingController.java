package com.example.demoProject.controller;

import com.example.demoProject.model.Borrowing;
import com.example.demoProject.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BorrowingController {

    @Autowired
    BorrowService borrowService;

    @PostMapping("/create/{userId}/{bookId}")
    public Borrowing createBorrowing(@PathVariable("userId") int userId, @PathVariable("bookId") int bookId) {

        return borrowService.createBorrowing(userId, bookId);
    }
    @PutMapping("/return/{borrowingId}")
    public Borrowing returnBorrowedBook(@PathVariable("borrowingId") int borrowingId) {
        return borrowService.returnBorrowedBook(borrowingId);
    }


}
