package ru.netology.sql.data;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
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

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class AuthInfo {
        String login;
        String password;
        String id;
    }

    @SneakyThrows
    public static AuthInfo getValidAuthInfo(){
        String user = "SELECT id, login, password FROM users;";
        var runner = new QueryRunner();
        DbUtils.loadDriver(JDBC_DRIVER);
        var conn = DriverManager.getConnection(DB_URL, USER, PASS);
        try {
            var resultHandler = runner.query(conn, user, new BeanHandler<>(AuthInfo.class));
            var login = resultHandler.getLogin();
            var pass = "qwerty123";
            var id = resultHandler.getId();
            return new AuthInfo(login, pass, id);
        } finally {
            DbUtils.close(conn);
        }
    }
//
//
//    @SneakyThrows
//    public static AuthInfo getInvalidLogin() {
//        var runner = new QueryRunner();
//        Faker faker = new Faker();
//        var user = "SELECT id,login, password FROM users;";
//        try (
//                var conn = DriverManager.getConnection(DB_URL, USER, PASS);
//        ) {
//            var first = runner.query(conn, user, new BeanHandler<>(AuthInfo.class));
//            var login = faker.name().username();
//            var pass = "qwerty123";
//            var id = first.id;
//            return new AuthInfo(login, pass, id);
//        }
//    }
//
    @SneakyThrows
    public static AuthInfo getInvalidPassword() {
        var runner = new QueryRunner();
        Faker faker = new Faker();
        var user = "SELECT id,login, password FROM users;";
        DbUtils.loadDriver(JDBC_DRIVER);
        var conn = DriverManager.getConnection(DB_URL, USER, PASS);
        try {
            var first = runner.query(conn, user, new BeanHandler<>(AuthInfo.class));
            var login = first.login;
            var pass = faker.number().toString();
            var id = first.id;
            return new AuthInfo(login, pass, id);
        } finally {
            DbUtils.close(conn);
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class VerificationCode {
        private String code;
    }

    @SneakyThrows
    public static VerificationCode getVerificationCode(AuthInfo authInfo) {
        var id = authInfo.getId();
        var runner = new QueryRunner();
        var vCode = "SELECT code FROM auth_codes WHERE user_id = ? AND created < NOW() - INTERVAL 1 SECOND ;";
        try (                var conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            var authCode = runner.query(conn, vCode, new BeanHandler<>(VerificationCode.class), id);
            var code = authCode.getCode();
            return new VerificationCode(code);
        }
    }
}

