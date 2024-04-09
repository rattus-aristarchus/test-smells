package org.example;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class BadE2ESecondExample {

    Faker faker = new Faker();

    SelenideElement inputUsername = $("#user-name");
    SelenideElement inputPassword = $("#password");
    SelenideElement buttonLogin = $("#login-button");

    @BeforeEach
    public void setUp() {
        openAuthorizationPage();
    }

    // Base url стоит использовать вместо того, чтобы описывать полный адрес страницы
    @Step("Open the login page")
    public void openAuthorizationPage() {
        open("https://www.saucedemo.com");
        inputUsername.shouldBe(Condition.visible);
    }

    // жуткая условная логика, даже если вынести заполнение полей в метод, проще не станет
    @Step
    public void authorize(Boolean username, Boolean password) {
        String trueUsername = "standard_user";
        String truePassword = "secret_sauce";

        if (username && password) {
            inputUsername.setValue(trueUsername);
            inputPassword.setValue(truePassword);
        } else if (username) {
            inputUsername.setValue(trueUsername);
            inputPassword.setValue(faker.internet().password());
        } else if (password) {
            inputUsername.setValue(faker.name().username());
            inputPassword.setValue(truePassword);
        } else {
            inputUsername.setValue(faker.name().username());
            inputPassword.setValue(faker.internet().password());
        }

        buttonLogin.click();
    }

    @Step
    public void checkUserAuthorized() {
        $("[data-test='secondary-header']").shouldBe(Condition.visible);
    }

    // Проверок, чтобы убедиться, что пользователь находится на той же странице, слишком много
    // При этом ни одна не решает проблему ненадежности
    @Step
    public void checkUserNotAuthorized() {
        $(".login_logo").shouldBe(Condition.visible);
        $("#login_button_container").shouldBe(Condition.visible);
        inputUsername.shouldBe(Condition.visible);
        inputPassword.shouldBe(Condition.visible);
    }

    // Не понятно, что делает метод авторизации с true, придется его смотреть отдельно
    // Нет возможности быстро узнать, с какими данными проходит тест
    @Test
    public void shouldAuthorizeUser() {
        authorize(true, true);

        checkUserAuthorized();
    }

    // Одинаковые тесты с разными параметрами в функциях
    @Test
    public void shouldNotAuthorizeUserWithInvalidPassword() {
        authorize(true, false);

        checkUserNotAuthorized();
    }

    @Test
    public void shouldNotAuthorizeUserWithInvalidUsername() {
        authorize(false, true);

        checkUserNotAuthorized();
    }

    // Несколько проверок в одном тесте. Тесты взаимосвязаны и от этого могут флакать
    // Не переиспользуется метод авторизации
    @Test
    public void shouldNotAuthorizeUserWithBlankInputs() {
        inputUsername.setValue(" ");
        inputPassword.setValue(" ");
        buttonLogin.click();

        checkUserNotAuthorized();
    }

    @Test
    public void shouldNotAuthorizeUserWithEmptyInputs() {
        buttonLogin.click();

        checkUserNotAuthorized();
    }
}
