package ru.netology.sql.data;

import lombok.*;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.DriverManager;

public class DataHelper {

    private DataHelper() {
    }

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/app";
    static final String USER = "app";
    static final String PASS = "pass";

    @Value
    public static class AuthInfo {
        String login;
        String password;
        String id;
    }

    @SneakyThrows
    public static AuthInfo getValidAuthInfo() {
        return new AuthInfo("vasya", "qwerty123", "973ab279-d2d1-4c04-94ea-e6c857f9d919");
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class VerificationCode {
        private String code;
    }

    @SneakyThrows
    public static VerificationCode getVerificationCode(AuthInfo authInfo) {
        var runner = new QueryRunner();
        var vCode = "SELECT code FROM auth_codes INNER JOIN users ON auth_codes.user_id = users.id" +
                " WHERE users.login = ? ORDER BY auth_codes.created DESC LIMIT 1";
        try (var conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            var authCode = runner.query(conn, vCode, new BeanHandler<>(VerificationCode.class), authInfo.getLogin());
            return authCode;
        }
    }
}

