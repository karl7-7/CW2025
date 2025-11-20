package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception { //start is the main entry point for javaFX applications

        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        ResourceBundle resources = null; // To be used for localisation, not needed here
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources); // FXMLLoader loads the FXML file
        Parent root = fxmlLoader.load(); // Parent serves as an abstract base class for all nodes that can have children in the scene graph. load() method parses the FXML and returns the root node of the scene graph
        GuiController c = fxmlLoader.getController(); // getController() retrieves the controller associated with the FXML

        primaryStage.setTitle("TetrisJFX"); // set the title of the window
        Scene scene = new Scene(root, 300, 510); // create a scene with the root node and specified dimensions
        primaryStage.setScene(scene); // set the scene on the primary stage
        primaryStage.show(); // show() makes the window visible
        new GameController(c); // Create a new GameController instance, passing the GuiController
    }


    public static void main(String[] args) { //code will start executing from here
        launch(args); // launch readies the application then invokes start
    }
}
