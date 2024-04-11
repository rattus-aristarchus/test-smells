package org.example;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;

public class GoodE2E {

    static Faker faker = new Faker();

    SelenideElement inputUsername = $("#user-name");
    SelenideElement inputPassword = $("#password");
    SelenideElement buttonLogin = $("#login-button");

    static String trueUsername = "standard_user";
    static String truePassword = "secret_sauce";

    @BeforeEach
    public void setUp() {
        Configuration.baseUrl = "https://www.saucedemo.com";
        openAuthorizationPage();
    }

    @Step("Open the login page")
    public void openAuthorizationPage() {
        open("");
        inputUsername.shouldBe(Condition.visible);
    }

    @Step("Authorize with credentials: {0}/{1}")
    public void authorize(String username, String password) {
        inputUsername.setValue(username);
        inputPassword.setValue(password);

        buttonLogin.click();
    }

    @Step("Check the product page is opened")
    public void checkUserAuthorized() {
        webdriver().shouldHave(url(Configuration.baseUrl + "/inventory.html"));
        $("[data-test='secondary-header']").shouldBe(Condition.visible);
    }

    @Step("Check the user wasn't redirected to the products page")
    public void checkUserNotAuthorized() {
        webdriver().shouldHave(url(Configuration.baseUrl + "/"));
        $("#login_button_container").shouldBe(Condition.visible);
    }

    @Test
    public void shouldAuthorizeUserWithValidCredentials() {
        authorize(trueUsername, truePassword);

        checkUserAuthorized();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidCredentials")
    @DisplayName("User can't authorize with ")
    public void shouldNotAuthorizeUserWithInvalidCredentials(String username, String password) {
        authorize(username, password);

        checkUserNotAuthorized();
    }

    @Test
    public void shouldNotAuthorizeUserWithEmptyInputs() {
        buttonLogin.click();

        checkUserNotAuthorized();
    }

    private static Stream<Arguments> invalidCredentials() {
        return Stream.of(
                Arguments.of("invalid password", trueUsername, faker.internet().password()),
                Arguments.of("invalid username", faker.name().username(), truePassword),
                Arguments.of("blank fields", " ", " ")
        );
    }
}
