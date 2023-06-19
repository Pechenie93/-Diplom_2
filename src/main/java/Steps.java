import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class Steps extends Client {
    private static final String CREATE_USER_ENDPOINT = "/api/auth/register";
    private static final String LOGIN_USER_ENDPOINT = "/api/auth/login ";
    private static final String USER_USER_ENDPOINT = "/api/auth/user";
    private static final String CREATE_ORDER_ENDPOINT = "/api/orders";
    private static final String INGREDIENTS_ENDPOINT = "/api/ingredients";
    private static final String GET_ORDER_ENDPOINT = "/api/orders";

    @Step("Создание пользователя")
    public ValidatableResponse create(User user) {
        return given().log().all().spec(getSpec()).body(user).when().post(CREATE_USER_ENDPOINT).then().log().all();
    }

    @Step("Авторизациця пользователя")
    public ValidatableResponse login(LoginUser loginUser) {
        return given().log().all().spec(getSpec()).body(loginUser).when().post(LOGIN_USER_ENDPOINT).then().log().all();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(String accessToken) {
        return given().log().all().spec(getSpec()).header("Authorization", accessToken).when().delete(USER_USER_ENDPOINT).then().log().all();

    }

    @Step("Обновление пользователя")
    public ValidatableResponse update(String accessToken, User user) {
        return given().log().all().spec(getSpec()).header("Authorization", accessToken).body(user).when().patch(USER_USER_ENDPOINT).then().log().all();
    }

    @Step("Получение данных пользователя")
    public ValidatableResponse get(String accessToken) {
        return given().log().all().spec(getSpec()).header("Authorization", accessToken).when().get(USER_USER_ENDPOINT).then().log().all();
    }

    @Step("Создание заказа не авторезированого пользователя")
    public ValidatableResponse create() {
        return given().spec(getSpec()).when().body("{\n\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa70\",\"61c0c5a71d1f82001bdaaa73\"]\n}").post(CREATE_ORDER_ENDPOINT).then().log().all();
    }

    @Step("Создание заказа авторезированого пользователя")
    public ValidatableResponse create(String accessToken) {
        return given().spec(getSpec()).header("Authorization", accessToken).when().body("{\n\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa70\",\"61c0c5a71d1f82001bdaaa73\"]\n}").post(CREATE_ORDER_ENDPOINT).then().log().all();
    }

    @Step("Создание заказа авторезированого пользователя неверным хешем")
    public ValidatableResponse createNeverIngredients(String accessToken) {
        return given().spec(getSpec()).header("Authorization", accessToken).when().body("{\n\"ingredients\": [\"91c0c5a71d1f82001bdaaa6d\",\"91c0c5a71d1f82001bdaaa70\",\"91c0c5a71d1f82001bdaaa73\"]\n}").post(CREATE_ORDER_ENDPOINT).then().log().all().statusCode(400).and().assertThat().body("message", equalTo("One or more ids provided are incorrect"));
    }

    @Step("Создание заказа авторезированого пользователя без ингридиентов")
    public ValidatableResponse createNoIngredients(String accessToken) {
        return given().spec(getSpec()).header("Authorization", accessToken).when().post(CREATE_ORDER_ENDPOINT).then().log().all().assertThat().statusCode(400).and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Получение ингридиентов")
    public ValidatableResponse getIngredients() {
        return given().spec(getSpec()).when().get(INGREDIENTS_ENDPOINT).then().log().all();
    }

    @Step("Получение заказа")
    public ValidatableResponse getOrders(String accessToken) {
        return given().spec(getSpec()).header("Authorization", accessToken).when().get(GET_ORDER_ENDPOINT).then().log().all().assertThat().statusCode(200).body("success", is(true));
    }

    @Step("Получение заказа")
    public ValidatableResponse getNoUserOrders() {
        return given().spec(getSpec()).when().get(GET_ORDER_ENDPOINT).then().log().all();
    }

}

