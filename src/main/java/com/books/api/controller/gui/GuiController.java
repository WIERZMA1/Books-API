package com.books.api.controller.gui;

import com.books.api.controller.BookNotFoundException;
import com.books.api.controller.RestControllerService;
import com.books.api.domain.AuthorDto;
import com.books.api.domain.BookDto;
import com.books.api.service.JSONService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Button addJSONBtn;
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

    private int lastAction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchBtn.setDisable(true);
        openBtn.setDisable(true);
        searchBtn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/search.png"))));
        addJSONBtn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/addJSON.png"))));
        addBtn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/add.png"))));
        deleteBtn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/delete.png"))));
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
        lastAction = 1;
    }

    @FXML
    public void handleOpen() throws BookNotFoundException {
        if (isActiveTabBooks) {
            bookController.showBookWindow();
            bookController.populateBookValues(restController.getBook(booksTabController.getSelectedContent()));
        } else {
            String author = authorsTabController.getSelectedContent();
            List<BookDto> books = new ArrayList<>(restController.getBooksByAuthor(author));
            tabs.getSelectionModel().select(booksTab);
            booksTabController.setTableContent(books);
            refreshTotalLabel();
            lastAction = 3;
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
    public void handleAddJSON() throws JSONException {
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
        }
    }

    @FXML
    private void handleAdd() {
        bookController.showBookWindow();
    }

    @FXML
    private void handleDelete() throws BookNotFoundException {
        restController.deleteBook(booksTabController.getSelectedContent());
        String selectedCategory = categoryList.getSelectionModel().getSelectedItem();
        refreshCategoryList();
        categoryList.setValue(selectedCategory);
        retrieveLastAction();
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

    private void showBooksByCategory(String category) {
        if (category != null) {
            List<BookDto> books = new ArrayList<>(restController.getBooksByCategory(category));
            booksTabController.setTableContent(books);
            refreshTotalLabel();
        } else {
            booksTabController.setTableContent(new ArrayList<>());
        }
    }

    private void checkSelectedCategory() {
        categoryList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.equals("-- Show All --")) {
                handleGetAll();
            } else {
                showBooksByCategory(newValue);
            }
            lastAction = 2;
        });
    }

    private void checkActiveTab() {
        tabs.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            isActiveTabBooks = newTab.equals(booksTab);
            addJSONBtn.setVisible(isActiveTabBooks);
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

    public void retrieveLastAction() throws BookNotFoundException {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        switch (lastAction) {
            // Get All
            case 1:
                handleGetAll();
                break;
            // Select Category
            case 2:
                categoryList.setValue(categoryList.getSelectionModel().getSelectedItem());
                break;
            // Open Author's Books
            case 3:
                tabs.getSelectionModel().select(authorsTab);
                handleOpen();
                break;
            default:
                break;
        }
    }
}
