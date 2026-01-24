package courier;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {

    CourierMethod courierMethod = new CourierMethod();
    int courierId;

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

    @Step("Создать и зарегистрировать курьера")
    public Courier createAndRegisterCourier() {
        Courier courier = CreatedCourier.randomCourier();
        courierMethod.created(courier);
        return courier;
    }

    @Step("Авторизоваться курьером")
    public void loginCourier(Courier courier) {
        courierId = courierMethod.login(courier)
                .statusCode(200)
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
                .statusCode(404);
    }
}
