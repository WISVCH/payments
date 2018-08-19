package ch.wisv.payments.model;

import junit.framework.TestCase;

public class MollieMethodEnumTest extends TestCase {


    public void testIdealCost() {
        MollieMethodEnum method = MollieMethodEnum.IDEAL;

        assertEquals(1.35, method.calculateCostIncludingTransaction(1));
        assertEquals(0.35, method.calculateCostIncludingTransaction(0));
    }

    public void testCreditCardCost() {
        MollieMethodEnum method = MollieMethodEnum.CREDIT_CARD;

        assertEquals(1.34, method.calculateCostIncludingTransaction(1));
        assertEquals(0.30, method.calculateCostIncludingTransaction(0));
    }

    public void testSofortCost() {
        MollieMethodEnum method = MollieMethodEnum.SOFORT;

        assertEquals(1.31, method.calculateCostIncludingTransaction(1));
        assertEquals(0.30, method.calculateCostIncludingTransaction(0));
    }

    public void testPaypalCost() {
        MollieMethodEnum method = MollieMethodEnum.PAYPAL;

        assertEquals(1.12, method.calculateCostIncludingTransaction(1));
        assertEquals(0.12, method.calculateCostIncludingTransaction(0));
    }
}
