package org.example.handler.auth;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandlerContext;
import org.example.dto.RegistrationDTO;
import org.example.entity.ResponseTypeEnum;
import org.example.handler.CommandHandler;
import org.example.handler.CommandType;
import org.example.service.AuthenticationService;
import org.example.utils.JsonUtils;
import org.example.utils.ResponseUserUtils;

@CommandType(ResponseTypeEnum.REGISTER)
public class RegistrationHandler implements CommandHandler {
    private final AuthenticationService authService;

    @Inject
    public RegistrationHandler(AuthenticationService authenticationService) {
        this.authService = authenticationService;
    }

    @Override
    public void handle(Object body, ChannelHandlerContext ctx) {
        try {
            RegistrationDTO registrationDTO = JsonUtils.fromJson(body, RegistrationDTO.class);
            authService.register(registrationDTO.getUsername(), registrationDTO.getPassword());
            ResponseUserUtils.sendOk(ctx, "Registration successful");
        } catch (Exception e) {
            ResponseUserUtils.sendError(ctx, e.getMessage());
        }
    }
}
