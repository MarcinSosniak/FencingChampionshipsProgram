package model.KillerDrawing;

public class KillerRandomizerStrategyPicker {
    public static KillerRandomizerStrategy KillerRandomizerStrategy()
    {
        return new RandomKillerRandomizationStrategy();
    }
}
