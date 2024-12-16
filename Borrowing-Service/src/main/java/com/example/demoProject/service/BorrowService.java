package com.example.demoProject.service;

import com.example.demoProject.model.Book;
import com.example.demoProject.model.Borrowing;
import com.example.demoProject.model.DatabaseSequence;
import com.example.demoProject.model.Users;
import com.example.demoProject.repository.BorrowingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Service
public class BorrowService {

    @Autowired
    BorrowingRepository borrowingRepository;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private MongoOperations mongoOperations;


    public int getSequenceNumber(String sequenceName) {

        Query query = new Query(Criteria.where("id").is(sequenceName));
        Update update = new Update().inc("seq", 1);
        DatabaseSequence counter = mongoOperations.findAndModify(query,
                update, options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return (int) (!Objects.isNull(counter) ? counter.getSeq() : 1);

    }
    public Borrowing createBorrowing(int userId, int bookId) {
        Users user = restTemplate.getForObject("http://localhost:8082/getUserById/" + userId, Users.class);
        Book book = restTemplate.getForObject("http://localhost:8083/getByBookId/" + bookId,  Book.class);
        if (user !=null &&book != null && book.getAvailabilityStatus() > 0) {
            Borrowing borrowing = new Borrowing();
            borrowing.setBorrowingId(getSequenceNumber(Borrowing.SEQUENCE_NAME));
            borrowing.setUserId(userId);
            borrowing.setBookId(bookId);
            LocalDate borrowingDate=LocalDate.now();
            borrowing.setBorrowDate(borrowingDate);
            borrowing.setStatus("BORROWED");
            LocalDate returningDate=LocalDate.now().plusDays(2);
            borrowing.setReturnDate(returningDate);
            borrowing = borrowingRepository.save(borrowing);
            book.setAvailabilityStatus(book.getAvailabilityStatus() - 1);
            restTemplate.exchange("http://localhost:8083/updateBooks/" + book.getBookId() + "/" + book.getAvailabilityStatus(), HttpMethod.PUT, null,
                    Book.class);
            return borrowing;
        } else {
            throw new RuntimeException("Book is not available or does not exist.");
        }
    }

    public Borrowing returnBorrowedBook(int borrowingId) {
        Borrowing borrowing = borrowingRepository.findByBorrowingId(borrowingId);
        borrowing.setStatus("RETURNED");
        borrowing.setReturnDate(LocalDate.now());
        borrowingRepository.save(borrowing);
        Book book = restTemplate.getForObject("http://localhost:8083/getByBookId/" +borrowing.getBookId(), Book.class);
        if (book != null) {
            book.setAvailabilityStatus(book.getAvailabilityStatus() +1);
            restTemplate.exchange("http://localhost:8083/updateBooks/" + book.getBookId() + "/" + book.getAvailabilityStatus(),
                    HttpMethod.PUT, null, Book.class);
        } else {
            throw new RuntimeException("Book not found with ID: " + borrowing.getBookId());
        }
        return borrowing;
    }


}
