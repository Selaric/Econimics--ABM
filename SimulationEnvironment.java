import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Central environment that manages all aspects of the economic simulation.
 * Coordinates agents, tracks economic indicators, and advances the simulation.
 */

public class SimulationEnvironment {
    private List<Household> households;
    private List<Firm> firms;
    private Government government;
    private MarketStatistics marketStats;
    private int monthsToSimulate;
    private List<Double> inflationHistory = new ArrayList<>();
    private double gdp;
    private double previousGDP;
    private double moneySupply;
    private double employmentRate;
    private SimulationConfig config;
    private int currentMonth;
    private double consumerDemand;
    private double supplyLevel;

    private EconomicIndicators economicIndicators;

    /**
     * Creates a new simulation environment based on configuration file.
     *
     * @param configFile Path to the configuration file
     */
    public SimulationEnvironment(String configFile) {
        this.config = new SimulationConfig(configFile);
        this.monthsToSimulate = config.getMonthsToSimulate();
        this.marketStats = new MarketStatistics();

        // Load macroeconomic parameters
        this.gdp = config.getInitialGDP();
        this.moneySupply = config.getInitialMoneySupply();
        this.employmentRate = config.getInitialEmploymentRate();
        this.previousGDP = this.gdp;
        this.consumerDemand = 1.0; // Initial normalized value
        this.supplyLevel = 1.0; // Initial normalized value

        // Load inflation history with initial value
        inflationHistory.add(config.getInitialInflation());

        // Initialize Government Policy
        PolicyStrategy strategy = new MonetaryPolicyStrategy();
        Government.createInstance(config.getInitialInterestRate(), config.getInflationTarget(), strategy);
        this.government = Government.getInstance();

        // Initialize Economic Indicators
        this.economicIndicators = new EconomicIndicators(
                config.getInitialInflation(), gdp, employmentRate, consumerDemand, supplyLevel
        );

        // Load simulation start month
        this.currentMonth = config.getStartMonth();

        // Initialize Households & Firms
        initializeAgents();
    }

    /**
     * Initializes all economic agents in the simulation.
     */
    private void initializeAgents() {
        HouseholdFactory householdFactory = new HouseholdFactory(
                config.getHouseholdCount(),
                config.getHouseholdAggressiveProb(),
                config.getHouseholdConservativeProb(),
                config.getHouseholdReactiveProb()
        );
        this.households = householdFactory.createHouseholds();

        FirmFactory firmFactory = new FirmFactory(
                config.getFirmCount(),
                config.getFirmSmallProb(),
                config.getFirmMediumProb(),
                config.getFirmLargeProb()
        );
        this.firms = firmFactory.createFirms();
    }

    /**
     * Returns the current economic indicators.
     *
     * @return EconomicIndicators object with current values
     */
    public EconomicIndicators getEconomicIndicators() {
        return economicIndicators;
    }

    /**
     * Returns the list of firms in the simulation.
     *
     * @return List of Firm objects
     */
    public List<Firm> getFirms() {
        return firms;
    }

    /**
     * Returns the government entity managing monetary policy.
     *
     * @return Government singleton instance
     */
    public Government getGovernment() {
        return government;
    }

    /**
     * Returns the current employment rate.
     *
     * @return Employment rate as a percentage
     */
    public double getEmploymentRate() {
        return employmentRate;
    }

    /**
     * Returns the market statistics tracker.
     *
     * @return MarketStatistics object
     */
    public MarketStatistics getMarketStatistics() {
        return marketStats;
    }

    /**
     * Applies lagged effects to inflation calculation to simulate
     * real-world delays in economic responses.
     *
     * @param currentInflation The raw current inflation rate
     * @return Smoothed inflation rate including historical effects
     */
    private double applyLaggedEffect(double currentInflation) {
        if (inflationHistory.size() < 3) return currentInflation;
        return (0.6 * currentInflation) +
                (0.3 * inflationHistory.get(inflationHistory.size() - 2)) +
                (0.1 * inflationHistory.get(inflationHistory.size() - 3));
    }

    /**
     * Advances the simulation by one time period.
     */
    private void advanceTime() {
        currentMonth++;
    }

    /**
     * Updates all economic indicators based on the current state of the simulation.
     */
    private void updateEconomicIndicators() {
        // Calculate smoothed inflation rate
        double rawInflation = inflationHistory.get(inflationHistory.size() - 1);
        double smoothedInflation = applyLaggedEffect(rawInflation);

        // Calculate GDP growth based on firm and household activity
        double gdpGrowthRate = calculateGDPGrowthRate();
        this.previousGDP = this.gdp;
        this.gdp = this.gdp * (1 + gdpGrowthRate);

        // Update employment rate based on GDP change and firm activity
        this.employmentRate = updateEmploymentRate();

        // Update consumer demand and supply based on current conditions
        this.consumerDemand = updateConsumerDemand(smoothedInflation);
        this.supplyLevel = updateSupplyLevel();

        // Update economic indicators with all new values
        this.economicIndicators.updateIndicators(
                smoothedInflation,
                this.gdp,
                this.employmentRate,
                this.consumerDemand,
                this.supplyLevel
        );
    }

    /**
     * Calculates the GDP growth rate based on current economic conditions.
     *
     * @return GDP growth rate as a decimal (e.g., 0.02 for 2%)
     */
    private double calculateGDPGrowthRate() {
        double interestEffect = 1.0 - (government.getInterestRate() / 20.0); // Higher interest rates slow growth
        double inflationRate = inflationHistory.get(inflationHistory.size() - 1);
        double inflationEffect = (inflationRate > 3.0) ? 1.0 - ((inflationRate - 3.0) / 20.0) : 1.0;

        // Base growth rate affected by interest rates and inflation
        double baseGrowthRate = 0.01; // 1% base growth
        double adjustedGrowthRate = baseGrowthRate * interestEffect * inflationEffect;

        // Random economic shocks (both positive and negative)
        double randomShock = (new Random().nextDouble() - 0.5) * 0.01; // -0.5% to +0.5%

        return Math.max(-0.05, Math.min(adjustedGrowthRate + randomShock, 0.1)); // Limit to -5% to 10%
    }

    /**
     * Updates employment rate based on economic conditions.
     *
     * @return Updated employment rate as a percentage
     */
    private double updateEmploymentRate() {
        // Calculate GDP change percentage
        double gdpChangePercent = (gdp - previousGDP) / previousGDP * 100;

        // Employment lags GDP changes slightly
        double employmentChange = gdpChangePercent * 0.5; // 50% of GDP change rate

        // Apply change to current employment rate
        double newRate = employmentRate + (employmentChange / 100);

        // Ensure employment rate stays within realistic bounds (50% to 100%)
        return Math.max(50.0, Math.min(newRate, 100.0));
    }

    /**
     * Updates consumer demand based on inflation, interest rates, and employment.
     *
     * @param inflation Current inflation rate
     * @return Updated consumer demand index (normalized value)
     */
    private double updateConsumerDemand(double inflation) {
        double interestRate = government.getInterestRate();

        // Consumer demand decreases with higher interest rates and inflation
        double interestEffect = Math.max(0.7, 1.0 - (interestRate / 20.0));
        double inflationEffect = Math.max(0.7, 1.0 - (inflation / 15.0));
        double employmentEffect = employmentRate / 100.0;

        // Calculate new demand (normalized to approx 0.5-1.5 range)
        double newDemand = consumerDemand * interestEffect * inflationEffect * employmentEffect;

        // Add random market sentiment
        double sentiment = 0.95 + (new Random().nextDouble() * 0.1); // 0.95 to 1.05
        newDemand *= sentiment;

        // Constrain to reasonable range
        return Math.max(0.5, Math.min(newDemand, 1.5));
    }

    /**
     * Updates supply level based on firm activity and market conditions.
     *
     * @return Updated supply level index (normalized value)
     */
    private double updateSupplyLevel() {
        // Firms respond to changes in demand, but with a lag
        double demandEffect = consumerDemand;

        // Supply adjusts based on interest rates (higher rates = less investment = less supply)
        double interestRate = government.getInterestRate();
        double interestEffect = Math.max(0.8, 1.0 - (interestRate / 25.0));

        // Calculate new supply level
        double newSupply = supplyLevel * 0.9 + (demandEffect * interestEffect * 0.1);

        // Constrain to reasonable range
        return Math.max(0.5, Math.min(newSupply, 1.5));
    }

    /**
     * Runs the complete economic simulation for the configured number of months.
     */
    public void run() {
        // Print simulation initialization summary
        System.out.println("=== ECONOMIC SIMULATION INITIALIZATION ===");
        System.out.printf("• Start Month:           %d%n", currentMonth);
        System.out.printf("• Months to Simulate:    %d%n", monthsToSimulate);
        System.out.printf("• Initial GDP:           $%.2f billion%n", gdp / 1000);
        System.out.printf("• Initial Inflation:     %.2f%%%n", inflationHistory.get(0));
        System.out.printf("• Inflation Target:      %.2f%%%n", config.getInflationTarget());
        System.out.printf("• Initial Interest Rate: %.2f%%%n", government.getInterestRate());
        System.out.printf("• Initial Employment:    %.2f%%%n", employmentRate);
        System.out.printf("• Consumer Demand:       %.2f (index)%n", consumerDemand);
        System.out.printf("• Supply Level:          %.2f (index)%n%n", supplyLevel);

        // Run monthly simulation
        for (int i = 0; i < monthsToSimulate; i++) {
            advanceTime();
            System.out.println("=== Simulation Month " + currentMonth + " ===");

            // Run agents
            for (Household household : households) {
                household.act(this);
            }
            for (Firm firm : firms) {
                firm.act(this);
            }

            System.out.printf("=== MONTH %d ECONOMIC REPORT ===%n", currentMonth);
            System.out.printf("• Inflation Rate:   %.2f%%%n", economicIndicators.getCurrentInflation());
            System.out.printf("• Interest Rate:    %.2f%%%n", government.getInterestRate());
            System.out.printf("• GDP:              $%.2f billions%n", gdp / 1000);
            System.out.printf("• Consumer Demand:  %.2f (index)%n", consumerDemand);
            System.out.printf("• Supply Level:     %.2f (index)%n%n", supplyLevel);

            // Calculate and update state
            double currentInflation = marketStats.calculateInflation(firms);
            inflationHistory.add(currentInflation);
            updateEconomicIndicators();
            government.updatePolicy(economicIndicators);

            System.out.printf("Month %d Summary: Inflation = %.2f%%, Interest Rate = %.2f%%, GDP = %.2f, Employment = %.2f%%%n%n",
                    currentMonth, economicIndicators.getCurrentInflation(), government.getInterestRate(),
                    gdp, employmentRate);
        }
    }



    /**
     * Waits for all concurrent tasks to complete.
     *
     * @param executor The executor service managing the tasks
     */
    private void waitForTasks(ExecutorService executor) {
        try {
            executor.shutdown();
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.err.println("⚠️ WARNING: Forcing task shutdown!");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}