package com.hoangvnit.stackoverflow.common;

/**
 * This {@link Enum} class identify for all fragment of the application
 *
 * @author Nguyen Ngoc Hoang (www.hoangvnit.com)
 */
public enum FRAGMENT_ID {
    USER_LIST_FRAGMENT("USER_LIST_FRAGMENT"),
    USER_REPUTATION_FRAGMENT("USER_REPUTATION_FRAGMENT");

    private String key;

    FRAGMENT_ID(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
