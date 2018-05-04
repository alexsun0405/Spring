package com.test.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TestDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void reduceNumber(long bookId) {
        String sql = "update book set number = number - 1 where number > 0 and book_id = " + bookId;
        jdbcTemplate.update(sql);
    }

    public void addNumber(long bookId) {
        String sql = "update book set number = number + 1 where book_id = " + bookId;
        jdbcTemplate.update(sql);
    }
}
