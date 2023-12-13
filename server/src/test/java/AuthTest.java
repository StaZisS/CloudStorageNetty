import io.netty.channel.embedded.EmbeddedChannel;
import org.example.dto.LoginDTO;
import org.example.dto.RegistrationDTO;
import org.example.entity.ResponseTypeEnum;
import org.example.entity.StatusCodeEnum;
import org.example.json.RequestBody;
import org.example.json.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.example.json.ResponseBody.ResponseBodyBuilder;

public class AuthTest {
    private final String username = "xexexe";
    private final String password = "123456789";
    private EmbeddedChannel channel;

    @BeforeEach
    public void setUp() {
        TestConfig configurator = new TestConfig();
        channel = configurator.getChannel();
    }

    private ResponseBody getResponseBody(ResponseTypeEnum responseTypeEnum, Object body) {
        return new ResponseBodyBuilder()
                .setTypeRequest(responseTypeEnum)
                .setBody(body)
                .build();
    }

    @Test
    public void registrationSuccess() {
        var registrationDTO = new RegistrationDTO(username, password);
        var response = getResponseBody(
                ResponseTypeEnum.REGISTER,
                registrationDTO
        );

        channel.writeInbound(response);

        var request = (RequestBody) channel.readOutbound();
        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());
    }

    @Test
    public void registrationUserExist() {
        var registrationDTO = new RegistrationDTO(username, password);
        var response = getResponseBody(
                ResponseTypeEnum.REGISTER,
                registrationDTO
        );

        channel.writeInbound(response);

        var request = (RequestBody) channel.readOutbound();

        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());

        channel.writeInbound(response);

        request = channel.readOutbound();
        assertEquals(StatusCodeEnum.ERROR.getValue(), request.getStatusCode());
    }

    @Test
    public void loginUserNotExist() {
        var loginDTO = new LoginDTO(username, password);
        var response = getResponseBody(
                ResponseTypeEnum.LOGIN,
                loginDTO
        );

        channel.writeInbound(response);

        var request = (RequestBody) channel.readOutbound();
        assertEquals(StatusCodeEnum.ERROR.getValue(), request.getStatusCode());
    }

    @Test
    public void loginSuccess() {
        var registrationDTO = new RegistrationDTO(username, password);
        var response = getResponseBody(
                ResponseTypeEnum.REGISTER,
                registrationDTO
        );

        channel.writeInbound(response);
        var request = (RequestBody) channel.readOutbound();
        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());

        var loginDTO = new LoginDTO(username, password);
        response = getResponseBody(
                ResponseTypeEnum.LOGIN,
                loginDTO
        );

        channel.writeInbound(response);

        request = channel.readOutbound();

        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());
    }

    @Test
    public void loginWrongCredentials() {
        var registrationDTO = new RegistrationDTO(username, password);
        var response = getResponseBody(
                ResponseTypeEnum.REGISTER,
                registrationDTO
        );

        channel.writeInbound(response);

        var request = (RequestBody) channel.readOutbound();
        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());

        var wrongUsername = "notxexexe";

        var loginDTO = new LoginDTO(wrongUsername, password);
        response = getResponseBody(
                ResponseTypeEnum.LOGIN,
                loginDTO
        );

        channel.writeInbound(response);

        request = channel.readOutbound();
        assertEquals(StatusCodeEnum.ERROR.getValue(), request.getStatusCode());
    }

    @Test
    public void goToAuthPageWithoutLogin() {
        var response = getResponseBody(
                ResponseTypeEnum.GET_FILE_TREE,
                null
        );

        channel.writeInbound(response);

        var request = (RequestBody) channel.readOutbound();
        assertEquals(StatusCodeEnum.ERROR.getValue(), request.getStatusCode());
    }

    @Test
    public void goToAuthPageAlreadyLogged() {
        var registrationDTO = new RegistrationDTO(username, password);
        var response = getResponseBody(
                ResponseTypeEnum.REGISTER,
                registrationDTO
        );

        channel.writeInbound(response);
        var request = (RequestBody) channel.readOutbound();
        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());

        var loginDTO = new LoginDTO(username, password);
        response = getResponseBody(
                ResponseTypeEnum.LOGIN,
                loginDTO
        );

        channel.writeInbound(response);

        request = channel.readOutbound();

        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());

        response = getResponseBody(
                ResponseTypeEnum.GET_FILE_TREE,
                null
        );

        channel.writeInbound(response);

        request = channel.readOutbound();

        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());
    }

    @Test
    public void logoutAlreadyLogged() {
        var registrationDTO = new RegistrationDTO(username, password);
        var response = getResponseBody(
                ResponseTypeEnum.REGISTER,
                registrationDTO
        );

        channel.writeInbound(response);
        var request = (RequestBody) channel.readOutbound();
        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());

        var loginDTO = new LoginDTO(username, password);
        response = getResponseBody(
                ResponseTypeEnum.LOGIN,
                loginDTO
        );

        channel.writeInbound(response);

        request = channel.readOutbound();

        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());

        response = getResponseBody(
                ResponseTypeEnum.LOGOUT,
                null
        );

        channel.writeInbound(response);

        request = channel.readOutbound();

        assertEquals(StatusCodeEnum.OK.getValue(), request.getStatusCode());
    }

    @Test
    public void logoutNotLogged() {
        var response = getResponseBody(
                ResponseTypeEnum.LOGOUT,
                null
        );

        channel.writeInbound(response);

        var request = (RequestBody) channel.readOutbound();

        assertEquals(StatusCodeEnum.ERROR.getValue(), request.getStatusCode());
    }
}
