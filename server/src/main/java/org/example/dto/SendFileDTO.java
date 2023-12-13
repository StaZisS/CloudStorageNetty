package org.example.dto;

public class SendFileDTO {
    private String serverPath;
    private long fileSize;

    public SendFileDTO() {
    }

    public SendFileDTO(String serverPath, long fileSize) {
        this.serverPath = serverPath;
        this.fileSize = fileSize;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
