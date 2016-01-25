/**
 * Created by Wenxuan on 2016/1/13.
 * Email: wenxuan-zhang@outlook.com
 */
public class Interval {
    public Interval(double lower, double upper) {
        if (upper < lower)
            throw new IllegalArgumentException("upper bound cannot be less than lower bound");

        this.upper = upper;
        this.lower = lower;
    }

    public boolean Contains(double value) {
        return value > lower && value <= upper;
    }

    public double width() {
        return upper - lower;
    }

    public double upper;
    public double lower;
}
