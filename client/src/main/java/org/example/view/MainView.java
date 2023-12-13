package org.example.view;

import org.example.dto.FileNodeDTO;

public interface MainView {
    void displayFileDirectory(FileNodeDTO fileNode);

    void displayError(Object message);

    void switchToLogin();

    void refreshFileDirectory();
}
