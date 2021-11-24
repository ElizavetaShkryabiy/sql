package ru.netology.sql.data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.DriverManager;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
        private String id;
    }

    @SneakyThrows
    public static AuthInfo getValidAuthInfo(int index) {
        var user = "SELECT id, login, password FROM users ;";
        var runner = new QueryRunner();
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            var first = runner.query(conn, user, new BeanListHandler<>(AuthInfo.class));
            var login = first.get(index).getLogin();
            var pass = first.get(index).getPassword();
            var id = first.get(index).getId();
            return new AuthInfo(login, pass, id);
        }
    }

    @SneakyThrows
    public static AuthInfo getInvalidLogin() {
        var runner = new QueryRunner();
        Faker faker = new Faker();
        var user = "SELECT id,login, password FROM users;";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            var first = runner.query(conn, user, new BeanHandler<>(AuthInfo.class));
            var login = faker.name().username();
            var pass = first.password;
            var id = first.id;
            return new AuthInfo(login, pass, id);
        }
    }

    @SneakyThrows
    public static AuthInfo getInvalidPassword() {
        var runner = new QueryRunner();
        Faker faker = new Faker();
        var user = "SELECT id,login, password FROM users;";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            var first = runner.query(conn, user, new BeanHandler<>(AuthInfo.class));
            var login = first.login;
            var pass = faker.number().toString();
            var id = first.id;
            return new AuthInfo(login, pass, id);
        }
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    @SneakyThrows
    public static VerificationCode getVerificationCode(AuthInfo authInfo) {
        var id = authInfo.id;
        var runner = new QueryRunner();
        var vCode = "SELECT id, code FROM auth_codes WHERE user_id = ? [$id]";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            var authCode = runner.query(conn, vCode, new BeanHandler<>(VerificationCode.class));
            var code = authCode.code;
            return new VerificationCode(code);
        }
    }
}
