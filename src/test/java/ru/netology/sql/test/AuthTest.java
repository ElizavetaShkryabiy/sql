package ru.netology.sql.test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.netology.sql.data.DataHelper;
import ru.netology.sql.data.RestartInfo;
import ru.netology.sql.page.LoginPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;


class AuthTest {
//    @AfterEach
//    void setUp() {
//        RestartInfo restart = new RestartInfo();
//        restart.renewCode();
//    }
//    @AfterAll
//    void setAllUp(){
//        RestartInfo restart = new RestartInfo();
//        restart.restartDB();
//    }

    @SneakyThrows
    @Test
    void shouldLogInSuccessfully() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getValidAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);

    }

    @Test
    void shouldGiveErrorNotificationIfWrongUser(){
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getInvalidLogin();
        var verificationPage = loginPage.validLogin(authInfo);
        loginPage.errorNotification();

    }

    @Test
    void shouldGiveErrorNotificationIfWrongPassword(){
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getInvalidPassword();
        var verificationPage = loginPage.validLogin(authInfo);
        loginPage.errorNotification();
    }

    @Test
    void shouldGiveErrorNotificationIfWrongVCode() throws SQLException {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getValidAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.inValidVerify();
        verificationPage.errorNotification();
    }

    @Test
    @SneakyThrows
    void shouldBlockSystemIfWrongPasswordThrice() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getInvalidPassword();
        loginPage.validLogin(authInfo);
        loginPage.validLogin(authInfo);
        loginPage.validLogin(authInfo);
        loginPage.errorNotificationBlocked();
    }

}

