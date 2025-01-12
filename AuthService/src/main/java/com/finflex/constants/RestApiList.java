package com.finflex.constants;

public class RestApiList {
    //api version
    public static final String API = "/api";
    public static final String VERSION = "/v1";


    //crud endpoints
    public static final String USERS = API + VERSION + "/users";
    public static final String GET_ALL_USERS = "/getAllUsers";
    public static final String GET_USER_BY_TCKN = "/getUserByTCKN";
    public static final String GET_USER_BY_USER_NUMBER = "/getUserByUserNo";
    public static final String CREATE_USER = "/createUser";
    public static final String UPDATE_USER   = "/updateUser";
    public static final String DEACTIVATE_USER_BY_TCKN = "/deleteAccountByTCKN";

    //token endpoints
    public static final String TOKENS = API + VERSION + "/tokens";
    public static final String VERIFY_USER_REGISTER = "/verify-mail";
    public static final String CHANGE_USER_PASSWORD = "/change-password-mail";

    //auth endpoints
    public static final String AUTH = API + VERSION + "/auth";
    public static final String LOGIN = "/login";
    public static final String CHANGE_PASSWORD = "/changePass";
    public static final String FORGOT_PASSWORD = "/forgotPass";



}
