package com.hoangvnit.stackoverflow.common;

/**
 * Application's constants
 *
 * @author Nguyen Ngoc Hoang (www.hoangvnit.com)
 */
public class Setting {
    public static final String REST_ROOT_URL = "https://api.stackexchange.com/2.2/";

    public static final String GSON_DATE_FORMAT = "dd/MM/yyyy";
    public static final String REQUEST_HEADER_CONTENT_TYPE = "Content-Type";
    public static final String REQUEST_HEADER_CONTENT_TYPE_JSON_VALUE = "application/json";
    public static final String REQUEST_HEADER_ACCEPT_LANGUAGE = "Accept-Language";
    public static final String REQUEST_HEADER_ACCEPT_LANGUAGE_VALUE = "en";

    public static final int OK_HTTP_CLIENT_CACHE_SIZE = 10 * 1024 * 1024;
    public static final int OK_HTTP_CLIENT_TIMEOUT = 30;
    public static final String OK_HTTP_CLIENT_CACHE_FILE_NAME = "responses";

    public static final String IS_FILTER_SOF_USER_KEY = "isFilterSofUser";
    public static final String USER_MODEL_KEY = "userModel";
}
