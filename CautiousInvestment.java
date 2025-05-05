public class CautiousInvestment implements InvestmentStrategy {
    @Override
    public void invest(Firm firm, double interestRate, double inflation) {
        double demandFactor = firm.getResponsiveness();
        double adjustment = (inflation > 10.0) ? 0.9 : 1.0; // Contracts supply under economic uncertainty
        firm.adjustBehavior(demandFactor * adjustment, inflation);
    }
}