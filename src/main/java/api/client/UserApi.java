package api.client;

import api.util.URLs;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import api.model.User;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserApi {


    String accessToken;
    String refreshToken;

    @Step ("Создаем пользователя")
    public Response createUser(String email, String password, String name) {

        User data = new User(email, password, name);

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(data)
                .when()
                .post(URLs.CREATE_USER);
    }

    @Step ("Создаем пользователя успешно")
    public Response createUserSuccessfully() {
        return createUser(api.util.User.EMAIL, api.util.User.PASSWORD, api.util.User.NAME);
    }
    @Step ("Получить аксесс токен")
    public String getAccessToken(Response response) {
        return response.then().extract().path("accessToken");
    }

    @Step ("Получить рефреш токен")
    public String getRefreshToken(Response response) {
        return response.then().extract().path("refreshToken");
    }

    @Step ("логиним юзера")
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
    @Step ("Успешно логиним юзера")
    public Response loginUserSuccessfully() {
        return loginUser(api.util.User.EMAIL, api.util.User.PASSWORD, this.accessToken);
    }

    public void saveTokens(Response response) {
        this.accessToken = getAccessToken(response);
        this.refreshToken = getRefreshToken(response);
    }

    @Step ("удаляем пользователя")
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
    @Step ("Обновить информацию о пользователе")
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
    @Step ("Получение информации")
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
