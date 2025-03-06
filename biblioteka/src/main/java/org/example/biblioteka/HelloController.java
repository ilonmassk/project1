package org.example.biblioteka;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class HelloController {
    private final DatabaseManager db = new DatabaseManager();

    @FXML private TextField authorField, titleField, publisherField, yearField, pagesField, statusField;
    @FXML private TextField searchField;
    @FXML private ListView<String> booksList;
    @FXML private TextField returnYearField, overdueDaysField;
    @FXML private Label penaltyLabel;
    @FXML private TextField deleteBookField;

    // Вспомогательный метод для очистки полей
    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    // Метод для добавления книги
    @FXML
    private void addBook() {
        try {
            String author = authorField.getText();
            String title = titleField.getText();
            int publisherId = Integer.parseInt(publisherField.getText());
            int year = Integer.parseInt(yearField.getText());
            int pages = Integer.parseInt(pagesField.getText());
            int statusId = Integer.parseInt(statusField.getText());

            db.addBook(author, title, publisherId, year, pages, statusId);
            clearFields(authorField, titleField, publisherField, yearField, pagesField, statusField);
            booksList.getItems().add(title + " добавлена!");
        } catch (NumberFormatException e) {
            booksList.getItems().add("Ошибка ввода данных! Проверьте правильность введенных значений.");
        }
    }

    // Метод для удаления книги
    @FXML
    private void deleteBook() {
        try {
            int inventoryNumber = Integer.parseInt(deleteBookField.getText());
            db.deleteBook(inventoryNumber);
            booksList.getItems().add("Книга с номером " + inventoryNumber + " удалена!");
            deleteBookField.clear();
        } catch (NumberFormatException e) {
            booksList.getItems().add("Ошибка: введите корректный номер книги!");
        }
    }

    // Метод для поиска книг по автору
    @FXML
    private void searchBooks() {
        booksList.getItems().clear();
        String author = searchField.getText();
        if (author.isEmpty()) {
            booksList.getItems().add("Введите имя автора для поиска.");
            return;
        }
        List<String> books = db.searchBooksByAuthor(author);
        if (books.isEmpty()) {
            booksList.getItems().add("Книги по автору " + author + " не найдены.");
        } else {
            booksList.getItems().addAll(books);
        }
    }

    // Метод для расчета штрафа
    @FXML
    private void calculatePenalty() {
        try {
            int year = Integer.parseInt(returnYearField.getText());
            int overdueDays = Integer.parseInt(overdueDaysField.getText());
            int penalty = PenaltyCalculator.calculatePenalty(year, overdueDays);
            penaltyLabel.setText("Штраф: " + penalty + " руб.");
        } catch (NumberFormatException e) {
            penaltyLabel.setText("Ошибка ввода данных! Проверьте правильность введенных значений.");
        }
    }
}
