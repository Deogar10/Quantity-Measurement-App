public static class QuantityMeasurementApp7 {

    public static void main(String[] args) {

        System.out.println(add(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCHES),
                LengthUnit.FEET));

        System.out.println(add(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCHES),
                LengthUnit.INCHES));

        System.out.println(add(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCHES),
                LengthUnit.YARDS));

        System.out.println(add(new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(3.0, LengthUnit.FEET),
                LengthUnit.YARDS));

        System.out.println(add(new QuantityLength(36.0, LengthUnit.INCHES),
                new QuantityLength(1.0, LengthUnit.YARDS),
                LengthUnit.FEET));

        System.out.println(add(new QuantityLength(2.54, LengthUnit.CENTIMETERS),
                new QuantityLength(1.0, LengthUnit.INCHES),
                LengthUnit.CENTIMETERS));

        System.out.println(add(new QuantityLength(5.0, LengthUnit.FEET),
                new QuantityLength(0.0, LengthUnit.INCHES),
                LengthUnit.YARDS));

        System.out.println(add(new QuantityLength(5.0, LengthUnit.FEET),
                new QuantityLength(-2.0, LengthUnit.FEET),
                LengthUnit.INCHES));
    }

    // UC7 add method
    public static QuantityLength add(QuantityLength l1, QuantityLength l2, LengthUnit targetUnit) {
        if (l1 == null || l2 == null || targetUnit == null) {
            throw new IllegalArgumentException("Inputs cannot be null");
        }
        if (!Double.isFinite(l1.getValue()) || !Double.isFinite(l2.getValue())) {
            throw new IllegalArgumentException("Values must be finite");
        }

        double base1 = l1.getUnit().toFeet(l1.value);
        double base2 = l2.unit.toFeet(l2.value);

        double sumFeet = base1 + base2;

        double result = targetUnit.fromFeet(sumFeet);

        return new QuantityLength(result, targetUnit);
    }
}



    @Override
    public String toString() {
        return "Quantity(" + round(value) + ", " + unit + ")";
    }

    private double round(double val) {
        return Math.round(val * 1000.0) / 1000.0; // 3 decimal places
    }

    public boolean convertTo(LengthUnit lengthUnit) {
    }

    public boolean add(QuantityLength quantityLength, LengthUnit lengthUnit) {
    }
}

// LengthUnit enum
enum LengthUnit {

    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(0.0328084);

    private final double toFeetFactor;

    LengthUnit(double toFeetFactor) {
        this.toFeetFactor = toFeetFactor;
    }

    public double toFeet(double value) {
        return value * toFeetFactor;
    }

    public double fromFeet(double feetValue) {
        return feetValue / toFeetFactor;
    }

    public short convertToBaseUnit(double v) {
        return false;
    }

    public double convertFromBaseUnit(double v) {
    }
}