import DataForTests.URLs;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.apache.http.HttpStatus.*;

public class GetUserOrderTest {

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
        if (this.userApi.loginUserSuccessfully().getStatusCode() == SC_OK) {
            this.userApi.deleteUser();
        }
    }

    @Test
    public void getOrdersByAuthorizedUserTest() {
        this.userApi.saveTokens(this.userApi.createUserSuccessfully());

        this.orderApi.getOrders(userApi.accessToken)
                .then()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    public void getOrdersByNotAuthorizedUserTest() {
        this.orderApi.getOrders("")
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}
