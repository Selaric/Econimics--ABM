public class OpportunisticInvestment implements InvestmentStrategy {
    @Override
    public void invest(Firm firm, double interestRate, double inflation) {
        double demandFactor = firm.getResponsiveness();
        double volatilityFactor = Math.random() * 0.2 + 0.9; // Randomized flexibility
        firm.adjustBehavior(demandFactor * volatilityFactor, inflation);
    }
}