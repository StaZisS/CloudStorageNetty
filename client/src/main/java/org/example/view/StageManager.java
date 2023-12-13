package org.example.view;

import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.Network;
import org.example.config.FXMLLoader;
import org.example.pageController.PagePath;

import java.util.Objects;

public class StageManager {
    private final Stage primaryStage;
    private final FXMLLoader springFXMLLoader;

    @Inject
    public StageManager(FXMLLoader springFXMLLoader, Stage stage) {
        this.springFXMLLoader = springFXMLLoader;
        this.primaryStage = stage;
    }

    public void switchScene(final PagePath view) {
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFilePath());
        show(viewRootNodeHierarchy, view.getTitle());
    }

    public Stage getStage() {
        return primaryStage;
    }

    private void show(final Parent rootNode, String title) {
        Scene scene = prepareScene(rootNode);

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setOnCloseRequest(event -> closeApplication());

        try {
            primaryStage.show();
        } catch (Exception exception) {
            logAndExit("Unable to show scene for title" + title, exception);
        }
    }

    private Scene prepareScene(Parent rootNode) {
        Scene scene = primaryStage.getScene();

        if (scene == null) {
            scene = new Scene(rootNode);
        }
        scene.setRoot(rootNode);
        return scene;
    }

    private Parent loadViewNodeHierarchy(String fxmlFilePath) {
        Parent rootNode = null;
        try {
            rootNode = springFXMLLoader.load(fxmlFilePath);
            Objects.requireNonNull(rootNode, "A Root FXML node must not be null");
        } catch (Exception exception) {
            logAndExit("Unable to load FXML view" + fxmlFilePath, exception);
        }
        return rootNode;
    }


    private void logAndExit(String errorMsg, Exception e) {
        System.err.println(errorMsg);
        Platform.exit();
        throw new RuntimeException(e);
    }

    private void closeApplication() {
        Network.closeConnection();
        Platform.exit();
        System.exit(0);
    }

}
