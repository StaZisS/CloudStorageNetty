package org.example.dto;

public class RequestDownloadFileDTO {
    private String serverPath;

    public RequestDownloadFileDTO() {
    }

    public RequestDownloadFileDTO(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getServerPath() {
        return serverPath;
    }

}
