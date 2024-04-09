package org.example;

import com.codeborne.selenide.Condition;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class BadE2EFirstExample {

    // У шага нет описания, не понятно, что он делает
    // Нет понимания, что страница уже прогрузилась
    @Step
    public void openAuthorizationPage() {
        open("https://www.saucedemo.com");
    }

    // Элементы не вынесены в поля теста или в пейдж обджекты,
    // что делает невозможным их переиспользование
    // Хардкод данных в методе, который мог бы принимать любые данные
    @Step
    public void authorize() {
        $("#user-name").setValue("standard_user");
        $("#password").setValue("secret_sauce");
        $("#login-button").click();
    }

    // Класс - не самый надежный селектор, лучше использовать айди или дата айди
    @Step
    public void checkUserAuthorized() {
        $(".app_logo").shouldBe(Condition.visible);
    }

    // Ненадежная проверка, так как если страница прогружается медленно,
    // то она может пройти, успев найти логотип на текущей же страницы
    @Step
    public void checkUserNotAuthorized() {
        $(".login_logo").shouldBe(Condition.visible);
    }

    // Тест ничего не проверяет, хотя и не падает
    @Test
    public void shouldAuthorizeUser() {
        openAuthorizationPage();
        authorize();
    }

    // 1. Не используются фикстуры для выноса шага открытия страницы
    // 2. Не переиспользуется метод authorize, хотя можно доработать
    // 3. Данные генерируются в другом классе, но на данный момент это избыточно
    @Test
    public void shouldNotAuthorizeUserWithInvalidPassword() {
        Faker faker = new Faker();
        TestUser user = new TestUser();

        openAuthorizationPage();

        $("#user-name").setValue(user.username);
        $("#password").setValue(faker.internet().password());
        $("#login-button").click();

        checkUserNotAuthorized();
    }

    // Экземпляр класса Faker создается в каждом тесте, хотя достаточно было бы одного
    // Тесты практически одинаковые, было бы лучше сделать один параметризированный
    @Test
    public void shouldNotAuthorizeUserWithInvalidUsername() {
        Faker faker = new Faker();
        TestUser user = new TestUser();

        openAuthorizationPage();

        $("#user-name").setValue(faker.name().username());
        $("#password").setValue(user.password);
        $("#login-button").click();

        checkUserNotAuthorized();
    }

    // Несколько проверок в одном тесте. Тесты взаимосвязаны и от этого могут флакать
    // Проверки повторяются
    @Test
    public void shouldNotAuthorizeUserWithEmptyAndBlankInputs() {
        openAuthorizationPage();

        $("#login-button").click();

        checkUserNotAuthorized();

        $("#user-name").setValue(" ");
        $("#password").setValue(" ");
        $("#login-button").click();

        checkUserNotAuthorized();

        $("#user-name").clear();
        $("#password").clear();
        $("#login-button").click();

        checkUserNotAuthorized();
    }
}
