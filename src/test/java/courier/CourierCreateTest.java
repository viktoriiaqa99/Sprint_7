package courier;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.apache.http.HttpStatus.*;

public class CourierCreateTest {

    CourierMethod courierMethod = new CourierMethod();
    int courierId;

    @After
    public void cleanup() {
        if (courierId != 0) {
            courierMethod.delete(courierId);
        }
    }

    @Test
    @Description("Курьера можно создать с корректными данными")
    public void createCourierSuccess() {
        Courier courier = createRandomCourier();
        createCourier(courier);
        loginCourier(courier);
    }

    @Test
    @Description("Нельзя создать двух одинаковых курьеров")
    public void createDuplicateCourier() {
        Courier courier = createRandomCourier();
        createCourier(courier);
        loginCourier(courier);
        createDuplicate(courier);
    }

    @Test
    @Description("Нельзя создать курьера без логина")
    public void createCourierWithoutLogin() {
        Courier courier = new Courier(null, "1234", "Ivan");
        createCourierWithoutLoginStep(courier);
    }

    @Test
    @Description("Нельзя создать курьера без пароля")
    public void createCourierWithoutPassword() {
        Courier courier = new Courier("login123", null, "Ivan");
        createCourierWithoutPasswordStep(courier);
    }

    @Step("Создать случайного курьера")
    public Courier createRandomCourier() {
        return CreatedCourier.randomCourier();
    }

    @Step("Отправить запрос на создание курьера")
    public void createCourier(Courier courier) {
        courierMethod.created(courier)
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Step("Авторизоваться курьером")
    public void loginCourier(Courier courier) {
        courierId = courierMethod.login(courier)
                .statusCode(SC_OK)
                .extract()
                .path("id");
    }

    @Step("Попытаться создать дубликат курьера")
    public void createDuplicate(Courier courier) {
        courierMethod.created(courier)
                .statusCode(SC_CONFLICT);
    }

    @Step("Попытаться создать курьера без логина")
    public void createCourierWithoutLoginStep(Courier courier) {
        courierMethod.created(courier)
                .statusCode(SC_BAD_REQUEST);
    }

    @Step("Попытаться создать курьера без пароля")
    public void createCourierWithoutPasswordStep(Courier courier) {
        courierMethod.created(courier)
                .statusCode(SC_BAD_REQUEST);
    }
}
