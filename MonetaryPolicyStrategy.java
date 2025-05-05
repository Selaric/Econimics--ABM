public class MonetaryPolicyStrategy implements PolicyStrategy {
    private final double scalingFactor = 0.5; // Determines how aggressively we adjust rates

    @Override
    public void adjustInterestRate(Government government, EconomicIndicators indicators) {
        double actualInflation = indicators.getCurrentInflation();
        double targetInflation = government.getTargetInflation();
        double interestRate = government.getInterestRate();

        // Calculate proportional change
        double inflationGap = actualInflation - targetInflation;
        double interestAdjustment = scalingFactor * inflationGap;

        // Update interest rate
        interestRate += interestAdjustment;
        government.setInterestRate(interestRate);

        System.out.printf("Monetary policy applied. Inflation gap: %.2f | Adjustment: %.2f | New Interest Rate: %.2f%n",
                inflationGap, interestAdjustment, interestRate);
    }
}