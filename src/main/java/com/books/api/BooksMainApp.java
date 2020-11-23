package com.books.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BooksMainApp extends Application {

    private ConfigurableApplicationContext springContext;
    private FXMLLoader fxmlLoader;

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(BooksMainApp.class);
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(springContext::getBean);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        fxmlLoader.setLocation(getClass().getResource("/fxml/main.fxml"));
        Parent rootNode;
        try {
            rootNode = fxmlLoader.load();
            primaryStage.setTitle("Books");
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/books.png")));
            Scene scene = new Scene(rootNode);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        springContext.stop();
        Platform.exit();
        System.exit(-1);
    }

    public static void main(String[] args) {
        launch(args);
    }
}