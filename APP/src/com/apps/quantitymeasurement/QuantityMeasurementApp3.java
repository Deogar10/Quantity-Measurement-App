package com.apps.quantitymeasurement;

public class QuantityMeasurementApp3 {

    // 🔷 ENUM for Units
    enum LengthUnit {
        FEET(1.0),
        INCHES(1.0 / 12.0);

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

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength q3 = new QuantityLength(2.0, LengthUnit.FEET);
        QuantityLength q4 = new QuantityLength(1.0, LengthUnit.INCHES);

        System.out.println("---- TEST RESULTS ----");

        // Same unit equality
        System.out.println("1 ft == 1 ft : " + q1.equals(new QuantityLength(1.0, LengthUnit.FEET)));

        // Cross unit equality
        System.out.println("1 ft == 12 in : " + q1.equals(q2));

        // Reverse comparison (symmetry)
        System.out.println("12 in == 1 ft : " + q2.equals(q1));

        // Inequality
        System.out.println("1 ft == 2 ft : " + q1.equals(q3));

        // Inch inequality
        System.out.println("1 in == 2 in : " + q4.equals(new QuantityLength(2.0, LengthUnit.INCHES)));

        // Reflexive
        System.out.println("q1 == q1 : " + q1.equals(q1));

        // Null comparison
        System.out.println("q1 == null : " + q1.equals(null));

        // Exception case
        try {
            new QuantityLength(1.0, null);
        } catch (Exception e) {
            System.out.println("Null unit test passed: " + e.getMessage());
        }
    }
}