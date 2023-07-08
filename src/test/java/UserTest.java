import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class UserTest {
    private User user;
    private Steps steps;
    private Methods methods;
    private String accessToken;
    private int code;
    private boolean status;

    @Before
    public void setUser() {
        user = UserGenerator.random();
        steps = new Steps();
        methods = new Methods();

    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            steps.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Тестирование создание пользователя")
    public void creatureUserTest() {
        ValidatableResponse response = steps.create(user);
        accessToken = response.extract().path("accessToken").toString();
        methods.createUserResponse(response, code, status);

    }

    @Test
    @DisplayName("Тестирование создание пользователя без имени")
    public void creatureUserWithoutNameTest() {
        user.setName("");
        ValidatableResponse response = steps.create(user);
        methods.correctCreateUserResponse(response);

    }

    @Test
    @DisplayName("Тестирование создание пользователя без email")
    public void creatureUserWithoutEmailTest() {
        user.setEmail("");
        ValidatableResponse response = steps.create(user);
        methods.correctCreateUserResponse(response);

    }

    @Test
    @DisplayName("Тестирование создание пользователя без пароля")
    public void creatureUserWithoutPasswordTest() {
        user.setPassword("");
        ValidatableResponse response = steps.create(user);
        methods.correctCreateUserResponse(response);
    }

    @Test
    @DisplayName("Тестируем создание пользователя с одинаковым логином")
    public void creatureUserDoubleTest() {
        // Создание первого пользователя
        ValidatableResponse responseFirst = steps.create(user);
        methods.correctCreateUserResponse(responseFirst);

        // Создание второго пользователя с теми же учетными данными
        ValidatableResponse responseSecond = steps.create(user);

        // Проверка, что второй пользователь не создался
        responseSecond.assertThat().statusCode(400);

        // Удаление первого пользователя
        String firstUserAccessToken = responseFirst.extract().path("accessToken").toString();
        steps.delete(firstUserAccessToken);
    }

}
