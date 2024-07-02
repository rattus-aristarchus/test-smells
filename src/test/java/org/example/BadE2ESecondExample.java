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

    // Instead of writing the full address, it's better to use the baseUrl property
    @Step("Open the login page")
    public void openAuthorizationPage() {
        open("https://www.saucedemo.com");
        inputUsername.shouldBe(Condition.visible);
    }

    // A maze of conditional logic. Even if we hide filling in the credentials, this
    // won't look much better.
    @Step
    public void authorize(Boolean username, Boolean password) {
        String trueUsername = "standard_user";
        String truePassword = "secret_sauce";

        if (username && password) {
            inputUsername.setValue(trueUsername);
            inputPassword.setValue(truePassword);
            buttonLogin.click();

            checkUserAuthorized();
        } else if (username) {
            inputUsername.setValue(trueUsername);
            inputPassword.setValue(faker.internet().password());
            buttonLogin.click();

            checkUserNotAuthorized();
        } else if (password) {
            inputUsername.setValue(faker.name().username());
            inputPassword.setValue(truePassword);
            buttonLogin.click();

            checkUserNotAuthorized();
        } else {
            inputUsername.setValue(faker.name().username());
            inputPassword.setValue(faker.internet().password());
            buttonLogin.click();

            checkUserNotAuthorized();
        }
    }

    @Step
    public void checkUserAuthorized() {
        $("[data-test='secondary-header']").shouldBe(Condition.visible);
    }

    // Too many checks - and none of them are definitive
    @Step
    public void checkUserNotAuthorized() {
        $(".login_logo").shouldBe(Condition.visible);
        $("#login_button_container").shouldBe(Condition.visible);
        inputUsername.shouldBe(Condition.visible);
        inputPassword.shouldBe(Condition.visible);
    }

    // It's not clear what "true" and "false" mean here, you
    // have to look up the method.
    // It' not clear what data the test is using.
    @Test
    public void shouldAuthorizeUser() {
        authorize(true, true);

        checkUserAuthorized();
    }

    // This is the exact same test with different parameters.
    @Test
    public void shouldNotAuthorizeUserWithInvalidPassword() {
        authorize(true, false);

        checkUserNotAuthorized();
    }

    // Same here.
    @Test
    public void shouldNotAuthorizeUserWithInvalidUsername() {
        authorize(false, true);

        checkUserNotAuthorized();
    }

    // The authorization method is not used.
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
