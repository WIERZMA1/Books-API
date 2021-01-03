package com.books.api.controller.gui;


import com.books.api.BooksMainApp;
import com.books.api.controller.RestControllerService;
import com.books.api.domain.BookDto;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.books.api.BooksMainApp.primaryScene;
import static com.books.api.BooksMainApp.springContext;

@Service
public class BookController {

    @Autowired
    private RestControllerService restController;

    @FXML
    Label isbnLabel = new Label();
    @FXML
    TextField titleTextField = new TextField();
    @FXML
    TextField subtitleTextField = new TextField();
    @FXML
    TextField publisherTextField = new TextField();
    @FXML
    TextField publishedDateTextField = new TextField();
    @FXML
    TextArea descriptionTextArea = new TextArea();
    @FXML
    TextField pageCountTextField = new TextField();
    @FXML
    TextField thumbnailTextField = new TextField();
    @FXML
    TextField languageTextField = new TextField();
    @FXML
    TextField previewLinkTextField = new TextField();
    @FXML
    TextField ratingTextField = new TextField();
    @FXML
    TextField authorsTextField = new TextField();
    @FXML
    TextField categoriesTextField = new TextField();

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) isbnLabel.getScene().getWindow();
        stage.close();
    }

    public void showBookWindow(BookDto book) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/book.fxml"));
            fxmlLoader.setControllerFactory(springContext::getBean);
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(book.title);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/book.png")));
/*            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);*/
            stage.setScene(new Scene(root, primaryScene.getWidth(), primaryScene.getHeight()));
            populateBookValues(book);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateBookValues(BookDto book) {
        isbnLabel.setText(book.isbn);
        titleTextField.setText(book.title);
        subtitleTextField.setText(book.subtitle);
        publisherTextField.setText(book.publisher);
        publishedDateTextField.setText(String.valueOf(book.publishedDate));
        descriptionTextArea.setText(book.description);
        pageCountTextField.setText(String.valueOf(book.pageCount));
        thumbnailTextField.setText(book.thumbnailUrl);
        languageTextField.setText(book.language);
        previewLinkTextField.setText(book.previewLink);
        ratingTextField.setText(String.valueOf(book.rating));
        authorsTextField.setText(book.authors.toString());
        categoriesTextField.setText(book.categories.toString());
    }

    @FXML
    private void handleUpdateBook() {
        restController.updateBook(updateBookValues());
    }

    private BookDto updateBookValues() {
        return new BookDto(isbnLabel.getText(), titleTextField.getText(), subtitleTextField.getText(),
                publisherTextField.getText(), Long.parseLong(publishedDateTextField.getText()),
                descriptionTextArea.getText(), Integer.parseInt(pageCountTextField.getText()),
                thumbnailTextField.getText(), languageTextField.getText(), previewLinkTextField.getText(),
                Double.parseDouble(ratingTextField.getText()),
                retrieveSetFromString(authorsTextField.getText()),
                retrieveSetFromString(categoriesTextField.getText()));
    }

    private Set<String> retrieveSetFromString(String s) {
        return Arrays.stream(s.split(", ")).map(b -> b.replace("[", ""))
                .map(b -> b.replace("]", "")).collect(Collectors.toSet());
    }
}
