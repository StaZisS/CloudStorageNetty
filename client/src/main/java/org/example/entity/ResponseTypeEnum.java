package org.example.entity;

/**
 * Used to define endpoints on the server
 */
public enum ResponseTypeEnum {
    REGISTER("register"),
    LOGIN("login"),
    LOGOUT("logout"),
    SEND_FILE("send_file"),
    GET_FILE_TREE("get_file_tree"),
    DOWNLOAD_FILE("download_file"),
    MOVE_FILE("move_file"),
    COPY_FILE("copy_file"),
    ;

    final String value;

    ResponseTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
