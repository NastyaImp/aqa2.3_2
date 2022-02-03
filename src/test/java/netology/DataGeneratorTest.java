package netology;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.*;

class DataGeneratorTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        val registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        $("[data-test-id=\"login\"] .input__control").sendKeys(registeredUser.getLogin());
        $("[data-test-id=\"password\"] .input__control").sendKeys(registeredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(withText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        val notRegisteredUser = DataGenerator.Registration.getUser("active");
        $("[data-test-id=\"login\"] .input__control").sendKeys(notRegisteredUser.getLogin());
        $("[data-test-id=\"password\"] .input__control").sendKeys(notRegisteredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        System.out.println(notRegisteredUser.getLogin());
        System.out.println(notRegisteredUser.getPassword());
        $("[data-test-id=\"error-notification\"]").shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        val blockedUser = DataGenerator.Registration.getRegisteredUser("blocked");
        $("[data-test-id=\"login\"] .input__control").sendKeys(blockedUser.getLogin());
        $("[data-test-id=\"password\"] .input__control").sendKeys(blockedUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id=\"error-notification\"]").shouldBe(visible)
                .shouldHave(text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        val registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        $("[data-test-id=\"login\"] .input__control").sendKeys(DataGenerator.getRandomLogin());
        $("[data-test-id=\"password\"] .input__control").sendKeys(registeredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id=\"error-notification\"]").shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        val registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        $("[data-test-id=\"login\"] .input__control").sendKeys(registeredUser.getLogin());
        $("[data-test-id=\"password\"] .input__control").sendKeys(DataGenerator.getRandomPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id=\"error-notification\"]").shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }
}