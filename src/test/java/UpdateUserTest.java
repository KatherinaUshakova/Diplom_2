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
import static org.junit.Assert.assertEquals;

public class UpdateUserTest {
    String newName;
    String newEmail;
    UserApi userApi;

    @Before
    public void setUp() {
        RestAssured.baseURI = URLs.MAIN_PAGE;
        this.userApi = new UserApi();
        this.userApi.saveTokens(this.userApi.createUserSuccessfully());
    }

    @After
    public void tearDown() throws InterruptedException {
        Thread.sleep(1000);
        this.userApi.deleteUser();
    }

    @DisplayName("обновить данные юзера")
    @Test
    public void updateUserTest() throws InterruptedException {
        this.userApi.loginUserSuccessfully();

        Response response = this.userApi.updateUser("name", User.INVALID_NAME);

        response
                .then()
                .statusCode(SC_OK);

        newName = response
                .then()
                .extract()
                .path("user.name");

        assertEquals(User.INVALID_NAME, newName);

        Thread.sleep(1000);

        response = this.userApi.updateUser("email", User.INVALID_EMAIL);

        newEmail = response
                .then()
                .extract()
                .path("user.email");

        response
                .then()
                .statusCode(SC_OK);

        assertEquals(User.INVALID_EMAIL.toLowerCase(), newEmail);
    }

    @DisplayName("обновить данные неавторизованного юзера")
    @Test
    public void updateNotAuthorizedUserTest() {
        String correctToken = this.userApi.getAccessToken();
        this.userApi.setAccessToken("");

        this.userApi.updateUser("name", "hoho")
                .then()
                .statusCode(SC_UNAUTHORIZED);

        this.userApi.updateUser("email", "haha@mail.ru")
                .then()
                .statusCode(SC_UNAUTHORIZED);

        this.userApi.setAccessToken(correctToken);

        this.userApi.loginUserSuccessfully();
    }
}
