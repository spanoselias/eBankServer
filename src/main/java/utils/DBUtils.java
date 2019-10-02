package utils;

import java.sql.*;
import java.util.stream.Stream;

public class DBUtils {

    @FunctionalInterface
    public interface PrepareStatementBinder<T> {
        void accept(final PreparedStatement pstmt) throws SQLException;
    }

    @FunctionalInterface
    public interface ResultSetConsumer<ResultSet, R> {
        R apply(final ResultSet t)throws SQLException;
    }

    public static Long writeToDB(
            final Connection conn,
            final String query,
            final PrepareStatementBinder binder) throws SQLException {

        try (final PreparedStatement pstm = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            binder.accept(pstm);
            pstm.execute();

            final ResultSet generatedKeysRs = pstm.getGeneratedKeys();
            while (generatedKeysRs.next()) {
                return generatedKeysRs.getLong(1);
            }
        }
        return null;
    }

    public static int executeQuery(
            final Connection conn,
            final String query,
            final PrepareStatementBinder binder) throws SQLException {

        try (final PreparedStatement pstm = conn.prepareStatement(query)) {
            binder.accept(pstm);

            return pstm.executeUpdate();
        }
    }

    public static <T> Stream<T> selectQuery(
            final Connection conn,
            final String query,
            final PrepareStatementBinder binder,
            final ResultSetConsumer<ResultSet, T> result) throws SQLException {

        try (final PreparedStatement pstm = conn.prepareStatement(query)) {
            binder.accept(pstm);

            final ResultSet rs = pstm.executeQuery();

            final Stream.Builder<T> steamBuilder = Stream.builder();
            while (rs.next()) {
                final T row = result.apply(rs);
                steamBuilder.add(row);
            }

            return steamBuilder.build();
        }

    }


    public static <T> Stream<T> selectQuery(
            final Connection conn,
            final String query,
            final ResultSetConsumer<ResultSet, T> result) throws SQLException {

        final Statement stmt = conn.createStatement();
        final ResultSet rs = stmt.executeQuery(query);

        final Stream.Builder<T> steamBuilder = Stream.builder();
        while (rs.next()) {
            final T row = result.apply(rs);
            steamBuilder.add(row);
        }

        return steamBuilder.build();
    }
}
