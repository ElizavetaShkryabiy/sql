package ru.netology.sql.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.DriverManager;

public class RestartInfo {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/app";
    static final String USER = "app";
    static final String PASS = "pass";
    @SneakyThrows
    public static void cleanUp() {

        var dUsers = "DELETE FROM app.users;";
        var dAuthCodes = "DELETE FROM app.auth_codes;";
        var dTransactions = "DELETE FROM app.card_transactions;";
        var dCards = "DELETE FROM app.cards;";
        var runner = new QueryRunner();
        DbUtils.loadDriver(JDBC_DRIVER);
        var conn = DriverManager.getConnection(DB_URL, USER, PASS);
        try {
            runner.execute(conn, dAuthCodes);
            runner.execute(conn, dTransactions);
            runner.execute(conn, dCards);
            runner.execute(conn, dUsers);

        } finally {
            DbUtils.close(conn);
        }
    }
}
