package com.apps.quantitymeasurement;

public class QuantityMeasurementApp5 {

    // 🔷 ENUM (Base unit = FEET)
    enum LengthUnit {
        FEET(1.0),
        INCHES(1.0 / 12.0),
        YARDS(3.0),
        CENTIMETERS(0.393701 / 12.0);

        private final double factor;

        LengthUnit(double factor) {
            this.factor = factor;
        }

        public double getFactor() {
            return factor;
        }
    }

    // 🔷 Quantity Class (Immutable)
    static class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid numeric value");

            this.value = value;
            this.unit = unit;
        }

        // 🔷 Convert to base (feet)
        private double toBase() {
            return value * unit.getFactor();
        }

        // 🔷 Instance conversion → returns NEW object
        public QuantityLength convertTo(LengthUnit targetUnit) {
            if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");

            double baseValue = toBase();
            double converted = baseValue / targetUnit.getFactor();

            return new QuantityLength(converted, targetUnit);
        }

        // 🔷 Static conversion API (UC5 requirement)
        public static double convert(double value, LengthUnit source, LengthUnit target) {

            if (source == null || target == null)
                throw new IllegalArgumentException("Units cannot be null");

            if (!Double.isFinite(value))
                throw new IllegalArgumentException("Invalid numeric value");

            // Normalize to base → then convert
            double base = value * source.getFactor();
            return base / target.getFactor();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || !(obj instanceof QuantityLength)) return false;

            QuantityLength other = (QuantityLength) obj;

            double epsilon = 1e-6;
            return Math.abs(this.toBase() - other.toBase()) < epsilon;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(toBase());
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    // 🔷 Method Overloading (UC5 concept)

    // Method 1
    public static void demonstrateLengthConversion(double value, LengthUnit from, LengthUnit to) {
        double result = QuantityLength.convert(value, from, to);
        System.out.println("convert(" + value + ", " + from + ", " + to + ") = " + result);
    }

    // Method 2
    public static void demonstrateLengthConversion(QuantityLength q, LengthUnit to) {
        QuantityLength result = q.convertTo(to);
        System.out.println(q + " → " + result);
    }

    // Equality demo
    public static void demonstrateLengthEquality(QuantityLength q1, QuantityLength q2) {
        System.out.println(q1 + " == " + q2 + " : " + q1.equals(q2));
    }

    // Comparison demo
    public static void demonstrateLengthComparison(double v1, LengthUnit u1, double v2, LengthUnit u2) {
        QuantityLength q1 = new QuantityLength(v1, u1);
        QuantityLength q2 = new QuantityLength(v2, u2);
        demonstrateLengthEquality(q1, q2);
    }

    // 🔷 MAIN METHOD (All UC5 Test Scenarios)
    public static void main(String[] args) {

        System.out.println("------ UC5 CONVERSION TESTS ------");

        // ✅ Basic conversions
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);
        demonstrateLengthConversion(24.0, LengthUnit.INCHES, LengthUnit.FEET);
        demonstrateLengthConversion(1.0, LengthUnit.YARDS, LengthUnit.INCHES);
        demonstrateLengthConversion(72.0, LengthUnit.INCHES, LengthUnit.YARDS);
        demonstrateLengthConversion(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCHES);
        demonstrateLengthConversion(6.0, LengthUnit.FEET, LengthUnit.YARDS);

        // ✅ Zero & Negative
        demonstrateLengthConversion(0.0, LengthUnit.FEET, LengthUnit.INCHES);
        demonstrateLengthConversion(-1.0, LengthUnit.FEET, LengthUnit.INCHES);

        // ✅ Instance conversion
        QuantityLength q = new QuantityLength(3.0, LengthUnit.YARDS);
        demonstrateLengthConversion(q, LengthUnit.FEET);

        // ✅ Equality tests
        System.out.println("\n------ EQUALITY TESTS ------");
        demonstrateLengthComparison(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCHES);
        demonstrateLengthComparison(1.0, LengthUnit.YARDS, 3.0, LengthUnit.FEET);
        demonstrateLengthComparison(1.0, LengthUnit.CENTIMETERS, 0.393701, LengthUnit.INCHES);

        // ❌ Invalid cases
        System.out.println("\n------ INVALID TESTS ------");
        try {
            QuantityLength.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCHES);
        } catch (Exception e) {
            System.out.println("NaN test passed: " + e.getMessage());
        }

        try {
            QuantityLength.convert(1.0, null, LengthUnit.FEET);
        } catch (Exception e) {
            System.out.println("Null unit test passed: " + e.getMessage());
        }

        // ✅ Round-trip test
        System.out.println("\n------ ROUND TRIP TEST ------");
        double original = 5.0;
        double converted = QuantityLength.convert(original, LengthUnit.FEET, LengthUnit.INCHES);
        double back = QuantityLength.convert(converted, LengthUnit.INCHES, LengthUnit.FEET);
        System.out.println("Original: " + original + " → Back: " + back);
    }
}