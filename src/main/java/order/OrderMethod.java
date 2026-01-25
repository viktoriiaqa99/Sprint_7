package order;

import base.BaseMethod;
import io.restassured.response.ValidatableResponse;

public class OrderMethod extends BaseMethod {

    protected static final String ORDER_URI = BASE_URI + "/orders";

    public ValidatableResponse orderCreate(Order order) {
        return spec()
                .body(order)
                .when()
                .post(ORDER_URI)
                .then().log().all();
    }

    public ValidatableResponse getOrderList() {
        return spec()
                .when()
                .get(ORDER_URI)
                .then().log().all();
    }

    public ValidatableResponse cancelOrder(int track) {
        return spec()
                .when()
                .put(ORDER_URI + "/cancel?track=" + track)
                .then().log().all();
    }
}
