import java.util.List;

public class Household implements Agent {
    private double income;
    private double savingsRate;
    private double interestSensitivity;

    public Household(double income, double savingsRate, double interestSensitivity) {
        this.income = income;
        this.savingsRate = savingsRate;
        this.interestSensitivity = interestSensitivity;
    }

    @Override
    public void act(SimulationEnvironment env) {
        List<Firm> firms = env.getFirms();
        double interestRate = env.getGovernment().getInterestRate();
        double employmentRate = env.getEmploymentRate();

        // ✅ Get latest inflation rate from EconomicIndicators
        EconomicIndicators indicators = env.getEconomicIndicators(); // Ensure this method exists
        double inflationRate = indicators.getCurrentInflation();

        // ✅ Adjust spending based on inflation sensitivity
        double inflationSensitivityFactor = 1 - (0.5 * (inflationRate / 100)); // More inflation → Lower spending
        inflationSensitivityFactor = Math.max(0.3, inflationSensitivityFactor); // Prevent spending from dropping too low

        double spendingFactor = Math.max(0.5, employmentRate / 100);
        double spending = income * (1 - savingsRate) * (1 - interestSensitivity * (interestRate / 100)) * spendingFactor * inflationSensitivityFactor;
    }
}