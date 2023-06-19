import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class GetOrderTest {
    private User user;
    private Steps steps;
    private Methods methods;
    private String accessToken;
    private LoginUser loginUser;

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
    @DisplayName("Получение списка заказов без регистрации")
    public void getOrderNoUserTest() {
        ValidatableResponse response = steps.getNoUserOrders();
        methods.orderNoUser(response);
    }

    @Test
    @DisplayName("Получение списка заказов зарегестрированого пользователя")
    public void getOrderUserTest() {
        steps.create(user);
        ValidatableResponse response = steps.login(loginUser);
        accessToken = response.extract().path("accessToken").toString();
        steps.create(accessToken);
        steps.getOrders(accessToken);
    }
}
