package com.apps.quantitymeasurement;

public class QuantityMeasurementApp4 {

    // 🔷 ENUM with all units (base unit = FEET)
    enum LengthUnit {
        FEET(1.0),
        INCHES(1.0 / 12.0),
        YARDS(3.0),
        CENTIMETERS(0.393701 / 12.0); // cm → inches → feet

        private final double conversionFactor;

        LengthUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        public double getConversionFactor() {
            return conversionFactor;
        }
    }

    // 🔷 Generic Quantity Class
    static class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        // Convert to base unit (feet)
        private double toBaseUnit() {
            return value * unit.getConversionFactor();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || !(obj instanceof QuantityLength)) return false;

            QuantityLength other = (QuantityLength) obj;

            return Double.compare(this.toBaseUnit(), other.toBaseUnit()) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(toBaseUnit());
        }
    }

    // 🔷 MAIN METHOD (Demo + Test Cases)
    public static void main(String[] args) {

        System.out.println("------ UC4 TEST RESULTS ------");

        // Basic units
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength yards = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength cm = new QuantityLength(1.0, LengthUnit.CENTIMETERS);

        // ✅ Cross-unit equality
        System.out.println("1 ft == 12 in : " + feet.equals(inches));
        System.out.println("1 yard == 3 ft : " + yards.equals(new QuantityLength(3.0, LengthUnit.FEET)));
        System.out.println("1 yard == 36 in : " + yards.equals(new QuantityLength(36.0, LengthUnit.INCHES)));
        System.out.println("1 cm == 0.393701 in : " + cm.equals(new QuantityLength(0.393701, LengthUnit.INCHES)));

        // ✅ Same unit equality
        System.out.println("2 yard == 2 yard : " +
                new QuantityLength(2.0, LengthUnit.YARDS)
                        .equals(new QuantityLength(2.0, LengthUnit.YARDS)));

        System.out.println("2 cm == 2 cm : " +
                new QuantityLength(2.0, LengthUnit.CENTIMETERS)
                        .equals(new QuantityLength(2.0, LengthUnit.CENTIMETERS)));

        // ❌ Inequality
        System.out.println("1 yard == 2 ft : " +
                yards.equals(new QuantityLength(2.0, LengthUnit.FEET)));

        System.out.println("1 cm == 1 ft : " +
                cm.equals(new QuantityLength(1.0, LengthUnit.FEET)));

        // ✅ Symmetry
        System.out.println("36 in == 1 yard : " +
                new QuantityLength(36.0, LengthUnit.INCHES).equals(yards));

        // ✅ Transitive property
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q2 = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength q3 = new QuantityLength(36.0, LengthUnit.INCHES);

        System.out.println("Transitive (yard=feet & feet=inches → yard=inches): "
                + (q1.equals(q2) && q2.equals(q3) && q1.equals(q3)));

        // ✅ Complex scenario
        System.out.println("2 yard == 6 ft == 72 in : "
                + new QuantityLength(2.0, LengthUnit.YARDS)
                .equals(new QuantityLength(6.0, LengthUnit.FEET))
                + " & "
                + new QuantityLength(6.0, LengthUnit.FEET)
                .equals(new QuantityLength(72.0, LengthUnit.INCHES)));

        // ✅ Reflexive
        System.out.println("q1 == q1 : " + q1.equals(q1));

        // ❌ Null comparison
        System.out.println("q1 == null : " + q1.equals(null));

        // ❌ Invalid unit
        try {
            new QuantityLength(1.0, null);
        } catch (Exception e) {
            System.out.println("Null unit handled: " + e.getMessage());
        }
    }
}
