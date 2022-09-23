package api.util;

public class URLs {

    public static final String MAIN_PAGE = "https://stellarburgers.nomoreparties.site/";
    public static final String CREATE_USER = "/api/auth/register";  //need body data
    public static final String LOGIN_USER = "/api/auth/login";   // need auth (access) + body data
    public static final String DELETE_USER = "/api/auth/user";  // need auth (access) + body data
    public static final String UPDATE_DATA = "/api/auth/user";  // need body(refresh token)
    public static final String LOGOUT_USER = "/api/auth/logout"; //need body (refresh token)
    public static final String CREATE_ORDER = "/api/orders";
    public static final String GET_INGREDIENTS = "/api/ingredients";
    public static final String GET_ORDERS = "/api/orders"; // need auth

}
