package com.example.demoProject.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "borrowing")
public class Borrowing {


    @Id
    private int borrowingId;
    private int userId;
    private int bookId;
    private String status;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";


    public Borrowing() {

    }

    public Borrowing(int borrowingId, int userId, int bookId, String status, LocalDate borrowDate, LocalDate returnDate) {
        this.borrowingId = borrowingId;
        this.userId = userId;
        this.bookId = bookId;
        this.status = status;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public int getBorrowingId() {
        return borrowingId;
    }

    public void setBorrowingId(int borrowingId) {
        this.borrowingId = borrowingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    public static String getSequenceName() {
        return SEQUENCE_NAME;
    }
}
