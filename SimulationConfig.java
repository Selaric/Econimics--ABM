import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SimulationConfig {
    private Properties properties;

    public SimulationConfig(String configFile) {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(configFile)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
        }
    }

    private double getDouble(String key, double defaultValue) {
        try {
            return Double.parseDouble(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            System.err.println("Invalid format for key: " + key);
            return defaultValue;
        }
    }

    private int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            System.err.println("Invalid format for key: " + key);
            return defaultValue;
        }
    }

    public int getMonthsToSimulate() {
        return getInt("months_to_simulate", 12); // Default to 12 if missing
    }

    // Accessor methods for simulation parameters
    public double getInitialGDP() { return getDouble("initial.gdp", 1000.0); }
    public double getInitialMoneySupply() { return getDouble("initial.moneySupply", 500.0); }
    public double getInitialEmploymentRate() { return getDouble("initial.employmentRate", 0.95); }
    public double getInitialInflation() {
        return getDouble("initial.inflation", 2.0); // Default to 2.0% if missing
    }
    public int getStartMonth() { return getInt("start.month", 1); }
    public double getInitialInterestRate() { return getDouble("initial.interestRate", 0.02); }
    public double getInflationTarget() { return getDouble("inflation.target", 0.02); }
    public int getSimulationDuration() { return getInt("simulation.duration", 12); }
    public int getNumHouseholds() { return getInt("agents.households", 1000); }
    public int getNumFirms() { return getInt("agents.firms", 100); }
    public double getConsumerSpendingProbability() { return getDouble("prob.consumerSpending", 0.6); }
    public double getFirmHiringProbability() { return getDouble("prob.firmHiring", 0.5); }
    public double getFirmPriceAdjustmentProbability() { return getDouble("prob.priceAdjustment", 0.3); }

    // Household configuration
    public int getHouseholdCount() { return getNumHouseholds(); }
    public double getHouseholdAggressiveProb() { return getDouble("household.aggressive.prob", 0.3); }
    public double getHouseholdConservativeProb() { return getDouble("household.conservative.prob", 0.4); }
    public double getHouseholdReactiveProb() { return getDouble("household.reactive.prob", 0.3); }

    // Firm configuration
    public int getFirmCount() { return getNumFirms(); }
    public double getFirmSmallProb() { return getDouble("firm.small.prob", 0.5); }
    public double getFirmMediumProb() { return getDouble("firm.medium.prob", 0.3); }
    public double getFirmLargeProb() { return getDouble("firm.large.prob", 0.2); }
}