package ru.netology.sql.test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.sql.data.DataHelper;
import ru.netology.sql.data.RestartInfo;
import ru.netology.sql.page.LoginPage;
import ru.netology.sql.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;


class AuthTest {


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
       loginPage.inValidData();

    }

    @Test
    void shouldGiveErrorNotificationIfWrongPassword() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        loginPage.inValidData();
    }

    @Test
    void shouldGiveErrorNotificationIfWrongVCode()  {
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
    @AfterAll
    public static void cleanUp(){
        RestartInfo.cleanUp();
    }
}

