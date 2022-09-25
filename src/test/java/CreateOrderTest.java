import api.client.OrderApi;
import api.client.UserApi;
import api.util.Order;
import api.util.URLs;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import api.model.OrderIngredients;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest {
    OrderApi orderApi;
    UserApi userApi;

    @Before
    public void setUp() {
        RestAssured.baseURI = URLs.MAIN_PAGE;
        orderApi = new OrderApi();
        userApi = new UserApi();
    }

    @After
    public void tearDown() {
        this.userApi.deleteUser();
    }

    @DisplayName("Создание заказа для авторизованного пользователя")
    @Test
    public void createOrderLoggedInTest() {
        userApi.saveTokens(this.userApi.createUserSuccessfully());

        this.orderApi.createOrder(this.orderApi.makeChoice(), this.userApi.getAccessToken())
                .then()
                .statusCode(SC_OK)
                .and()
                .body( "success", equalTo(true));
    }

    @DisplayName("Создание заказа для неавторизованного пользователя")
    @Test
    public void createOrderNotLoggedInTest() {
        this.orderApi.createOrder(this.orderApi.makeChoice(), "")
                .then()
                .statusCode(SC_OK)
                .and()
                .body("order.number", notNullValue())
                .and()
                .body("success", equalTo(true));
    }

    @DisplayName("создание заказа авторизованным пользователем без ингредиентов")
    @Test
    public void createOrderLoggedInWithoutIngredientsTest() {
        userApi.saveTokens(this.userApi.createUserSuccessfully());
        String[] order = {"", ""};

        this.orderApi.createOrder(new OrderIngredients(order), this.userApi.getAccessToken())
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);

        this.orderApi.createOrder(new OrderIngredients(), this.userApi.getAccessToken())
                .then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo(  "Ingredient ids must be provided"));
    }

    @DisplayName("создание заказа авторизованным пользователем с неверным хешем ингредиентов")
    @Test
    public void createOrderWrongIngredientsTest() {
        userApi.saveTokens(this.userApi.createUserSuccessfully());
        String[] order = {Order.WRONG_HASH};

        this.orderApi.createOrder(new OrderIngredients(order), this.userApi.getAccessToken())
                .then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("One or more ids provided are incorrect"));
    }
}
