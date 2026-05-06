
// =======================
// 1. IMeasurable Interface
// =======================
interface IMeasurable {

    double getConversionFactor();

    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    String getUnitName();
}


// =======================
// 2. LengthUnit Enum (UC10)
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

    public double getConversionFactor() {
        return factor;
    }

    public double convertToBaseUnit(double value) {
        return value * factor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / factor;
    }

    public String getUnitName() {
        return name();
    }
}


// =======================
// 3. WeightUnit Enum (UC10)
// =======================
enum WeightUnit implements IMeasurable {

    GRAM(1.0),
    KILOGRAM(1000.0);

    private final double factor;

    WeightUnit(double factor) {
        this.factor = factor;
    }

    public double getConversionFactor() {
        return factor;
    }

    public double convertToBaseUnit(double value) {
        return value * factor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / factor;
    }

    public String getUnitName() {
        return name();
    }
}


// =======================
// 4. VolumeUnit Enum (UC11 NEW)
// =======================
enum VolumeUnit implements IMeasurable {

    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double factor;

    VolumeUnit(double factor) {
        this.factor = factor;
    }

    public double getConversionFactor() {
        return factor;
    }

    public double convertToBaseUnit(double value) {
        return value * factor; // base = litre
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / factor;
    }

    public String getUnitName() {
        return name();
    }
}


// =======================
// 5. Generic Quantity Class (UC10 Core)
// =======================

// =======================
// 6. QuantityMeasurementApp (UC11 Demo)
// =======================
public class QuantityMeasurementApp11 {

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

        // ======================
        // LENGTH (UC10)
        // ======================
        Quantity<LengthUnit> feet = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> inch = new Quantity<>(12, LengthUnit.INCH);

        demonstrateEquality(feet, inch);

        // ======================
        // WEIGHT (UC10)
        // ======================
        Quantity<WeightUnit> kg = new Quantity<>(1, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> gram = new Quantity<>(1000, WeightUnit.GRAM);

        demonstrateEquality(kg, gram);

        // ======================
        // VOLUME (UC11 NEW)
        // ======================
        Quantity<VolumeUnit> litre = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> ml = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> gallon = new Quantity<>(1.0, VolumeUnit.GALLON);

        // Equality
        demonstrateEquality(litre, ml);

        // Conversion
        demonstrateConversion(litre, VolumeUnit.MILLILITRE);
        demonstrateConversion(gallon, VolumeUnit.LITRE);

        // Addition
        demonstrateAddition(litre, ml, VolumeUnit.LITRE);
        demonstrateAddition(litre, gallon, VolumeUnit.GALLON);
    }
}