package com.apps.quantitymeasurement;

public class QuantityMeasurementApp6 {

    // 🔷 ENUM (Base = FEET)
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
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");

            this.value = value;
            this.unit = unit;
        }

        // 🔷 Convert to base (feet)
        private double toBase() {
            return value * unit.getFactor();
        }

        // 🔷 Convert to another unit
        public QuantityLength convertTo(LengthUnit target) {
            if (target == null) throw new IllegalArgumentException("Target unit cannot be null");

            double base = toBase();
            double converted = base / target.getFactor();
            return new QuantityLength(converted, target);
        }

        // 🔷 Static conversion (UC5)
        public static double convert(double value, LengthUnit from, LengthUnit to) {
            if (from == null || to == null) throw new IllegalArgumentException("Units cannot be null");
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");

            double base = value * from.getFactor();
            return base / to.getFactor();
        }

        // 🔷 UC6: ADDITION (Instance Method)
        public QuantityLength add(QuantityLength other) {
            if (other == null) throw new IllegalArgumentException("Second operand cannot be null");

            double sumBase = this.toBase() + other.toBase();
            double result = sumBase / this.unit.getFactor(); // result in THIS unit

            return new QuantityLength(result, this.unit);
        }

        // 🔷 UC6: ADDITION (Static Method with target unit)
        public static QuantityLength add(QuantityLength q1, QuantityLength q2, LengthUnit targetUnit) {
            if (q1 == null || q2 == null)
                throw new IllegalArgumentException("Operands cannot be null");
            if (targetUnit == null)
                throw new IllegalArgumentException("Target unit cannot be null");

            double sumBase = q1.toBase() + q2.toBase();
            double result = sumBase / targetUnit.getFactor();

            return new QuantityLength(result, targetUnit);
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
        public String toString() {
            return value + " " + unit;
        }
    }

    // 🔷 Demo Methods
    public static void demonstrateAddition(QuantityLength q1, QuantityLength q2) {
        System.out.println("Add: " + q1 + " + " + q2 + " = " + q1.add(q2));
    }

    public static void demonstrateAddition(QuantityLength q1, QuantityLength q2, LengthUnit target) {
        System.out.println("Add (target=" + target + "): " +
                QuantityLength.add(q1, q2, target));
    }

    // 🔷 MAIN METHOD (UC6 TESTS)
    public static void main(String[] args) {

        System.out.println("------ UC6 ADDITION TESTS ------");

        QuantityLength f1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength f2 = new QuantityLength(2.0, LengthUnit.FEET);
        QuantityLength i12 = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength y1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength cm = new QuantityLength(2.54, LengthUnit.CENTIMETERS);

        // ✅ Same unit
        demonstrateAddition(f1, f2); // 3 feet

        // ✅ Cross unit
        demonstrateAddition(f1, i12); // 2 feet
        demonstrateAddition(i12, f1); // 24 inches

        // ✅ Yard + feet
        demonstrateAddition(y1, new QuantityLength(3.0, LengthUnit.FEET));

        // ✅ Inches + yard
        demonstrateAddition(new QuantityLength(36.0, LengthUnit.INCHES), y1);

        // ✅ CM + inch
        demonstrateAddition(cm, new QuantityLength(1.0, LengthUnit.INCHES));

        // ✅ Target unit addition
        demonstrateAddition(f1, i12, LengthUnit.INCHES);

        // ✅ Zero
        demonstrateAddition(new QuantityLength(5.0, LengthUnit.FEET),
                new QuantityLength(0.0, LengthUnit.INCHES));

        // ✅ Negative
        demonstrateAddition(new QuantityLength(5.0, LengthUnit.FEET),
                new QuantityLength(-2.0, LengthUnit.FEET));

        // ✅ Commutativity check
        QuantityLength r1 = f1.add(i12);
        QuantityLength r2 = i12.add(f1);
        System.out.println("Commutative: " + r1.equals(r2));

        // ❌ Null case
        try {
            f1.add(null);
        } catch (Exception e) {
            System.out.println("Null test passed: " + e.getMessage());
        }

        // ✅ Large values
        demonstrateAddition(
                new QuantityLength(1e6, LengthUnit.FEET),
                new QuantityLength(1e6, LengthUnit.FEET)
        );

        // ✅ Small values
        demonstrateAddition(
                new QuantityLength(0.001, LengthUnit.FEET),
                new QuantityLength(0.002, LengthUnit.FEET)
        );
    }
}