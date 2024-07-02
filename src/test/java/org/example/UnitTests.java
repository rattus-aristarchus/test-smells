package org.example;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UnitTests {

    public String hello(String name) {
        return "Hello " + name + "!";
    }

    // 1. The name isn't telling us anything, you won't be able to do a search for the method
    @Test
    void test() {

    }

    // 2. Arrange, act, and assert are not separated
    @Test
    void shouldReturnHelloPhrase() {
        assert(hello("John")).matches("Hello John!");
    }

    // 3. Data (the name) is hard-coded and not reused; variable names are uninformative
    @Test
    void shouldReturnHelloPhrase1() {
        String a = "John";

        String b = hello("John");

        assert(b)
                .matches("Hello John!");
    }

    // 4. Pesticide effect
    @Test
    void shouldReturnHelloPhrase2() {
        String name = "John";

        String result = hello(name);

        assert(result)
                .contains("Hello " + name + "!");
    }


    // 5. Vague error message:
    //  java.lang.AssertionError
    //  at io.qameta.allure.IssuesRestTest.shouldReturnHelloPhrase(UnitTests.java:58)
    @Test
    void shouldReturnHelloPhrase3() {
        Faker faker = new Faker();
        String name = faker.name().firstName();

        String result = hello(name);

        assert(result)
                .equals("Hello " + name + "!");
    }

    // 6. The new error message looks like this:
    //expected: <Hello Mauro!> but was: <Hello Mauro>
    //    Expected :Hello Mauro!
    //    Actual   :Hello Mauro
    @Test
    void shouldReturnHelloPhrase4() {
        Faker faker = new Faker();
        String name = faker.name().firstName();

        String result = hello(name);

        Assertions.assertEquals(
                result,
                "Hello " + name + ""
        );
    }
}
