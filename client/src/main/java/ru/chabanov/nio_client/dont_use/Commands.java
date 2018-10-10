package ru.chabanov.nio_client.dont_use;
public enum Commands {
    LOGIN("LOGGED"),
    SEND("SEND"),
    LOGOUT("UNLOGGED");

    private final String message;

    Commands(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}