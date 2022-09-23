import api.client.UserApi;
import api.util.URLs;
import api.util.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest {
    UserApi userApi;

    @Before
    public void setUp() throws InterruptedException {
        RestAssured.baseURI = URLs.MAIN_PAGE;
        Thread.sleep(1000);
        this.userApi = new UserApi();
    }

    @After
    public void tearDown() {
        Response resp = this.userApi.loginUser(User.EMAIL, User.PASSWORD, this.userApi.getAccessToken());

        if (resp.getStatusCode() == SC_OK) {
            this.userApi.deleteUser();
        }
    }

    @DisplayName("Успешная регистрация пользователя")
    @Test
    public void createUserSuccessfullyTest() {
        Response response = this.userApi.createUserSuccessfully();
        response
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));

        this.userApi.saveTokens(response);
    }

    @DisplayName("создание существующего пользователя")
    @Test
    public void createExistedUserTest() {
        Response response = this.userApi.createUserSuccessfully();

        this.userApi.saveTokens(response);

        this.userApi.createUserSuccessfully()
                .then()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("message", equalTo("User already exists"));
    }

    @DisplayName("создание неверного пользователя")
    @Test
    public void createInvalidUserTest() {
        this.userApi.createUser(User.EMAIL, User.PASSWORD, "")
                .then()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
