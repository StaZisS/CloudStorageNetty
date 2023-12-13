package org.example.dto;

import java.util.List;

public class FileNodeDTO {
    private String name;
    private boolean isDirectory;
    private List<FileNodeDTO> children;
    private long fileSize;

    public FileNodeDTO() {
    }

    public FileNodeDTO(String name, boolean isDirectory, List<FileNodeDTO> children, long fileSize) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.children = children;
        this.fileSize = fileSize;
    }

    public List<FileNodeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<FileNodeDTO> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean compare(FileNodeDTO fileNodeDTO) {
        if (this.name.equals(fileNodeDTO.getName()) && this.isDirectory == fileNodeDTO.isDirectory() && this.fileSize == fileNodeDTO.getFileSize()) {
            if (this.children.size() == fileNodeDTO.getChildren().size()) {
                for (int i = 0; i < this.children.size(); i++) {
                    if (!this.children.get(i).compare(fileNodeDTO.getChildren().get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
