import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginTest {
    private User user;
    private Steps steps;
    private LoginUser loginUser;
    private Methods methods;
    private String accessToken;
    private int code;
    private boolean statys;

    @Before
    public void setUser() {
        user = UserGenerator.random();
        steps = new Steps();
        methods = new Methods();
        loginUser = new LoginUser(user);

    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            steps.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Тестирование авторизации")
    public void authorizationLoginTest() {
        steps.create(user);
        ValidatableResponse response = steps.login(loginUser);
        accessToken = response.extract().path("accessToken").toString();
        methods.createUserResponse(response, code, statys);

    }

    @Test
    @DisplayName("Авторизация без логина")
    public void authorizationNoEmailTest() {
        user.setEmail("");
        ValidatableResponse response = steps.login(loginUser);
        methods.emailOrPasswordIncorrect(response);

    }

    @Test
    @DisplayName("Авторизация без пароля")
    public void authorizationNoPasswordTest() {
        user.setPassword("");
        ValidatableResponse response = steps.login(loginUser);
        methods.emailOrPasswordIncorrect(response);

    }
}
