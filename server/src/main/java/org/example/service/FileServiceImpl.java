package org.example.service;

import io.netty.channel.Channel;
import io.netty.handler.stream.ChunkedFile;
import org.example.config.AppConfig;
import org.example.dto.FileNodeDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class FileServiceImpl implements FileService {
    private final static String ROOT_DIRECTORY = AppConfig.getProperty("server.file.dir");

    @Override
    public FileNodeDTO getFileTree(String directoryPath) throws IOException {
        var directory = Paths.get(directoryPath);
        var node = new FileNodeDTO(
                directory.getFileName().toString(),
                Files.isDirectory(directory),
                new ArrayList<>(),
                directory.toFile().length()
        );

        if (Files.isDirectory(directory)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
                for (Path path : stream) {
                    node.getChildren().add(getFileTree(path.toString()));
                }
            } catch (IOException e) {
                throw new IOException(e);
            }
        }

        return node;
    }

    @Override
    public boolean isFileExist(String path) {
        return Files.exists(Path.of(ROOT_DIRECTORY, path));
    }

    @Override
    public void moveFile(String file, String directory) throws IOException {
        var filePath = Paths.get(ROOT_DIRECTORY, file);
        var directoryPath = Paths.get(ROOT_DIRECTORY, directory);
        Files.move(filePath, directoryPath.resolve(filePath.getFileName()));
    }

    @Override
    public void copyFile(String file, String directory) throws IOException {
        var filePath = Paths.get(ROOT_DIRECTORY, file);
        var directoryPath = Paths.get(ROOT_DIRECTORY, directory, filePath.getFileName().toString());
        Files.copy(filePath, directoryPath);
    }

    @Override
    public void sendFile(File file, Channel channel) throws IOException {
        ChunkedFile chunkedFile = new ChunkedFile(file, 64 * 1024);
        channel.writeAndFlush(chunkedFile);
    }
}
