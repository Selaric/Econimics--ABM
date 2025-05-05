public class Firm implements Agent {
    private String size;
    private double responsiveness;
    private InvestmentStrategy strategy;

    public Firm(String size, double responsiveness, InvestmentStrategy strategy) {
        this.size = size;
        this.responsiveness = responsiveness;
        this.strategy = strategy;
    }

    public double getResponsiveness() {
        return responsiveness;
    }

    @Override
    public void act(SimulationEnvironment env) {
        double interestRate = env.getGovernment().getInterestRate();
        double inflation = env.getGovernment().getCurrentInflation();
        double demandFactor = env.getEconomicIndicators().getConsumerDemand();

        adjustBehavior(demandFactor, inflation);
        strategy.invest(this, interestRate, inflation); // Firms invest dynamically
    }

    public void adjustBehavior(double demandFactor, double inflation) {
        double marketPressure = demandFactor - inflation * 0.01;
        responsiveness = Math.max(0.75, Math.min(1.25, responsiveness * (1.0 + marketPressure)));
    }

    public String getSize() { return size; }


}