package order;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    OrderMethod orderMethod = new OrderMethod();
    private final String[] colors;
    private int track;

    public OrderCreateTest(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {null}
        };
    }

    @After
    public void cancelOrder() {
        if (track != 0) {
            orderMethod.cancelOrder(track);
        }
    }

    @Test
    @Description("Создание заказа с параметризацией цвета")
    public void createOrderTest() {
        Order order = createOrder(colors);
        sendCreateOrderRequest(order);
    }

    @Step("Сформировать заказ")
    public Order createOrder(String[] colors) {
        return new Order(
                "Ivan",
                "Ivanov",
                "Moscow",
                "1",
                "+79990001122",
                3,
                "2025-01-25",
                "comment",
                colors
        );
    }

    @Step("Отправить запрос на создание заказа")
    public void sendCreateOrderRequest(Order order) {
        track = orderMethod.orderCreate(order)
                .statusCode(SC_CREATED)
                .body("track", notNullValue())
                .extract()
                .path("track");
    }
}
