package com.books.api.controller.gui;

import com.books.api.controller.BookNotFoundException;
import com.books.api.controller.RestControllerService;
import com.books.api.domain.AuthorDto;
import com.books.api.domain.BookDto;
import com.books.api.service.JSONService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GuiController implements Initializable {

    private static final String USER_DIR = "UserDir";
    private final Preferences pref = Preferences.userNodeForPackage(getClass());
    private File dir = new File(pref.get(USER_DIR, System.getProperty("user.dir")));

    @Autowired
    private BooksTabController booksTabController;
    @Autowired
    private AuthorsTabController authorsTabController;
    @Autowired
    private BookController bookController;
    @Autowired
    private RestControllerService restController;
    @Autowired
    private JSONService jsonService;

    private boolean isActiveTabBooks;
    private boolean isBookSelected;
    private boolean isAuthorSelected;

    @FXML
    private TextField name;
    @FXML
    private Label total;
    @FXML
    private Button openBtn;
    @FXML
    private Button searchBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Label categoryLabel;
    @FXML
    private ChoiceBox<String> categoryList;

    @FXML
    private TabPane tabs;
    @FXML
    private Tab booksTab;
    @FXML
    private Tab authorsTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchBtn.setDisable(true);
        openBtn.setDisable(true);
        booksTabController.setTableCells();
        authorsTabController.setTableCells();
        addBookListener();
        addAuthorListener();
        refreshCategoryList();
        checkActiveTab();
        checkSelectedCategory();
        checkTextLength();
        tabs.getSelectionModel().select(authorsTab);
        tabs.getSelectionModel().select(booksTab);
    }

    @FXML
    public void handleGetAll() {
        if (isActiveTabBooks) {
            List<BookDto> books = new ArrayList<>(restController.getBooks());
            booksTabController.setTableContent(books);
            categoryList.setValue("-- Show All --");
        } else {
            List<AuthorDto> authors = new ArrayList<>(restController.getAuthors());
            authorsTabController.setTableContent(authors);
        }
        refreshTotalLabel();
    }

    @FXML
    public void handleOpen() throws BookNotFoundException {
        if (isActiveTabBooks) {
            bookController.showBookWindow(restController.getBook(booksTabController.getSelectedContent()));
        } else {
            List<BookDto> books = new ArrayList<>(restController.getBooksByAuthor(authorsTabController.getSelectedContent()));
            tabs.getSelectionModel().select(booksTab);
            booksTabController.setTableContent(books);
            refreshTotalLabel();
        }
    }

    @FXML
    public void handleSearch() {
        if (name.getText().length() >= 3) {
            if (isActiveTabBooks) {
                List<BookDto> books = new ArrayList<>(restController.searchBook(name.getText()));
                booksTabController.setTableContent(books);
            } else {
                List<AuthorDto> authors = new ArrayList<>(restController.searchAuthor(name.getText()));
                authorsTabController.setTableContent(authors);
            }
            refreshTotalLabel();
        }
    }

    @FXML
    public void handleAdd() throws JSONException {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(dir);
        List<File> inputFiles = fc.showOpenMultipleDialog(null);
        if (inputFiles != null) {
            for (File file : inputFiles) {
                String path = file.getAbsolutePath();
                try (Stream<String> lines = Files.lines(Paths.get(path))) {
                    String jsonFile = lines.collect(Collectors.joining(System.lineSeparator()));
                    List<BookDto> books = jsonService.parseJson(jsonFile);
                    for (BookDto book : books) {
                        restController.createBook(book);
                    }
                    refreshCategoryList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            dir = inputFiles.get(0).getParentFile();
            pref.put(USER_DIR, dir.getAbsolutePath());
            handleGetAll();
        }
    }

    @FXML
    public void handleDelete() {
        restController.deleteBook(booksTabController.getSelectedContent());
        refreshCategoryList();
    }

    private void refreshCategoryList() {
        categoryList.getItems().clear();
        categoryList.getItems().add("-- Show All --");
        restController.getAllCategories().forEach(category -> categoryList.getItems().addAll(category.name));
    }

    private void refreshTotalLabel() {
        total.setText(isActiveTabBooks ? "Total: " + booksTabController.getItemsSize()
                : "Total: " + authorsTabController.getItemsSize());
    }

    private void checkSelectedCategory() {
        categoryList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals("-- Show All --")) {
                    handleGetAll();
                } else {
                    List<BookDto> books = new ArrayList<>(restController.getBooksByCategory(newValue));
                    booksTabController.setTableContent(books);
                    refreshTotalLabel();
                }
            } else {
                booksTabController.setTableContent(new ArrayList<>());
            }
        });
    }

    private void checkActiveTab() {
        tabs.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            isActiveTabBooks = newTab.equals(booksTab);
            addBtn.setVisible(isActiveTabBooks);
            deleteBtn.setVisible(isActiveTabBooks);
            categoryLabel.setVisible(isActiveTabBooks);
            categoryList.setVisible(isActiveTabBooks);
            openBtn.setText(isActiveTabBooks ? "Open" : "Get Books");
            updateOpenButton();
            refreshTotalLabel();
        });
    }

    public void addBookListener() {
        booksTabController.booksTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            isBookSelected = newValue != null;
            updateOpenButton();
        });
    }

    public void addAuthorListener() {
        authorsTabController.authorsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            isAuthorSelected = newValue != null;
            updateOpenButton();
        });
    }

    private void updateOpenButton() {
        openBtn.setDisable(isActiveTabBooks && !isBookSelected
                || !isActiveTabBooks && !isAuthorSelected);
    }

    private void checkTextLength() {
        name.setOnKeyReleased(e -> searchBtn.setDisable(name.getLength() < 3));
    }
}
