package org.example.service;

import io.netty.channel.Channel;
import org.example.dto.FileNodeDTO;

import java.io.File;
import java.io.IOException;

public interface FileService {
    FileNodeDTO getFileTree(String directoryPath) throws IOException;

    boolean isFileExist(String path);

    void moveFile(String file, String directory) throws IOException;

    void copyFile(String file, String directory) throws IOException;

    void sendFile(File file, Channel channel) throws IOException;
}
