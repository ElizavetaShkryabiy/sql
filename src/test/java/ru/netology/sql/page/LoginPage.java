package ru.netology.sql.page;

import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import ru.netology.sql.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");
    Faker faker = new Faker();


    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return new VerificationPage();
    }
    public void inValidData() {
        loginField.setValue(faker.name().username());
        passwordField.setValue(faker.number().toString());
        loginButton.click();
        $("[data-test-id=error-notification]").shouldBe(appear, Duration.ofSeconds(7))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }
}
