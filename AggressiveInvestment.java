public class AggressiveInvestment implements InvestmentStrategy {
    @Override
    public void invest(Firm firm, double interestRate, double inflation) {
        double demandFactor = firm.getResponsiveness();
        double expansionRate = (interestRate < 6.0 && inflation < 8.0) ? 1.2 : 1.0; // Expands supply in stable conditions
        firm.adjustBehavior(demandFactor * expansionRate, inflation);
    }
}