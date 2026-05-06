import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

/* =========================
   IMeasurable Interface
   ========================= */
interface IMeasurable {
    double getConversionFactor();
    String getUnitName();

    default double convertToBaseUnit(double value) {
        return value * getConversionFactor();
    }

    default double convertFromBaseUnit(double baseValue) {
        return baseValue / getConversionFactor();
    }
}

/* =========================
   VolumeUnit Example (can be extended similarly for LengthUnit, WeightUnit)
   ========================= */
enum VolumeUnit implements IMeasurable {

    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double factor;

    VolumeUnit(double factor) {
        this.factor = factor;
    }

    @Override
    public double getConversionFactor() {
        return factor;
    }

    @Override
    public String getUnitName() {
        return name();
    }
}

/* =========================
   Arithmetic Operation Enum (DRY Core)
   ========================= */
enum ArithmeticOperation {

    ADD((a, b) -> a + b),
    SUBTRACT((a, b) -> a - b),
    DIVIDE((a, b) -> {
        if (b == 0) throw new ArithmeticException("Division by zero");
        return a / b;
    });

    private final DoubleBinaryOperator operation;

    ArithmeticOperation(DoubleBinaryOperator operation) {
        this.operation = operation;
    }

    public double compute(double a, double b) {
        return operation.applyAsDouble(a, b);
    }
}

/* =========================
   Generic Quantity Class (UC13 Refactored)
   ========================= */
class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        if (Double.isNaN(value) || Double.isInfinite(value))
            throw new IllegalArgumentException("Invalid numeric value");
        this.value = value;
        this.unit = unit;
    }

    /* =========================
       CENTRALIZED DRY HELPER
       ========================= */
    private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation op) {
        validate(other);

        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        return op.compute(base1, base2);
    }

    /* =========================
       VALIDATION (CENTRALIZED)
       ========================= */
    private void validate(Quantity<U> other) {
        if (other == null) throw new IllegalArgumentException("Operand cannot be null");

        if (!this.unit.getClass().equals(other.unit.getClass())) {
            throw new IllegalArgumentException("Cross-category operation not allowed");
        }

        if (Double.isNaN(other.value) || Double.isInfinite(other.value)) {
            throw new IllegalArgumentException("Invalid numeric value");
        }
    }

    /* =========================
       ADDITION
       ========================= */
    public Quantity<U> add(Quantity<U> other) {
        double resultBase = performBaseArithmetic(other, ArithmeticOperation.ADD);
        double result = this.unit.convertFromBaseUnit(resultBase);
        return new Quantity<>(round(result), this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        double resultBase = performBaseArithmetic(other, ArithmeticOperation.ADD);
        double result = targetUnit.convertFromBaseUnit(resultBase);
        return new Quantity<>(round(result), targetUnit);
    }

    /* =========================
       SUBTRACTION
       ========================= */
    public Quantity<U> subtract(Quantity<U> other) {
        double resultBase = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
        double result = this.unit.convertFromBaseUnit(resultBase);
        return new Quantity<>(round(result), this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        double resultBase = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
        double result = targetUnit.convertFromBaseUnit(resultBase);
        return new Quantity<>(round(result), targetUnit);
    }

    /* =========================
       DIVISION (dimensionless)
       ========================= */
    public double divide(Quantity<U> other) {
        validate(other);

        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        return ArithmeticOperation.DIVIDE.compute(base1, base2);
    }

    /* =========================
       ROUNDING
       ========================= */
    private double round(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

    /* =========================
       EQUALITY (base unit comparison)
       ========================= */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Quantity<?> other)) return false;

        if (!this.unit.getClass().equals(other.unit.getClass())) return false;

        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        return Math.abs(base1 - base2) < 0.0001;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit.getClass());
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
    }
}

/* =========================
   Demo / Test Runner
   ========================= */
public class QuantityMeasurementApp13 {

    public static void main(String[] args) {

        Quantity<VolumeUnit> q1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> q3 = new Quantity<>(1.0, VolumeUnit.GALLON);

        /* ===== Equality ===== */
        System.out.println(q1.equals(q2)); // true

        /* ===== Conversion via operations ===== */
        System.out.println(q1.add(q2)); // 2 L
        System.out.println(q1.subtract(q2)); // 0 L

        /* ===== Explicit unit ===== */
        System.out.println(q1.add(q2, VolumeUnit.MILLILITRE)); // 2000 mL

        /* ===== Division ===== */
        System.out.println(q1.divide(q2)); // 1.0

        /* ===== Cross-category safety (example compile-time enforced) ===== */
        // Quantity<LengthUnit> invalid = q1; // not allowed
    }
}