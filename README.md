# Economic Simulation System

## Overview

This project is an agent-based economic simulation that models the behavior of households, firms, and government monetary policy to analyze macroeconomic dynamics. The simulation tracks key economic indicators such as inflation, GDP, employment rates, consumer demand, and supply levels over time to demonstrate how different factors and policies affect economic outcomes.

## Features

- **Agent-Based Modeling**: Simulates the behavior of individual economic agents (households and firms) with distinct characteristics and decision-making strategies
- **Dynamic Interest Rate Policy**: Government entity that implements monetary policy by adjusting interest rates in response to inflation
- **Macroeconomic Indicators**: Tracks inflation, GDP, employment rates, consumer demand, and supply levels
- **Strategy Pattern Implementation**: Uses various investment and policy strategies that can be swapped at runtime
- **Configurable Parameters**: Extensive configuration options through external config files
- **Real-world Economic Effects**: Models time lags, market pressures, and economic feedback loops

## Project Structure

### Core Components

- **Agents**: Economic actors that make decisions based on current market conditions
  - `Household`: Consumer entities with varying savings rates and interest rate sensitivities
  - `Firm`: Business entities with different sizes and investment strategies

- **Government**: Singleton entity that implements monetary policy by adjusting interest rates
  - Uses `PolicyStrategy` to determine how to respond to economic conditions

- **Economic Indicators**: Tracks key metrics of the overall economy
  - Inflation, GDP, employment rate, consumer demand, supply level

- **Market Statistics**: Calculates market-wide statistics based on agent behaviors

- **Simulation Environment**: Coordinates all agents and processes, advances time, and produces reports

### Design Patterns

- **Strategy Pattern**: Used for investment strategies and policy decisions
- **Factory Pattern**: Creates households and firms with appropriate characteristics
- **Singleton Pattern**: Ensures a single government entity manages monetary policy

## Configuration

The simulation is configured through a `config.txt` file with parameters including:

```
# Macroeconomic Parameters
initial_gdp=2.0
initial_inflation=2.0
inflation_target=2.0
initial_interest_rate=45.5
start_month=1

# Population and Market Parameters
household_count=100
firm_count=1

# Household Behavior Probabilities (Total = 1.0)
household_aggressive_prob=0.3
household_conservative_prob=0.5
household_reactive_prob=0.2

# Firm Size Probabilities (Total = 1.0)
firm_small_prob=0.4
firm_medium_prob=0.4
firm_large_prob=0.2
```

## Investment Strategies

The system implements three investment strategies that firms can adopt:

1. **Aggressive Investment**: Expands supply in stable economic conditions
2. **Cautious Investment**: Contracts supply when inflation is high
3. **Opportunistic Investment**: Makes decisions with some randomized flexibility

## Running the Simulation

To run the simulation:

1. Ensure you have Java installed on your system
2. Compile all Java files:
   ```
   javac *.java
   ```
3. Run the main class:
   ```
   java Main
   ```

The simulation will:
1. Load configuration parameters
2. Initialize the government, households, and firms
3. Run through the specified number of months
4. Print detailed economic reports for each time period

## Sample Output

The simulation provides detailed economic reports for each simulated month:

```
=== ECONOMIC SIMULATION INITIALIZATION ===
• Start Month:           1
• Months to Simulate:    12
• Initial GDP:           $2.00 billion
• Initial Inflation:     2.00%
• Inflation Target:      2.00%
• Initial Interest Rate: 45.50%
• Initial Employment:    95.00%
• Consumer Demand:       1.00 (index)
• Supply Level:          1.00 (index)

=== Simulation Month 2 ===
=== MONTH 2 ECONOMIC REPORT ===
• Inflation Rate:   2.00%
• Interest Rate:    45.50%
• GDP:              $2.01 billions
• Consumer Demand:  0.89 (index)
• Supply Level:     0.99 (index)

Month 2 Summary: Inflation = 1.94%, Interest Rate = 45.47%, GDP = 2.01, Employment = 95.05%
```

## Extending the Simulation

The system can be extended by:

1. Creating new investment strategies implementing the `InvestmentStrategy` interface
2. Implementing new policy strategies by implementing `PolicyStrategy`
3. Adding new types of economic agents that implement the `Agent` interface
4. Enhancing the `EconomicIndicators` class with additional metrics

## Dependencies

- Java 17 or higher recommended (uses arrow operator for switch expressions)
- No external libraries required
