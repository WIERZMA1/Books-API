package com.books.api.controller.gui;


import com.books.api.controller.RestControllerService;
import com.books.api.domain.BookDto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
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
    private Stage stage;

    @FXML
    TextField isbnTextField = new TextField();
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
        stage = (Stage) isbnTextField.getScene().getWindow();
        stage.close();
    }

    public void showBookWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/book.fxml"));
            fxmlLoader.setControllerFactory(springContext::getBean);
            Parent root = fxmlLoader.load();
            stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/book.png")));
            stage.setScene(new Scene(root, primaryScene.getWidth(), primaryScene.getHeight()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateBookValues(BookDto book) {
        stage.setTitle(book.title);
        isbnTextField.setText(book.isbn);
        isbnTextField.setEditable(false);
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

    public BookDto updateBookValues() {
        long publishedDate = !publishedDateTextField.getText().equals("") ?
                Long.parseLong(publishedDateTextField.getText()) : 0L;
        int pageCount = !pageCountTextField.getText().equals("") ? Integer.parseInt(pageCountTextField.getText()) : 0;
        double rating = !ratingTextField.getText().equals("") ? Double.parseDouble(ratingTextField.getText()) : 0.0;

        return new BookDto(isbnTextField.getText(), titleTextField.getText(), subtitleTextField.getText(),
                publisherTextField.getText(), publishedDate, descriptionTextArea.getText(), pageCount,
                thumbnailTextField.getText(), languageTextField.getText(), previewLinkTextField.getText(),
                rating, retrieveSetFromString(authorsTextField.getText()),
                retrieveSetFromString(categoriesTextField.getText()));
    }

    private Set<String> retrieveSetFromString(String s) {
        return Arrays.stream(s.split(", ")).map(b -> b.replace("[", ""))
                .map(b -> b.replace("]", "")).collect(Collectors.toSet());
    }
}
