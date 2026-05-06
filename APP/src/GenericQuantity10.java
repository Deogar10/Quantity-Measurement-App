// =======================
// 1. IMeasurable Interface
// =======================

public interface IMeasurable {

    double getConversionFactor();

    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    String getUnitName();
}


// =======================
// 2. LengthUnit Enum
// =======================

enum LengthUnit implements IMeasurable {

    INCH(1.0),
    FEET(12.0),
    YARD(36.0),
    CENTIMETER(0.393700787);

    private final double factor;

    LengthUnit(double factor) {
        this.factor = factor;
    }

    @Override
    public double getConversionFactor() {
        return factor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * factor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / factor;
    }

    @Override
    public String getUnitName() {
        return name();
    }
}



// =======================
// 4. Ge
// =======================
// 5. QuantityMeasurementApp
// =======================

public class GenericQuantity10{

    public static <U extends IMeasurable> void demonstrateEquality(
            Quantity<U> q1, Quantity<U> q2) {
        System.out.println("Equality: " + q1.equals(q2));
    }

    public static <U extends IMeasurable> void demonstrateConversion(
            Quantity<U> q, U target) {
        System.out.println("Conversion: " + q.convertTo(target));
    }

    public static <U extends IMeasurable> void demonstrateAddition(
            Quantity<U> q1, Quantity<U> q2, U target) {
        System.out.println("Addition: " + q1.add(q2, target));
    }

    public static void main(String[] args) {

        // ===== LENGTH =====
        Quantity<LengthUnit> feet = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> inch = new Quantity<>(12, LengthUnit.INCH);

        demonstrateEquality(feet, inch);
        demonstrateConversion(feet, LengthUnit.INCH);
        demonstrateAddition(feet, inch, LengthUnit.FEET);

        // ===== WEIGHT =====
        Quantity<WeightUnit> kg = new Quantity<>(1, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> gram = new Quantity<>(1000, WeightUnit.GRAM);

        demonstrateEquality(kg, gram);
        demonstrateConversion(kg, WeightUnit.GRAM);
        demonstrateAddition(kg, gram, WeightUnit.KILOGRAM);
    }
}