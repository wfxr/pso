public class Interval {
    public Interval(double lower, double upper) {
        if (upper < lower)
            throw new IllegalArgumentException("upper bound cannot be less than lower bound");

        this.upper = upper;
        this.lower = lower;
    }

    public boolean Inside(double value) {
        return value > lower && value <= upper;
    }

    public double Restrain(double value) {
        if (value < lower)
            return lower;
        if (value > upper)
            return upper;
        return value;
    }

    public double width() {
        return upper - lower;
    }

    public double upper;
    public double lower;
}
