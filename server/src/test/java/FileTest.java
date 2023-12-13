import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.stream.ChunkedFile;
import org.example.config.AppConfig;
import org.example.dto.*;
import org.example.entity.ResponseTypeEnum;
import org.example.entity.StatusCodeEnum;
import org.example.json.RequestBody;
import org.example.json.ResponseBody;
import org.example.service.FileServiceImpl;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.example.json.ResponseBody.ResponseBodyBuilder;

public class FileTest {
    private final static String DIRECTORY_PATH = AppConfig.getProperty("server.file.dir");
    private final String nameTestFile = "test.txt";
    private final String nameTestDirectory = "test";
    private final String nameTestLargeFile = "largeTest.txt";
    private final long sizeLargeFile = 50L * 1024 * 1024;
    private EmbeddedChannel channel;

    @BeforeAll
    public static void createStartDirectory() {
        Path path = Path.of(DIRECTORY_PATH);
        try {
            Files.createDirectories(path);
        } catch (Exception ignored) {

        }
    }

    @AfterAll
    public static void deleteStartDirectory() {
        Path path = Path.of(DIRECTORY_PATH);
        try {
            Files.deleteIfExists(path);
        } catch (Exception ignored) {

        }
    }

    @BeforeEach
    public void setUp() {
        TestConfig configurator = new TestConfig();
        channel = configurator.getChannel();
        createTestFile();
    }

    public void createTestFile() {
        var pathTestFile = Path.of(DIRECTORY_PATH, nameTestFile);
        var pathTestDirectory = Path.of(DIRECTORY_PATH, nameTestDirectory);
        try {
            List<String> lines = List.of("hehe", "no hehe", "why not hehe?");
            Files.write(pathTestFile, lines, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.createDirectories(pathTestDirectory);
        } catch (Exception ignored) {

        }
    }

    @AfterEach
    public void deleteTestFile() {
        try (Stream<Path> paths = Files.walk(Path.of(DIRECTORY_PATH))) {
            paths.sorted(Comparator.reverseOrder())
                    .filter(path -> !path.equals(Path.of(DIRECTORY_PATH)))
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (Exception ignored) {
        }
    }

    public void emulateChunkedWriteHandler(EmbeddedChannel channelIn, File file) {
        try {
            ChunkedFile chunkedFile = new ChunkedFile(file, 64 * 1024);
            try {
                long bytesRemaining = chunkedFile.length();
                while (bytesRemaining > 0) {
                    ByteBuf chunk = chunkedFile.readChunk(channelIn.alloc());
                    int chunkSize = chunk.readableBytes();
                    bytesRemaining -= chunkSize;

                    channelIn.writeInbound(chunk);
                }
            } finally {
                chunkedFile.close();
            }
        } catch (Exception ignored) {
        }
    }

    public void emulateClientFileHandler(EmbeddedChannel channelIn, Path pathSaveFile, long sizeSaveFile) throws IOException {
        Files.createFile(pathSaveFile);
        try {
            long bytesRemaining = sizeSaveFile;
            while (bytesRemaining > 0) {
                ByteBuf chunk = channelIn.readOutbound();
                int chunkSize = chunk.readableBytes();
                bytesRemaining -= chunkSize;

                byte[] data = new byte[chunkSize];
                chunk.readBytes(data);
                Files.write(pathSaveFile, data, StandardOpenOption.APPEND);
                chunk.release();
            }
        } catch (Exception ignored) {
        }
    }

    private ResponseBody getResponseBody(ResponseTypeEnum responseTypeEnum, Object body) {
        return new ResponseBodyBuilder()
                .setTypeRequest(responseTypeEnum)
                .setBody(body)
                .build();
    }

    private void authenticateUser(EmbeddedChannel channelIn) {
        String username = "xexexe";
        String password = "123456789";
        var registrationDTO = new RegistrationDTO(username, password);
        var response = getResponseBody(
                ResponseTypeEnum.REGISTER,
                registrationDTO
        );

        channelIn.writeInbound(response);
        var request = (RequestBody) channelIn.readOutbound();
        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());

        var loginDTO = new LoginDTO(username, password);
        response = getResponseBody(
                ResponseTypeEnum.LOGIN,
                loginDTO
        );

        channelIn.writeInbound(response);

        request = channelIn.readOutbound();
        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());
    }

    private void generateRandomFile(String filePath, long fileSize) {
        try (RandomAccessFile randomFile = new RandomAccessFile(filePath, "rw")) {
            randomFile.setLength(fileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendFileSuccess() throws IOException {
        authenticateUser(channel);

        var whereTransferFile = Path.of(nameTestDirectory, nameTestFile);
        var whichFileTransfer = Path.of(DIRECTORY_PATH, nameTestFile);

        var testFile = new File(whichFileTransfer.toString());

        var sendFileDTO = new SendFileDTO(whereTransferFile.toString(), testFile.length());
        var response = getResponseBody(
                ResponseTypeEnum.SEND_FILE,
                sendFileDTO
        );

        channel.writeInbound(response);
        var request = (RequestBody) channel.readOutbound();
        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());

        emulateChunkedWriteHandler(channel, testFile);

        byte[] sendingFile = Files.readAllBytes(whichFileTransfer);
        byte[] testFileBytes = Files.readAllBytes(Path.of(DIRECTORY_PATH, whereTransferFile.toString()));
        assertArrayEquals(sendingFile, testFileBytes);
    }

    @Test
    public void sendLargeFileSuccess() throws IOException {
        authenticateUser(channel);

        var whereTransferFile = Path.of(nameTestDirectory, nameTestLargeFile);
        var whichFileTransfer = Path.of(DIRECTORY_PATH, nameTestLargeFile);

        generateRandomFile(whichFileTransfer.toString(), sizeLargeFile);

        var testFile = whichFileTransfer.toFile();

        var sendFileDTO = new SendFileDTO(whereTransferFile.toString(), testFile.length());
        var response = getResponseBody(
                ResponseTypeEnum.SEND_FILE,
                sendFileDTO
        );

        channel.writeInbound(response);
        var request = (RequestBody) channel.readOutbound();
        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());

        emulateChunkedWriteHandler(channel, testFile);


        byte[] sendingFile = Files.readAllBytes(whichFileTransfer);
        byte[] testFileBytes = Files.readAllBytes(Path.of(DIRECTORY_PATH, whereTransferFile.toString()));
        assertArrayEquals(sendingFile, testFileBytes);
    }

    @Test
    public void moveFileSuccess() throws IOException {
        authenticateUser(channel);

        var to = Path.of(nameTestDirectory);
        var from = Path.of(nameTestFile);
        var testFile = Path.of(DIRECTORY_PATH, nameTestFile).toFile();

        byte[] bytesTestFile = Files.readAllBytes(testFile.toPath());


        var moveFileDTO = new MoveFileDTO(from.toString(), to.toString());

        var response = getResponseBody(
                ResponseTypeEnum.MOVE_FILE,
                moveFileDTO
        );

        channel.writeInbound(response);
        var request = (RequestBody) channel.readOutbound();
        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());

        var path = Path.of(DIRECTORY_PATH, nameTestDirectory, nameTestFile);
        byte[] bytesSendFile = Files.readAllBytes(path);

        assertArrayEquals(bytesTestFile, bytesSendFile);
    }

    @Test
    public void downloadFileSuccess() throws IOException {
        authenticateUser(channel);

        var fileDownload = Path.of(nameTestFile);
        var testFile = Path.of(DIRECTORY_PATH, nameTestFile).toFile();

        byte[] bytesTestFile = Files.readAllBytes(testFile.toPath());


        var requestDownloadFileDTO = new RequestDownloadFileDTO(fileDownload.toString());

        var response = getResponseBody(
                ResponseTypeEnum.DOWNLOAD_FILE,
                requestDownloadFileDTO
        );

        channel.writeInbound(response);
        var request = (RequestBody) channel.readOutbound();
        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());

        var path = Path.of(DIRECTORY_PATH, nameTestDirectory, nameTestFile);
        emulateClientFileHandler(channel, path, testFile.length());
        byte[] bytesSendFile = Files.readAllBytes(path);

        assertArrayEquals(bytesTestFile, bytesSendFile);
    }

    @Test
    public void fileTreeSuccess() throws IOException {
        authenticateUser(channel);

        var response = getResponseBody(
                ResponseTypeEnum.GET_FILE_TREE,
                null
        );

        channel.writeInbound(response);
        var request = (RequestBody) channel.readOutbound();
        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());
        var body = (FileNodeDTO) request.getBody();

        var fileService = new FileServiceImpl();
        assertTrue(body.compare(fileService.getFileTree(DIRECTORY_PATH)));
    }

    @Test
    public void sendFileInDirectoryWhereFileWithThisNameExist() throws IOException {
        authenticateUser(channel);

        var whereTransferFile = Path.of(nameTestDirectory, nameTestFile);
        var whichFileTransfer = Path.of(DIRECTORY_PATH, nameTestFile);

        Files.createFile(Path.of(DIRECTORY_PATH, whereTransferFile.toString()));
        var testFile = whichFileTransfer.toFile();

        var response = getResponseBody(
                ResponseTypeEnum.SEND_FILE,
                new SendFileDTO(whereTransferFile.toString(), testFile.length())
        );
        channel.writeInbound(response);

        var request = (RequestBody) channel.readOutbound();
        assertEquals(StatusCodeEnum.ERROR.getValue(), request.getStatusCode());
    }

    @Test
    public void downloadNotExistFile() {
        authenticateUser(channel);

        var fileDownload = Path.of("notExistFile.txt");

        var response = getResponseBody(
                ResponseTypeEnum.DOWNLOAD_FILE,
                new RequestDownloadFileDTO(fileDownload.toString())
        );

        channel.writeInbound(response);
        var request = (RequestBody) channel.readOutbound();
        assertEquals(StatusCodeEnum.ERROR.getValue(), request.getStatusCode());
    }

    @Test
    public void sendDirectory() throws IOException {
        authenticateUser(channel);

        var whereTransferFile = Path.of(nameTestDirectory);
        var whichFileTransfer = Path.of(DIRECTORY_PATH, nameTestDirectory + "1");

        Files.createDirectory(whichFileTransfer);

        var testFile = new File(whichFileTransfer.toString());

        var sendFileDTO = new SendFileDTO(whereTransferFile.toString(), testFile.length());
        var response = getResponseBody(
                ResponseTypeEnum.SEND_FILE,
                sendFileDTO
        );

        channel.writeInbound(response);
        var request = (RequestBody) channel.readOutbound();
        assertEquals(request.getStatusCode(), StatusCodeEnum.ERROR.getValue());
    }

    @Test
    public void downloadDirectory() {
        authenticateUser(channel);

        var fileDownload = Path.of(nameTestDirectory);

        var response = getResponseBody(
                ResponseTypeEnum.DOWNLOAD_FILE,
                new RequestDownloadFileDTO(fileDownload.toString())
        );

        channel.writeInbound(response);
        var request = (RequestBody) channel.readOutbound();
        assertEquals(request.getStatusCode(), StatusCodeEnum.ERROR.getValue());
    }

    @Test
    public void CopyFileSuccess() throws IOException {
        authenticateUser(channel);

        var to = Path.of(nameTestDirectory);
        var from = Path.of(nameTestFile);

        var copyFileDTO = new CopyFileDTO(from.toString(), to.toString());
        var response = getResponseBody(
                ResponseTypeEnum.COPY_FILE,
                copyFileDTO
        );

        channel.writeInbound(response);
        var request = (RequestBody) channel.readOutbound();
        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());

        byte[] bytesFromFile = Files.readAllBytes(Path.of(DIRECTORY_PATH, from.toString()));
        byte[] bytesToFile = Files.readAllBytes(Path.of(DIRECTORY_PATH, to.toString(), from.toString()));

        assertArrayEquals(bytesFromFile, bytesToFile);
    }

    @Test
    public void CopyFileNotExist() {
        authenticateUser(channel);

        var to = Path.of(nameTestDirectory, nameTestFile);
        var from = Path.of("notExistFile.txt");

        var copyFileDTO = new CopyFileDTO(from.toString(), to.toString());
        var response = getResponseBody(
                ResponseTypeEnum.COPY_FILE,
                copyFileDTO
        );

        channel.writeInbound(response);
        var request = (RequestBody) channel.readOutbound();
        assertEquals(StatusCodeEnum.ERROR.getValue(), request.getStatusCode());
    }

}
