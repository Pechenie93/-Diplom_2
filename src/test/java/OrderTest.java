import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrderTest {
    private Steps steps;
    private User user;
    private Methods methods;
    private LoginUser loginUser;

    private String accessToken;
    private int code;
    private boolean statys;

    @Before
    public void setOrder() {
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
    @DisplayName("Создание заказа без авторизации")
    public void createOrderNoUserTest() {
        ValidatableResponse response = steps.create();
        Methods.createOrderResponse(response, code, statys);
    }

    @Test
    @DisplayName("Создание заказа с невернымс неверным хешем ингредиентов ")
    public void createOrderIncorrectIngredientTest() {
        steps.create(user);
        ValidatableResponse response = steps.login(loginUser);
        accessToken = response.extract().path("accessToken").toString();
        steps.createNeverIngredients(accessToken);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингридиентами")
    public void createOrderTest() {
        steps.create(user);
        ValidatableResponse response = steps.login(loginUser);
        accessToken = response.extract().path("accessToken").toString();
        steps.create(accessToken);
        Methods.createOrderResponse(response, code, statys);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией без ингридиентов")
    public void createOrderNoIngredientTest() {
        steps.create(user);
        ValidatableResponse response = steps.login(loginUser);
        accessToken = response.extract().path("accessToken").toString();
        steps.createNoIngredients(accessToken);

    }
}
