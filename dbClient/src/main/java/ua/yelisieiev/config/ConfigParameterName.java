package ua.yelisieiev.config;

public enum ConfigParameterName {
    DB_DRIVER("driver"),
    DB_URL("url"),
    DB_LOGIN("login"),
    DB_PASSWORD("password");

    private String name;

    ConfigParameterName(java.lang.String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
