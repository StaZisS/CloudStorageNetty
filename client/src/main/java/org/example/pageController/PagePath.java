package org.example.pageController;

import java.util.ResourceBundle;

/**
 * Enum of all fxml files paths and titles
 */
public enum PagePath {
    CONNECT {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("connect.title");
        }

        @Override
        public String getFxmlFilePath() {
            return "/fxml/connectPage.fxml";
        }
    },
    LOGIN {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        public String getFxmlFilePath() {
            return "/fxml/loginPage.fxml";
        }
    },
    REGISTRATION {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("registration.title");
        }

        @Override
        public String getFxmlFilePath() {
            return "/fxml/registrationPage.fxml";
        }
    },
    MAIN {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("main.title");
        }

        @Override
        public String getFxmlFilePath() {
            return "/fxml/mainPage.fxml";
        }
    };

    public abstract String getTitle();

    public abstract String getFxmlFilePath();

    String getStringFromResourceBundle(String key) {
        return ResourceBundle.getBundle("Bundle").getString(key);
    }
}
