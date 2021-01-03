package com.books.api.controller.gui;

import com.books.api.domain.AuthorDto;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorsTabController {

    @FXML
    public TableView<AuthorDto> authorsTable;
    @FXML
    private TableColumn<AuthorDto, String> nameColumn;
    @FXML
    private TableColumn<AuthorDto, Double> authorRatingColumn;

    public void setTableCells() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorRatingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
    }

    public void setTableContent(List<AuthorDto> authors) {
        authorsTable.getItems().setAll(authors);
    }

    public String getSelectedContent() {
        return authorsTable.getSelectionModel().getSelectedItem() != null
                ? authorsTable.getSelectionModel().getSelectedItem().getName() : "";
    }

    public int getItemsSize() {
        return authorsTable.getItems().size();
    }
}
