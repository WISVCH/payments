package ch.wisv.payments.model;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MollieMethodEnumTest {

    @Test
    public void testIdealCost() {
        MollieMethodEnum method = MollieMethodEnum.IDEAL;

        assertEquals(10.35, method.calculateCostIncludingTransaction(10), 0.001);
        assertEquals(0.35, method.calculateCostIncludingTransaction(0), 0.001);
    }

    @Test
    public void testSofortCost() {
        MollieMethodEnum method = MollieMethodEnum.SOFORT;

        assertEquals(10.41, method.calculateCostIncludingTransaction(10),0.001);
        assertEquals(0.30, method.calculateCostIncludingTransaction(0), 0.001);
    }
}
