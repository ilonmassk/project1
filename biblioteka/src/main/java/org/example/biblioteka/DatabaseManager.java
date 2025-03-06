package org.example.biblioteka;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {
    private final String HOST = "localhost";
    private final String PORT = "3306";
    private final String DB_NAME = "biblioteka";
    private final String LOGIN = "root";
    private final String PASS = "";

    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());

    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(connStr, LOGIN, PASS);
    }

    public boolean addBook(String author, String title, int publisherId, int year, int pages, int statusId) {
        String query = "INSERT INTO books (author, title, publisher_id, year, pages, status_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, author);
            stmt.setString(2, title);
            stmt.setInt(3, publisherId);
            stmt.setInt(4, year);
            stmt.setInt(5, pages);
            stmt.setInt(6, statusId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error adding book", e);
            return false;
        }
    }

    public boolean deleteBook(int inventoryNumber) {
        String query = "DELETE FROM books WHERE inventory_number = ?";
        try (Connection conn = getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, inventoryNumber);
            stmt.executeUpdate();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error deleting book with inventory number: " + inventoryNumber, e);
            return false;
        }
    }

    public List<String> searchBooksByAuthor(String author) {
        List<String> books = new ArrayList<>();
        String query = "SELECT b.inventory_number, b.author, b.title, b.year, s.status_name " +
                "FROM books b " +
                "JOIN statuses s ON b.status_id = s.id " +
                "WHERE b.author LIKE ?";
        try (Connection conn = getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + author + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("inventory_number");
                String bookAuthor = rs.getString("author");
                String title = rs.getString("title");
                int year = rs.getInt("year");
                String status = rs.getString("status_name");

                books.add("ID: " + id + " | " + bookAuthor + " - " + title + " (" + year + ") | Статус: " + status);
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error searching books by author: " + author, e);
        }
        return books;
    }
}
