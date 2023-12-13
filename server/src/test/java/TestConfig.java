import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.example.handler.auth.LoginHandler;
import org.example.handler.auth.LogoutHandler;
import org.example.handler.auth.RegistrationHandler;
import org.example.handler.CommandHandler;
import org.example.handler.CommandRouter;
import org.example.handler.CommandType;
import org.example.handler.file.FileTreeHandler;
import org.example.handler.file.CopyFileHandler;
import org.example.handler.file.MoveFileHandler;
import org.example.handler.file.SendFileHandler;
import org.example.handler.file.DownloadFileHandler;
import org.example.json.JsonDecoder;
import org.example.repository.SessionRepository;
import org.example.repository.UserRepository;
import org.example.service.FileService;
import org.example.service.SessionManager;
import org.example.service.SessionManagerImpl;
import org.example.service.AuthenticationService;
import org.example.service.AuthenticationServiceImpl;
import org.example.service.FileServiceImpl;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestConfig {
    private final EmbeddedChannel channel;

    public TestConfig() {
        UserRepository userRepository = new UserRepository();
        SessionRepository sessionRepository = new SessionRepository();
        SessionManager sessionManager = new SessionManagerImpl(sessionRepository);
        AuthenticationService authenticationService =
                new AuthenticationServiceImpl(userRepository);
        FileService fileService = new FileServiceImpl();

        Set<CommandHandler> commandHandlers = Set.of(
                new LoginHandler(authenticationService, sessionManager),
                new RegistrationHandler(authenticationService),
                new LogoutHandler(sessionManager),
                new FileTreeHandler(fileService),
                new MoveFileHandler(fileService),
                new SendFileHandler(fileService),
                new DownloadFileHandler(fileService),
                new CopyFileHandler(fileService)
        );

        var handlers = commandHandlers.stream()
                .collect(Collectors.toMap(handler -> {
                    CommandType annotation = handler.getClass().getAnnotation(CommandType.class);
                    if (annotation != null) {
                        return annotation.value();
                    }
                    throw new IllegalArgumentException("Command handler is missing CommandType annotation");
                }, Function.identity()));

        channel = new EmbeddedChannel(
                new JsonDecoder(),
                new ChunkedWriteHandler(),
                new CommandRouter(handlers, sessionManager)
        );
    }

    public EmbeddedChannel getChannel() {
        return channel;
    }
}
