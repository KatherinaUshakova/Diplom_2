import DataForTests.URLs;
import io.restassured.response.Response;
import pojo.Ingredient;
import pojo.IngredientsList;
import pojo.OrderIngredients;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderApi {
    public String[] ingredientsIds;

    public Response getIngredients() {
        return given()
                .get(URLs.GET_INGREDIENTS);
    }

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

    public OrderIngredients makeChoice() {
        return new OrderIngredients(new String[]{getIngredientIds()[rnd(0, 14)], getIngredientIds()[rnd(0, 14)]});
    }

    public Response getOrders(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .and()
                .get(URLs.GET_ORDERS);
    }

}
