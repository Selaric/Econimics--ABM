import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HouseholdFactory implements AgentFactory {
    private static final Random random = new Random();
    private final double pAggressive, pConservative, pReactive;
    private final int totalHouseholds;

    public HouseholdFactory(int totalHouseholds, double pAggressive, double pConservative, double pReactive) {
        this.totalHouseholds = totalHouseholds;
        this.pAggressive = pAggressive;
        this.pConservative = pConservative;
        this.pReactive = pReactive;
    }

    public List<Household> createHouseholds() {
        List<Household> households = new ArrayList<>();

        int nAgg = (int) (totalHouseholds * pAggressive);
        int nCon = (int) (totalHouseholds * pConservative);
        int nReact = totalHouseholds - nAgg - nCon;

        for (int i = 0; i < nAgg; i++) {
            households.add(createAgent(0.05, 0.4)); // Aggressive: Low savings, high sensitivity
        }
        for (int i = 0; i < nCon; i++) {
            households.add(createAgent(0.3, 0.1)); // Conservative: High savings, low sensitivity
        }
        for (int i = 0; i < nReact; i++) {
            households.add(createAgent(0.15, 0.25)); // Reactive: Moderate savings, medium sensitivity
        }

        return households;
    }

    @Override
    public Household createAgent() {
        return createAgent(0.15, 0.25); // Default household characteristics
    }

    private Household createAgent(double savingsRateBase, double sensitivityBase) {
        double income = generateIncome();
        double savingsRate = savingsRateBase + (0.02 * random.nextDouble());
        double interestSensitivity = sensitivityBase + (0.02 * random.nextDouble());

        return new Household(income, savingsRate, interestSensitivity);
    }

    private double generateIncome() {
        double lowIncomeThreshold = 5000;
        double middleIncomeThreshold = 15000;

        double randomVal = random.nextDouble();
        if (randomVal < 0.3) {
            return 2500 + random.nextDouble() * (lowIncomeThreshold - 2500);
        } else if (randomVal < 0.7) {
            return lowIncomeThreshold + random.nextDouble() * (middleIncomeThreshold - lowIncomeThreshold);
        } else {
            return middleIncomeThreshold + random.nextDouble() * (30000 - middleIncomeThreshold);
        }
    }
}