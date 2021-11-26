package ru.netology.sql.test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import ru.netology.sql.data.DataHelper;
import ru.netology.sql.page.LoginPage;
import ru.netology.sql.page.VerificationPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;


class AuthTest {

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
    void shouldGiveErrorNotificationIfWrongUser() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = loginPage.inValidData();
        loginPage.validLogin(authInfo);

    }

    @Test
    void shouldGiveErrorNotificationIfWrongPassword() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = loginPage.inValidData();
        loginPage.validLogin(authInfo);
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
    void shouldBlockSystemIfWrongVCodeThrice() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var verificationPage = new VerificationPage();
        var authInfo = DataHelper.getValidAuthInfo();
        loginPage.validLogin(authInfo);
        verificationPage.inValidVerify();
        verificationPage.inValidVerify();
        verificationPage.inValidVerify();
        verificationPage.errorNotificationBlocked();
    }

}

