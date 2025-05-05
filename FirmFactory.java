import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FirmFactory {
    private final int totalFirms;
    private final double pSmall, pMedium, pLarge;
    private static final Random random = new Random();

    public FirmFactory(int totalFirms, double pSmall, double pMedium, double pLarge) {
        this.totalFirms = totalFirms;
        this.pSmall = pSmall;
        this.pMedium = pMedium;
        this.pLarge = pLarge;
    }

    public List<Firm> createFirms() {
        List<Firm> firms = new ArrayList<>();
        int nSmall = (int) (totalFirms * pSmall);
        int nMedium = (int) (totalFirms * pMedium);
        int nLarge = totalFirms - nSmall - nMedium; // Ensure total consistency

        for (int i = 0; i < nSmall; i++) {
            firms.add(createFirm("SMALL"));
        }
        for (int i = 0; i < nMedium; i++) {
            firms.add(createFirm("MEDIUM"));
        }
        for (int i = 0; i < nLarge; i++) {
            firms.add(createFirm("LARGE"));
        }

        return firms;
    }

    private Firm createFirm(String size) {
        double capital, threshold, priceFlex;
        InvestmentStrategy strategy;

        switch (size) {
            case "SMALL" -> {
                capital = 5000 + random.nextDouble() * 10000;
                threshold = 2000 + random.nextDouble() * 1000;
                priceFlex = 0.3;
                strategy = new AggressiveInvestment(); // Ensure this class exists
            }
            case "MEDIUM" -> {
                capital = 20000 + random.nextDouble() * 10000;
                threshold = 5000 + random.nextDouble() * 2000;
                priceFlex = 0.2;
                strategy = new CautiousInvestment(); // Ensure this class exists
            }
            case "LARGE" -> {
                capital = 50000 + random.nextDouble() * 50000;
                threshold = 10000 + random.nextDouble() * 5000;
                priceFlex = 0.1;
                strategy = new OpportunisticInvestment(); // Ensure this class exists
            }
            default -> throw new IllegalArgumentException("Unknown firm size: " + size);
        }

        return new Firm(size, priceFlex, strategy);
    }
}