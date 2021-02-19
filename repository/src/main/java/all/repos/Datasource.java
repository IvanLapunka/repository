package all.repos;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Datasource {

    private final String password;
    private final String user;
    private final String url;
    private final String driverClass;

    @SneakyThrows
    private Datasource() {
        Properties dbProperties = new Properties();
        dbProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        user = dbProperties.getProperty("user");
        password = dbProperties.getProperty("password");
        url = dbProperties.getProperty("url");
        driverClass = dbProperties.getProperty("driver");
    }

    private static class DatasourceHolder {
        private static final Datasource HOLDER_INSTANCE = new Datasource();
    }

    public static Datasource getInstance() {
        return DatasourceHolder.HOLDER_INSTANCE;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driverClass);
        return DriverManager.getConnection(url, user, password);
    }
}
