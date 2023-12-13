package org.example.config;

import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.ResourceBundle;

public class FXMLLoader {
    private final ResourceBundle resourceBundle;
    private final Injector injector;

    @Inject
    public FXMLLoader(ResourceBundle resourceBundle, Injector injector) {
        this.resourceBundle = resourceBundle;
        this.injector = injector;
    }

    public Parent load(String fxmlPath) throws IOException {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader();
        loader.setResources(resourceBundle);
        loader.setLocation(getClass().getResource(fxmlPath));
        loader.setControllerFactory(injector::getInstance);
        return loader.load();
    }
}