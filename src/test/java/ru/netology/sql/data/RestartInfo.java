package ru.netology.sql.data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.DriverManager;

public class RestartInfo {
    @SneakyThrows
    public void renewCode() {
        var runner = new QueryRunner();
        var faker = new Faker();
        var vCode = "SELECT code FROM auth_codes;";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            runner.update(conn, vCode, faker.code());
        }
    }

    @SneakyThrows
    public static void restartDB() {
        var runner = new QueryRunner();
        var dUsers = "DROP DATABASE app";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            runner.update(conn, dUsers);
        }
    }
}
