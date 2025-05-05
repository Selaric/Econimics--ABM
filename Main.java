public class Main {
    public static void main(String[] args) {
        try {
            // Load configuration file dynamically
            String configFile = "src/config.txt";
            SimulationConfig config = new SimulationConfig(configFile);

            // Initialize government policy using createInstance()
            Government.createInstance(
                    config.getInitialInterestRate(),
                    config.getInflationTarget(),
                    new MonetaryPolicyStrategy()
            );

            // Create simulation environment with config file input
            SimulationEnvironment sim = new SimulationEnvironment(configFile);
            sim.run(); // Execute simulation

        } catch (Exception e) {
            System.err.println("Simulation failed: " + e.getMessage()); // Print error message instead of failing silently
        }
    }
}