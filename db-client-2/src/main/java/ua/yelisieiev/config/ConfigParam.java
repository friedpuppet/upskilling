package ua.yelisieiev.config;

public enum ConfigParam {
    DB_DRIVER("driver"),
    DB_URL("url"),
    DB_LOGIN("login"),
    DB_PASSWORD("password");

    private String name;

    ConfigParam(java.lang.String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
