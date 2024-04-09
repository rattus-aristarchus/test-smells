package org.example;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UnitTests {

    public String hello(String name) {
        return "Hello " + name + "!";
    }

    //1. Название ни о чем не говорит, сложно будет найти метод поиском
    @Test
    void test() {

    }

    //2. Нет разделения между определением переменных, действием и проверкой
    @Test
    void shouldReturnHelloPhrase() {
        assert(hello("John")).matches("Hello John!");
    }

    //3. Нет переиспользования информации (имени) + непонятные названия переменных
    @Test
    void shouldReturnHelloPhrase1() {
        String a = "John";

        String b = hello("John");

        assert(b)
                .matches("Hello John!");
    }

    //4. Эффект пестицида
    @Test
    void shouldReturnHelloPhrase2() {
        String name = "John";

        String result = hello(name);

        assert(result)
                .contains("Hello " + name + "!");
    }


    //5. Неинформативная ошибка ассерта
    //  java.lang.AssertionError
    //  at io.qameta.allure.IssuesRestTest.shouldReturnHelloPhrase(IssuesRestTest.java:92)
    @Test
    void shouldReturnHelloPhrase3() {
        Faker faker = new Faker();
        String name = faker.name().firstName();

        String result = hello(name);

        assert(result)
                .equals("Hello " + name + "!");
    }

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
