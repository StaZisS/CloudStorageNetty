package org.example.presenter;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.entity.FileEntity;
import org.example.model.Callback;
import org.example.model.main_page.*;
import org.example.utils.Converter;
import org.example.view.MainView;
import org.example.view.OkDialog;

import java.io.File;
import java.util.Map;

public class MainPresenter {
    private final MainModel model;
    private final MainView view;


    public MainPresenter(MainModel model, MainView view) {
        this.model = model;
        this.view = view;
    }

    public void handleRequestFileDirectory() {
        model.requestFileTreeFromServer(new Callback() {
            @Override
            public void onSuccess() {
                view.displayFileDirectory(model.getFileNode());
            }

            @Override
            public void onFailure(Object errorMessage) {
                view.switchToLogin();
                view.displayError(errorMessage);
            }
        });
    }

    public void handleSendFile(TreeItem<FileEntity> selectedItem, Stage stage) {
        if (selectedItem == null) {
            view.displayError("Select directory in server");
            return;
        }

        if (!selectedItem.getValue().isDirectory()) {
            view.displayError("Select directory in server, not file");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file to send");

        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }

        if (file.isDirectory()) {
            view.displayError("Select file, not directory");
            return;
        }

        var pathInServer = Converter.getFullPath(selectedItem) + "/" + file.getName();

        new SendFileModel().sendFile(file, pathInServer, new Callback() {
            @Override
            public void onSuccess() {
                OkDialog.showOk("File sent");
                view.refreshFileDirectory();
            }

            @Override
            public void onFailure(Object errorMessage) {
                view.displayError(errorMessage);
            }
        });
    }

    public void handleDownloadFile(TreeItem<FileEntity> selectedItem, Stage stage) {
        if (selectedItem == null) {
            view.displayError("Select file in server");
            return;
        }

        if (selectedItem.getValue().isDirectory()) {
            view.displayError("Select file in server, not directory");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select directory to save file");
        fileChooser.setInitialFileName(selectedItem.getValue().name());

        File file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            return;
        }

        var pathInServer = Converter.getFullPath(selectedItem);

        new ReceiveFileModel().receivedFile(pathInServer, file.getAbsolutePath(), selectedItem.getValue().size());
    }

    public void handleMoveFile(ObservableList<TreeItem<FileEntity>> listSelectedItem) {
        var paths = getSelectFileAndDirectory(listSelectedItem);

        if (paths == null) {
            return;
        }

        var pathFrom = paths.getKey();
        var pathTo = paths.getValue();

        new MoveFileModel().moveFile(pathFrom, pathTo);
    }

    public void handleCopyFile(ObservableList<TreeItem<FileEntity>> listSelectedItem) {
        var paths = getSelectFileAndDirectory(listSelectedItem);

        if (paths == null) {
            return;
        }

        var pathFrom = paths.getKey();
        var pathTo = paths.getValue();

        new CopyFileModel().copyFile(pathFrom, pathTo);
    }

    private Map.Entry<String, String> getSelectFileAndDirectory(ObservableList<TreeItem<FileEntity>> listSelectedItem) {
        String pathFrom;
        String pathTo;
        if (listSelectedItem.size() != 2) {
            view.displayError("Select need only 2 file");
            return null;
        }

        var firstFileEntity = listSelectedItem.get(0).getValue();
        var secondFileEntity = listSelectedItem.get(1).getValue();

        if (firstFileEntity.isDirectory() == secondFileEntity.isDirectory()) {
            view.displayError("Select need only 1 file and 1 directory");
            return null;
        }

        if (firstFileEntity.isDirectory()) {
            pathFrom = Converter.getFullPath(listSelectedItem.get(1));
            pathTo = Converter.getFullPath(listSelectedItem.get(0));
        } else {
            pathFrom = Converter.getFullPath(listSelectedItem.get(0));
            pathTo = Converter.getFullPath(listSelectedItem.get(1));
        }

        return Map.entry(pathFrom, pathTo);
    }
}
