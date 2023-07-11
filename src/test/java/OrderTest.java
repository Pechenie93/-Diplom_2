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
    private boolean status;

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
        String[] ingredients = {}; // Пустой массив ингредиентов
        ValidatableResponse response = steps.create(ingredients);
        methods.createOrderResponse(response, 400, false); // Укажите ожидаемый код и статус
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderIncorrectIngredientTest() {
        steps.create(user);
        ValidatableResponse response = steps.login(loginUser);
        accessToken = response.extract().path("accessToken").toString();

        String[] invalidIngredients = {"invalid1", "invalid2", "invalid3"};
        ValidatableResponse createOrderResponse = steps.createInvalidIngredients(accessToken, invalidIngredients);
        methods.assertErrorMessage(createOrderResponse, "One or more ids provided are incorrect");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void createOrderTest() {
        steps.create(user);
        ValidatableResponse loginResponse = steps.login(loginUser);
        accessToken = loginResponse.extract().path("accessToken").toString();

        String[] ingredients = {"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa73"};

        ValidatableResponse createOrderResponse = steps.create(accessToken, ingredients);
        methods.createOrderResponse(createOrderResponse, 200, true);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией без ингредиентов")
    public void createOrderNoIngredientTest() {
        steps.create(user);
        ValidatableResponse response = steps.login(loginUser);
        accessToken = response.extract().path("accessToken").toString();
        ValidatableResponse createOrderResponse = steps.createNoIngredients(accessToken);
        methods.assertErrorMessage(createOrderResponse, "Ingredient ids must be provided");
    }
}