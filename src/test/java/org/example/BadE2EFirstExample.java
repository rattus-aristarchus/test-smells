package org.example;

import com.codeborne.selenide.Condition;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class BadE2EFirstExample {

    // The step has no description, we don't know what it's doing.
    // We can't be sure that the page is loaded
    @Step
    public void openAuthorizationPage() {
        open("https://www.saucedemo.com");
    }

    // Elements haven't been put inside variables or page objects,
    // which means we can't reuse them.
    // Data is hard-coded, while it could be accepted from the outside.
    @Step
    public void authorize() {
        $("#user-name").setValue("standard_user");
        $("#password").setValue("secret_sauce");
        $("#login-button").click();
    }

    // Selectors are based on classes and not IDs
    @Step
    public void checkUserAuthorized() {
        $(".app_logo").shouldBe(Condition.visible);
    }

    // Unsafe check: if the page takes a while to load,
    // the check will pass, because the logo is present
    // on the previous page
    @Step
    public void checkUserNotAuthorized() {
        $(".login_logo").shouldBe(Condition.visible);
    }

    // The test doesn't test anything and never fails
    @Test
    public void shouldAuthorizeUser() {
        openAuthorizationPage();
        authorize();
    }

    // 1. Opening the main page isn't put into a fixture
    // 2. The 'authorize()' method isn't reused
    // 3. Data is generated in a separate class, which currently is overkill
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

    // An instance of the Faker class is created in each test,
    // although just one would have sufficed.
    // Tests are pretty much identical; they should be parameterized.
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

    // A single test has multiple checks. The tests depend on each other,
    // which may cause flakiness.
    // The checks are identical.
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
