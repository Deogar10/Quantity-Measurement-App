package com.apps.quantitymeasurement;

public class QuantityMeasurementApp {

    // Inner class to represent Feet measurement
    public static class Feet {
        private final double value;

        // Constructor
        public Feet(double value) {
            this.value = value;
        }

        // Override equals() method
        @Override
        public boolean equals(Object obj) {

            // 1. Reference check
            if (this == obj) {
                return true;
            }

            // 2. Null check
            if (obj == null) {
                return false;
            }

            // 3. Type check
            if (this.getClass() != obj.getClass()) {
                return false;
            }

            // 4. Cast and compare values
            Feet other = (Feet) obj;
            return Double.compare(this.value, other.value) == 0;
        }

        // (Optional but recommended) Override hashCode when equals is overridden
        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }
    }

    // Main method to demonstrate functionality
    public static void main(String[] args) {
        Feet f1 = new Feet(1.0);
        Feet f2 = new Feet(1.0);

        System.out.println("Are equal? " + f1.equals(f2)); // true
    }
}