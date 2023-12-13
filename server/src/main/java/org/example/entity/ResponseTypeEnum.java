package org.example.entity;

/**
 * Used to define endpoint and determine authorized endpoint or not
 */
public enum ResponseTypeEnum {
    REGISTER("register", false),
    LOGIN("login", false),
    LOGOUT("logout", true),
    SEND_FILE("send_file", true),
    GET_FILE_TREE("get_file_tree", true),
    DOWNLOAD_FILE("download_file", true),
    MOVE_FILE("move_file", true),
    COPY_FILE("copy_file", true),
    ;

    final String value;
    final boolean isAuthRequired;

    ResponseTypeEnum(String value, boolean isAuthRequired) {
        this.value = value;
        this.isAuthRequired = isAuthRequired;
    }

    public boolean isAuthRequired() {
        return isAuthRequired;
    }

    public String getValue() {
        return value;
    }
}
