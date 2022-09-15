import DataForTests.URLs;
import DataForTests.User;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest {
    UserApi userApi;

    @Before
    public void setUp() {
        RestAssured.baseURI = URLs.MAIN_PAGE;
        this.userApi = new UserApi();
        this.userApi.saveTokens(this.userApi.createUserSuccessfully());
    }

    @After
    public void tearDown() {
        this.userApi.deleteUser();
    }

    @Test
    public void loginUserTest() {
        this.userApi.loginUserSuccessfully()
                .then()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    public void loginInvalidUserTest() throws InterruptedException {
        this.userApi.loginUser("", User.PASSWORD, this.userApi.accessToken)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("email or password are incorrect"));

        Thread.sleep(1000);

        this.userApi.loginUser(User.EMAIL, User.INVALID_PASSWORD, this.userApi.accessToken)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("email or password are incorrect"));

        Thread.sleep(1000);

        this.userApi.loginUserSuccessfully();
    }
}
