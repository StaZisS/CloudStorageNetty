package org.example.config;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.example.ServerInitializer;
import org.example.handler.auth.LoginHandler;
import org.example.handler.auth.LogoutHandler;
import org.example.handler.auth.RegistrationHandler;
import org.example.handler.CommandHandler;
import org.example.handler.file.*;
import org.example.repository.SessionRepository;
import org.example.repository.UserRepository;
import org.example.service.*;

public class BillingModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ServerInitializer.class).asEagerSingleton();


        bind(SessionRepository.class).asEagerSingleton();
        bind(UserRepository.class).asEagerSingleton();
        bind(AuthenticationService.class).to(AuthenticationServiceImpl.class);
        bind(SessionManager.class).to(SessionManagerImpl.class);
        bind(FileService.class).to(FileServiceImpl.class);


        Multibinder<CommandHandler> multibinder = Multibinder.newSetBinder(binder(), CommandHandler.class);
        multibinder.addBinding().to(LoginHandler.class);
        multibinder.addBinding().to(RegistrationHandler.class);
        multibinder.addBinding().to(LogoutHandler.class);
        multibinder.addBinding().to(SendFileHandler.class);
        multibinder.addBinding().to(FileTreeHandler.class);
        multibinder.addBinding().to(DownloadFileHandler.class);
        multibinder.addBinding().to(MoveFileHandler.class);
        multibinder.addBinding().to(CopyFileHandler.class);
    }

}

