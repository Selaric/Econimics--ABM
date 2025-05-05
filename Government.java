public class Government {
    private static Government instance; // Singleton instance

    private double currentInflation;
    private double interestRate;
    private double targetInflation;
    private PolicyStrategy policyStrategy;

    // Private constructor ensures Singleton
    private Government(double initialInterestRate, double targetInflation, PolicyStrategy strategy) {
        this.interestRate = initialInterestRate;
        this.targetInflation = targetInflation;
        this.policyStrategy = strategy;
        this.currentInflation = Double.NaN; // Undefined initially
    }

    // Properly initializes the Singleton instance
    public static synchronized Government createInstance(double initialInterestRate, double targetInflation, PolicyStrategy strategy) {
        if (instance == null) {
            instance = new Government(initialInterestRate, targetInflation, strategy);
        }
        return instance;
    }

    public static Government getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Government instance has not been created yet. Use createInstance() first.");
        }
        return instance;
    }

    // Allows resetting the Singleton instance for new simulations (Optional)
    public static void resetInstance() {
        instance = null;
    }

    public void updatePolicy(EconomicIndicators indicators) {
        if (indicators == null) {
            System.err.println("Error: Economic indicators unavailable for policy update.");
            return;
        }

        this.currentInflation = indicators.getCurrentInflation();

        if (policyStrategy != null) {
            this.policyStrategy.adjustInterestRate(this, indicators);
        } else {
            System.err.println("Error: No policy strategy set for interest rate adjustment.");
        }
    }

    public double getCurrentInflation() {
        return Double.isNaN(currentInflation) ? 0.0 : currentInflation;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double newRate) {
        this.interestRate = newRate;
    }

    public double getTargetInflation() {
        return targetInflation;
    }

    public void setPolicyStrategy(PolicyStrategy policyStrategy) {
        if (policyStrategy != null) {
            this.policyStrategy = policyStrategy;
        } else {
            System.err.println("Warning: Attempted to set a null policy strategy.");
        }
    }
}