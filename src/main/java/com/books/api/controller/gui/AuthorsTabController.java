package com.books.api.controller.gui;

import com.books.api.domain.AuthorDto;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AuthorsTabController {

    @FXML
    public TableView<AuthorDto> authorsTable;
    @FXML
    private TableColumn<AuthorDto, String> nameColumn;
    @FXML
    private TableColumn<AuthorDto, Double> authorRatingColumn;

    public void setTableCells() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<AuthorDto, String>("name"));
        authorRatingColumn.setCellValueFactory(new PropertyValueFactory<AuthorDto, Double>("rating"));
    }

    public void setTableContent(List<AuthorDto> authors) {
        authorsTable.getItems().setAll(authors);
    }
}
