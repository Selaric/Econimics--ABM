import java.util.List;

public class MarketStatistics {
    private double lastMarketPressure = 1.0; // Economic pressure index instead of price
    private final double smoothingFactor = 0.8; // Stabilizes market condition fluctuations

    public double calculateInflation(List<Firm> firms) {
        if (firms.isEmpty()) return 0.0; // Prevent division by zero

        double totalResponsiveness = 0;
        double weightedResponsivenessTotal = 0;

        for (Firm f : firms) {
            double responsiveness = f.getResponsiveness(); // ✅ Ensure method exists first
            totalResponsiveness += responsiveness;
            weightedResponsivenessTotal += responsiveness * responsiveness; // Self-weighting effect
        }

        double marketPressure = (totalResponsiveness > 0) ? (weightedResponsivenessTotal / totalResponsiveness) : 1.0;
        double inflation = (lastMarketPressure > 0) ? ((marketPressure - lastMarketPressure) / lastMarketPressure) * 100 : 0;

        // Apply smoothing to stabilize inflation calculations
        lastMarketPressure = (smoothingFactor * lastMarketPressure) + ((1 - smoothingFactor) * marketPressure);

        return Math.max(-5.0, Math.min(inflation, 15.0)); // Prevent extreme inflation spikes
    }
}