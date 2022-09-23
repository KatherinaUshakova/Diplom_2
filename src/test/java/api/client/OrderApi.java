package api.client;

import api.util.URLs;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import api.model.Ingredient;
import api.model.IngredientsList;
import api.model.OrderIngredients;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderApi {
    public String[] ingredientsIds;

    @Step ("Получение ингредиентов")
    public Response getIngredients() {
        return given()
                .get(URLs.GET_INGREDIENTS);
    }

    @Step ("Создание заказа")
    public Response createOrder(OrderIngredients order, String accessToken) {

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("Authorization", accessToken);

        return given()
                .headers(headers)
                .and()
                .body(order)
                .when()
                .post(URLs.CREATE_ORDER);
    }
    @Step ("Получение хешей ингредиентов")
    public String[] getIngredientIds() {
        IngredientsList body = getIngredients().body().as(IngredientsList.class);

        List<Ingredient> list = body.getIngredients();
        this.ingredientsIds = new String[15];

        for (Ingredient ingredient: list) {
            this.ingredientsIds[list.indexOf(ingredient)] = ingredient.get_id();
        }

        return this.ingredientsIds;
    }

    public static int rnd(int min, int max) {
        max -= min;

        return (int) (Math.random() * ++max) + min;
    }

    @Step ("выбор ингредиентов")
    public OrderIngredients makeChoice() {
        return new OrderIngredients(new String[]{getIngredientIds()[rnd(0, 14)], getIngredientIds()[rnd(0, 14)]});
    }

    @Step ("Получение заказов")
    public Response getOrders(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .and()
                .get(URLs.GET_ORDERS);
    }

}
