package utils;

import com.zaxxer.hikari.HikariDataSource;
import script.SqlScripts;

import java.sql.Connection;
import java.sql.SQLException;

public class H2Ds {

    private static HikariDataSource ds = new HikariDataSource();

    static {
        ds.setJdbcUrl("jdbc:h2:mem:test");
        ds.setUsername("sa");
        ds.setPassword("sa");

        ds.setMinimumIdle(20);
        ds.setMaximumPoolSize(100);

        ds.setAutoCommit(false);

        try (final Connection connection = ds.getConnection()) {

            DBUtils.writeToDB(connection, SqlScripts.sDbSchema, pstm -> { });
            DBUtils.writeToDB(connection, SqlScripts.initialData, pstm -> { });

            connection.commit();

        } catch (Throwable ex) {

            throw new RuntimeException(ex);
        }
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private H2Ds() {
      // Private constructor.
    }
}