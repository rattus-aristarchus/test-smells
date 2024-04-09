package org.example;

import com.github.javafaker.Faker;

public class TestUser {

    Faker faker = new Faker();

    String username;
    String password;

    TestUser() {
        this.username = faker.name().username();
        this.password = faker.internet().password();
    }
}
