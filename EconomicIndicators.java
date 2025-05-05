public class EconomicIndicators {
    private double inflationRate;
    private double gdp;
    private double employmentRate;
    private double consumerDemand;
    private double supplyLevel;

    public EconomicIndicators(double inflationRate, double gdp, double employmentRate,
                              double consumerDemand, double supplyLevel) {
        this.inflationRate = inflationRate;
        this.gdp = gdp;
        this.employmentRate = employmentRate;
        this.consumerDemand = consumerDemand;
        this.supplyLevel = supplyLevel;
    }

    // Getters to provide economic data
    public double getCurrentInflation() { return inflationRate; }
    public double getGDP() { return gdp; }
    public double getEmploymentRate() { return employmentRate; }
    public double getConsumerDemand() { return consumerDemand; }
    public double getSupplyLevel() { return supplyLevel; }

    // Method to dynamically update indicators
    public void updateIndicators(double newInflation, double newGDP, double newEmploymentRate,
                                 double newDemand, double newSupply) {
        this.inflationRate = newInflation;
        this.gdp = newGDP;
        this.employmentRate = newEmploymentRate;
        this.consumerDemand = newDemand;
        this.supplyLevel = newSupply;
    }
}