package com.techgadget.dao;

import com.techgadget.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY username";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            users.add(mapRowToUser(results));
        }
        return users;
    }

    @Override
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            return mapRowToUser(results);
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        if (results.next()) {
            return mapRowToUser(results);
        }
        return null;
    }

    @Override
    public User createUser(User newUser) {
        String insertSql = "INSERT INTO users (username, password, role, street, city, zip_code, state) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING user_id";
        String passwordHash = new BCryptPasswordEncoder().encode(newUser.getPassword());

        Integer newUserId = jdbcTemplate.queryForObject(insertSql, Integer.class,
                newUser.getUsername(), passwordHash, newUser.getRole(),
                newUser.getStreet(), newUser.getCity(), newUser.getZipCode(), newUser.getState());

        return getUserById(newUserId);
    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setStreet(rs.getString("street"));
        user.setCity(rs.getString("city"));
        user.setZipCode(rs.getString("zip_code"));
        user.setState(rs.getString("state"));
        return user;
    }
}

