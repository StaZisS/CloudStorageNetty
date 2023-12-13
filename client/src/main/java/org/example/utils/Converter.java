package org.example.utils;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.config.AppConfig;
import org.example.dto.FileNodeDTO;
import org.example.entity.FileEntity;

public class Converter {
    public static final Image folderIcon = new Image(AppConfig.getProperty("icon.folder.path"));
    public static final Image fileIcon = new Image(AppConfig.getProperty("icon.file.path"));

    public static TreeItem<FileEntity> convertToTreeItem(FileNodeDTO fileNodeDTO) {
        ImageView imageView = new ImageView();
        if (fileNodeDTO.isDirectory()) {
            imageView.setImage(folderIcon);
        } else {
            imageView.setImage(fileIcon);
        }

        imageView.setFitWidth(16);
        imageView.setFitHeight(16);

        var treeItem = new TreeItem<>(new FileEntity(fileNodeDTO.getName(), fileNodeDTO.isDirectory(), fileNodeDTO.getFileSize()), imageView);
        if (fileNodeDTO.isDirectory()) {
            for (var child : fileNodeDTO.getChildren()) {
                treeItem.getChildren().add(convertToTreeItem(child));
            }
        }

        return treeItem;
    }

    public static String getFullPath(TreeItem<FileEntity> selectedItem) {
        StringBuilder path = new StringBuilder(selectedItem.getValue().name());
        var parent = selectedItem.getParent();
        if (parent == null) {
            return "";
        }
        while (parent != null) {
            if (parent.getParent() == null) {
                break;
            }
            path.insert(0, parent.getValue() + "/");
            parent = parent.getParent();
        }
        return path.toString();
    }

}
