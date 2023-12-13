package org.example.config;

import com.google.inject.AbstractModule;
import javafx.stage.Stage;
import org.example.config.ResourceBundleProvider;

import java.util.ResourceBundle;

public class BillingModule extends AbstractModule {
    private final Stage primaryStage;

    public BillingModule(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    protected void configure() {
        bind(ResourceBundle.class).toProvider(ResourceBundleProvider.class);
        bind(Stage.class).toInstance(primaryStage);
    }
}
