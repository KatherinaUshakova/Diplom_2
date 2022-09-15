import DataForTests.Order;
import DataForTests.URLs;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.OrderIngredients;

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

    @Test
    public void createOrderLoggedInTest() {
        userApi.saveTokens(this.userApi.createUserSuccessfully());

        this.orderApi.createOrder(this.orderApi.makeChoice(), this.userApi.accessToken)
                .then()
                .statusCode(SC_OK)
                .and()
                .body( "success", equalTo(true));
    }

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

    @Test
    public void createOrderLoggedInWithoutIngredientsTest() {
        userApi.saveTokens(this.userApi.createUserSuccessfully());
        String[] order = {"", ""};

        this.orderApi.createOrder(new OrderIngredients(order), this.userApi.accessToken)
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);

        this.orderApi.createOrder(new OrderIngredients(), this.userApi.accessToken)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo(  "Ingredient ids must be provided"));
    }

    @Test
    public void createOrderWrongIngredientsTest() {
        userApi.saveTokens(this.userApi.createUserSuccessfully());
        String[] order = {Order.WRONG_HASH};

        this.orderApi.createOrder(new OrderIngredients(order), this.userApi.accessToken)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("One or more ids provided are incorrect"));
    }
}
