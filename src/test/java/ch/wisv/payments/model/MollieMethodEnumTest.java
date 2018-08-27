package ch.wisv.payments.model;

import junit.framework.TestCase;

public class MollieMethodEnumTest extends TestCase {


    public void testIdealCost() {
        MollieMethodEnum method = MollieMethodEnum.IDEAL;

        assertEquals(10.35, method.calculateCostIncludingTransaction(10));
        assertEquals(0.35, method.calculateCostIncludingTransaction(0));
    }

    public void testBanContactCost() {
//        MollieMethodEnum method = MollieMethodEnum.BANCONTACT;

//        assertEquals(10.48, method.calculateCostIncludingTransaction(10));
//        assertEquals(0.30, method.calculateCostIncludingTransaction(0));
    }

    public void testSofortCost() {
        MollieMethodEnum method = MollieMethodEnum.SOFORT;

        assertEquals(10.41, method.calculateCostIncludingTransaction(10));
        assertEquals(0.30, method.calculateCostIncludingTransaction(0));
    }
}
