package org.example.pageController;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeView;
import org.example.dto.FileNodeDTO;
import org.example.entity.FileEntity;
import org.example.model.LogoutModel;
import org.example.model.main_page.MainModel;
import org.example.presenter.MainPresenter;
import org.example.utils.Converter;
import org.example.view.ErrorDialog;
import org.example.view.MainView;
import org.example.view.StageManager;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable, MainView {
    private final StageManager stageManager;
    public TreeView<FileEntity> treeView;

    private MainPresenter presenter;

    @Inject
    MainPageController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainModel model = new MainModel();
        presenter = new MainPresenter(model, this);
        presenter.handleRequestFileDirectory();
        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @Override
    public void displayFileDirectory(FileNodeDTO fileNode) {
        var treeItem = Converter.convertToTreeItem(fileNode);
        treeView.setRoot(treeItem);
    }

    @Override
    public void displayError(Object message) {
        ErrorDialog.showError(message);
    }

    @Override
    public void switchToLogin() {
        stageManager.switchScene(PagePath.LOGIN);
    }

    @Override
    public void refreshFileDirectory() {
        presenter.handleRequestFileDirectory();
    }

    public void logout(ActionEvent actionEvent) {
        LogoutModel.logout();
        stageManager.switchScene(PagePath.LOGIN);
    }

    public void switchToRegistration(ActionEvent actionEvent) {
        stageManager.switchScene(PagePath.REGISTRATION);
    }

    public void switchToLogin(ActionEvent actionEvent) {
        stageManager.switchScene(PagePath.LOGIN);
    }

    public void sendFile(ActionEvent actionEvent) {
        if (treeView.getSelectionModel().getSelectedItems().size() > 1) {
            ErrorDialog.showError("Select only one directory");
            return;
        }

        var selectedFile = treeView.getSelectionModel().getSelectedItem();
        presenter.handleSendFile(selectedFile, stageManager.getStage());
    }

    public void downloadFile(ActionEvent actionEvent) {
        if (treeView.getSelectionModel().getSelectedItems().size() > 1) {
            ErrorDialog.showError("Select only one file");
            return;
        }

        var selectedFile = treeView.getSelectionModel().getSelectedItem();
        presenter.handleDownloadFile(selectedFile, stageManager.getStage());
    }

    public void refreshPage(ActionEvent actionEvent) {
        refreshFileDirectory();
    }

    public void moveFile(ActionEvent actionEvent) {
        var selectedFile = treeView.getSelectionModel().getSelectedItems();
        presenter.handleMoveFile(selectedFile);
    }

    public void copyFile(ActionEvent actionEvent) {
        var selectedFile = treeView.getSelectionModel().getSelectedItems();
        presenter.handleCopyFile(selectedFile);
    }
}
