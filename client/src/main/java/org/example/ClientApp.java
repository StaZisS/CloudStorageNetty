package org.example;


import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import org.example.config.BillingModule;
import org.example.pageController.PagePath;
import org.example.view.StageManager;


public class ClientApp extends Application {
    protected StageManager stageManager;
    protected Injector injector;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() {
    }

    @Override
    public void start(Stage stage) {
        injector = Guice.createInjector(
                new BillingModule(stage)
        );
        stageManager = injector.getInstance(StageManager.class);
        displayInitialScene();
    }

    @Override
    public void stop() {
        Network.closeConnection();
    }

    protected void displayInitialScene() {
        stageManager.switchScene(PagePath.CONNECT);
    }

}