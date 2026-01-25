package courier;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {

    CourierMethod courierMethod = new CourierMethod();
    Courier courier;
    int courierId;

    @Before
    public void setUp() {
        courier = CreatedCourier.randomCourier();
        courierMethod.created(courier);
    }

    @After
    public void cleanup() {
        if (courierId != 0) {
            courierMethod.delete(courierId);
        }
    }

    @Test
    @Description("Курьер может авторизоваться с корректными данными")
    public void loginSuccess() {
        Courier courier = createAndRegisterCourier();
        loginCourier(courier);
    }

    @Test
    @Description("Ошибка при неверном пароле")
    public void loginWrongPassword() {
        Courier courier = createAndRegisterCourier();
        loginWithWrongPassword(courier);
    }

    @Test
    @Description("Ошибка при неверном логине")
    public void loginWrongLogin() {
        Courier wrongLogin = new Courier(
                "wrongLogin",
                courier.getPassword(),
                courier.getFirstName()
        );

        courierMethod.login(wrongLogin)
                .statusCode(SC_NOT_FOUND);
    }

    @Step("Создать и зарегистрировать курьера")
    public Courier createAndRegisterCourier() {
        Courier courier = CreatedCourier.randomCourier();
        courierMethod.created(courier);
        return courier;
    }

    @Step("Авторизоваться курьером")
    public void loginCourier(Courier courier) {
        courierId = courierMethod.login(courier)
                .statusCode(SC_OK)
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    @Step("Авторизация с неверным паролем")
    public void loginWithWrongPassword(Courier courier) {
        Courier wrongPassword = new Courier(
                courier.getLogin(),
                "wrong",
                courier.getFirstName()
        );

        courierMethod.login(wrongPassword)
                .statusCode(SC_NOT_FOUND);
    }
}
