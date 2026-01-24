package courier;

import java.util.UUID;

public class CreatedCourier {

    public static Courier randomCourier() {
        return new Courier(
                "login" + UUID.randomUUID(),
                "password" + UUID.randomUUID(),
                "firstName" + UUID.randomUUID()
        );
    }
}
