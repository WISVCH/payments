package ch.wisv.payments.model;

import lombok.Getter;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * MollieMethodEnum.
 */
public enum MollieMethodEnum {

    /**
     * Bancontact (Belgium).
     */
    BANCONTACT("bancontact", new ExpressionBuilder("1.01815 * x + 0.3025")),

    /**
     * iDeal (Netherlands)
     */
    IDEAL("ideal", new ExpressionBuilder("x + 0.35")),

    /**
     * SOFORT (Europe).
     */
    SOFORT("sofort", new ExpressionBuilder("1.01089 * x + 0.3025"));

    @Getter
    private final String name;

    @Getter
    private final ExpressionBuilder transactionCost;

    MollieMethodEnum(String name, ExpressionBuilder transactionCost) {
        this.name = name;
        this.transactionCost = transactionCost;
    }

    /**
     * Calculate the cost including transaction cost.
     *
     * @param cost of type double
     *
     * @return double
     */
    public double calculateCostIncludingTransaction(double cost) {
        Expression e = this.getTransactionCost().variables("x")
                .build()
                .setVariable("x", cost);

        return Math.round(e.evaluate() * 100.d) / 100.d;
    }
}
