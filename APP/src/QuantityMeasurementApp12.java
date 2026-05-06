
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
// 2. LengthUnit (UC10)
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
// 3. WeightUnit (UC10)
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
// 4. VolumeUnit (UC11)
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
// 5. Generic Quantity Class (UC10 + UC11 + UC12)
// =======================
class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null || Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Invalid value/unit");
        }
        this.value = value;
        this.unit = unit;
    }

    // =======================
    // CONVERSION
    // =======================
    public Quantity<U> convertTo(U targetUnit) {
        double base = unit.convertToBaseUnit(value);
        double result = targetUnit.convertFromBaseUnit(base);
        return new Quantity<>(round(result), targetUnit);
    }

    // =======================
    // ADDITION (UC10)
    // =======================
    public Quantity<U> add(Quantity<U> other) {
        double sum =
                this.unit.convertToBaseUnit(this.value)
                        + other.unit.convertToBaseUnit(other.value);

        return new Quantity<>(round(unit.convertFromBaseUnit(sum)), unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        double sum =
                this.unit.convertToBaseUnit(this.value)
                        + other.unit.convertToBaseUnit(other.value);

        return new Quantity<>(round(targetUnit.convertFromBaseUnit(sum)), targetUnit);
    }

    // =======================
    // SUBTRACTION (UC12)
    // =======================
    public Quantity<U> subtract(Quantity<U> other) {

        double result =
                this.unit.convertToBaseUnit(this.value)
                        - other.unit.convertToBaseUnit(other.value);

        return new Quantity<>(round(unit.convertFromBaseUnit(result)), unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {

        double result =
                this.unit.convertToBaseUnit(this.value)
                        - other.unit.convertToBaseUnit(other.value);

        return new Quantity<>(round(targetUnit.convertFromBaseUnit(result)), targetUnit);
    }

    // =======================
    // DIVISION (UC12)
    // =======================
    public double divide(Quantity<U> other) {

        if (other.value == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }

        double thisBase = this.unit.convertToBaseUnit(this.value);
        double otherBase = other.unit.convertToBaseUnit(other.value);

        return thisBase / otherBase;
    }

    // =======================
    // EQUALITY
    // =======================
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Quantity<?> other)) return false;

        if (!this.unit.getClass().equals(other.unit.getClass()))
            return false;

        double a = this.unit.convertToBaseUnit(this.value);
        double b = ((IMeasurable) other.unit).convertToBaseUnit(other.value);

        return Double.compare(a, b) == 0;
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}


// =======================
// 6. QuantityMeasurementApp (UC12 Demo)
// =======================
public class QuantityMeasurementApp12 {

    public static <U extends IMeasurable> void eq(Quantity<U> a, Quantity<U> b) {
        System.out.println("Equality: " + a.equals(b));
    }

    public static <U extends IMeasurable> void conv(Quantity<U> a, U u) {
        System.out.println("Conversion: " + a.convertTo(u));
    }

    public static <U extends IMeasurable> void add(Quantity<U> a, Quantity<U> b, U u) {
        System.out.println("Addition: " + a.add(b, u));
    }

    public static <U extends IMeasurable> void sub(Quantity<U> a, Quantity<U> b, U u) {
        System.out.println("Subtraction: " + a.subtract(b, u));
    }

    public static <U extends IMeasurable> void div(Quantity<U> a, Quantity<U> b) {
        System.out.println("Division: " + a.divide(b));
    }

    public static void main(String[] args) {

        // =======================
        // LENGTH
        // =======================
        Quantity<LengthUnit> feet = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> inch = new Quantity<>(120, LengthUnit.INCH);

        eq(feet, inch);
        sub(feet, inch, LengthUnit.FEET);
        div(feet, inch);

        // =======================
        // WEIGHT
        // =======================
        Quantity<WeightUnit> kg = new Quantity<>(10, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> gram = new Quantity<>(5000, WeightUnit.GRAM);

        sub(kg, gram, WeightUnit.KILOGRAM);
        div(kg, gram);

        // =======================
        // VOLUME
        // =======================
        Quantity<VolumeUnit> l = new Quantity<>(5, VolumeUnit.LITRE);
        Quantity<VolumeUnit> ml = new Quantity<>(500, VolumeUnit.MILLILITRE);

        sub(l, ml, VolumeUnit.LITRE);
        div(l, ml);

        // ADDITION STILL WORKS (UC10/11)
        add(l, new Quantity<>(1, VolumeUnit.LITRE), VolumeUnit.LITRE);
    }
}