package order;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {

    OrderMethod orderMethod = new OrderMethod();

    @Test
    @Description("Получение списка заказов без параметров")
    public void getOrderListTest() {
        getOrders();
    }

    @Step("Получить список заказов")
    public void getOrders() {
        orderMethod.getOrderList()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}
