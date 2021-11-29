package ru.netology.sql.page;

import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import ru.netology.sql.data.DataHelper;

import java.time.Duration;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
  private SelenideElement codeField = $("[data-test-id=code] input");
  private SelenideElement verifyButton = $("[data-test-id=action-verify]");
  static final Faker faker = new Faker(Locale.forLanguageTag("eng"));

//  public VerificationPage() {
//    codeField.shouldBe(visible);
//  }

  public DashboardPage validVerify(DataHelper.VerificationCode code) {
    codeField.setValue(code.getCode());
    verifyButton.click();
    return new DashboardPage();
  }
  public void inValidVerify() {
    codeField.setValue(faker.number().toString());
    verifyButton.click();
  }

  public void errorNotification(){
    $("[data-test-id=error-notification]").shouldBe(appear, Duration.ofSeconds(7))
            .shouldHave(text("Ошибка! Неверно указан код! Попробуйте ещё раз."));
  }
  public void errorNotificationBlocked(){
    $("[data-test-id=error-notification]").shouldBe(appear, Duration.ofSeconds(7))
            .shouldHave(text("Ошибка! Превышено количество попыток ввода кода!"));
  }
}
