package com.books.api.controller.gui;

import com.books.api.domain.BookDto;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksTabController {

    @FXML
    public TableView<BookDto> booksTable;
    @FXML
    private TableColumn<BookDto, String> isbnColumn;
    @FXML
    private TableColumn<BookDto, String> titleColumn;
    @FXML
    private TableColumn<BookDto, String> subtitleColumn;
    @FXML
    private TableColumn<BookDto, Double> bookRatingColumn;
    @FXML
    private TableColumn<BookDto, List<String>> authorsColumn;
    @FXML
    private TableColumn<BookDto, List<String>> categoriesColumn;

    public void setTableCells() {
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        subtitleColumn.setCellValueFactory(new PropertyValueFactory<>("subtitle"));
        bookRatingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        authorsColumn.setCellValueFactory(new PropertyValueFactory<>("authors"));
        categoriesColumn.setCellValueFactory(new PropertyValueFactory<>("categories"));
    }

    public void setTableContent(List<BookDto> books) {
        booksTable.getItems().setAll(books);
    }

    public String getSelectedContent() {
        return booksTable.getSelectionModel().getSelectedItem() != null
                ? booksTable.getSelectionModel().getSelectedItem().getIsbn() : "";
    }

    public int getItemsSize() {
        return booksTable.getItems().size();
    }
}
