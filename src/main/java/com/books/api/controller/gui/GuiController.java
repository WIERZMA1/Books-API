package com.books.api.controller.gui;

import com.books.api.controller.RestController;
import com.books.api.domain.AuthorDto;
import com.books.api.domain.BookDto;
import com.books.api.service.JSONService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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

@Controller
public class GuiController implements Initializable {

    private static final String USER_DIR = "UserDir";
    private Preferences pref = Preferences.userNodeForPackage(getClass());
    private File dir = new File(pref.get(USER_DIR, System.getProperty("user.dir")));

    @Autowired
    BooksTabController booksController = new BooksTabController();
    @Autowired
    AuthorsTabController authorsController = new AuthorsTabController();
    @Autowired
    private RestController controller = new RestController();
    @Autowired
    private JSONService jsonService = new JSONService();

    private boolean activeTab;

    @FXML
    private TextField name;
    @FXML
    private Label total;
    @FXML
    private Button searchBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private ChoiceBox<String> categoryList;

    @FXML
    private TabPane tabs;
    @FXML
    private Tab booksTab;

    public GuiController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchBtn.setDisable(true);
        booksController.setTableCells();
        authorsController.setTableCells();
        categoryList.getItems().add("-- Show All --");
        refreshCategoryList();
        checkActiveTab();
        checkSelectedCategory();
        checkTextLength();
    }

    @FXML
    public void handleGetAll() {
        if (activeTab) {
            List<BookDto> books = new ArrayList<>(controller.getBooks());
            booksController.setTableContent(books);
            total.setText("Total: " + books.size());
        } else {
            List<AuthorDto> authors = new ArrayList<>(controller.getAuthors());
            authorsController.setTableContent(authors);
            total.setText("Total: " + authors.size());
        }
    }

    @FXML
    public void handleSearch() {
        if (name.getText().length() >= 3) {
            if (activeTab) {
                List<BookDto> books = new ArrayList<>(controller.searchBook(name.getText()));
                booksController.setTableContent(books);
                total.setText("Total: " + books.size());
            } else {
                List<AuthorDto> authors = new ArrayList<>(controller.searchAuthor(name.getText()));
                authorsController.setTableContent(authors);
                total.setText("Total: " + authors.size());
            }
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
                        controller.createBook(book);
                    }
                    refreshCategoryList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            handleGetAll();
        }
    }

    @FXML
    public void handleDelete() {
        controller.deleteBook(booksController.getSelectedContent());
        controller.getAllCategories().forEach(category -> categoryList.getItems().addAll(category.name));
    }

    private void refreshCategoryList() {
        controller.getAllCategories().forEach(category -> categoryList.getItems().addAll(category.name));
    }

    private void checkSelectedCategory() {
        categoryList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("-- Show All --")) {
                    handleGetAll();
                } else {
                    List<BookDto> books = new ArrayList<>(controller.getBooksByCategory(newValue));
                    booksController.setTableContent(books);
                    total.setText("Total: " + books.size());
                }
            }
        });
    }

    private void checkActiveTab() {
        tabs.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            activeTab = newTab.equals(booksTab);
            addBtn.setVisible(activeTab);
            deleteBtn.setVisible(activeTab);
        });
    }

    private void checkTextLength() {
        name.setOnKeyReleased(e -> {
            searchBtn.setDisable(name.getLength() < 3);
        });
    }
}
