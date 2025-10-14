package org.example.spring_kafka.util;

import jakarta.annotation.PostConstruct;

import net.datafaker.Faker;

import org.example.spring_kafka.model.Account;
import org.example.spring_kafka.model.User;

import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelGenerator {
    private Model<Account> accountModel;
    private Model<User> userModel;

    @Autowired
    private Faker faker;

    public Model<Account> getAccountModel() {
        return accountModel;
    }

    public Faker getFaker() {
        return faker;
    }

    public Model<User> getUserModel() {
        return userModel;
    }

    @PostConstruct
    private void init() {
        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getAccounts))
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .toModel();


        accountModel = Instancio.of(Account.class)
                .ignore(Select.field(Account::getId))
                .supply(Select.field(Account::getTitle), () -> faker.app().name())
                .toModel();
    }
}
