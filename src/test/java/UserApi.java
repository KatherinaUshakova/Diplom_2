import DataForTests.URLs;
import io.restassured.response.Response;
import pojo.User;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserApi {
    String accessToken;
    String refreshToken;

    public Response createUser(String email, String password, String name) {

        User data = new User(email, password, name);

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(data)
                .when()
                .post(URLs.CREATE_USER);
    }

    public Response createUserSuccessfully() {
        return createUser(DataForTests.User.EMAIL, DataForTests.User.PASSWORD, DataForTests.User.NAME);
    }

    public String getAccessToken(Response response) {
        return response.then().extract().path("accessToken");
    }

    public String getRefreshToken(Response response) {
        return response.then().extract().path("refreshToken");
    }

    public Response loginUser(String email, String password, String accessToken) {
        User data = new User(email, password);

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("Authorization", accessToken);

        return given()
                .headers(headers)
                .and()
                .body(data)
                .when()
                .post(URLs.LOGIN_USER);
    }

    public Response loginUserSuccessfully() {
        return loginUser(DataForTests.User.EMAIL, DataForTests.User.PASSWORD, this.accessToken);
    }

    public void saveTokens(Response response) {
        this.accessToken = getAccessToken(response);
        this.refreshToken = getRefreshToken(response);
    }

    public Response deleteUser() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        headers.put("Authorization", accessToken);

        return given()
                .headers(headers)
                .and()
                .body("{}")
                .when()
                .delete(URLs.DELETE_USER);
    }

    public Response updateUser(String updateField, String updateValue) {
        String json = String.format("{\"%s\": \"%s\"}", updateField, updateValue);

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("Authorization", accessToken);

        return given()
                .headers(headers)
                .and()
                .body(json)
                .when()
                .patch(URLs.UPDATE_DATA);
    }

    public Response getInfo() {
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("Authorization", accessToken);

        return given()
                .headers(headers)
                .and()
                .when()
                .get(URLs.UPDATE_DATA);
    }
}
