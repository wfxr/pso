import com.google.common.base.Preconditions;

/**
 * An implementation of left-open-right-closed interval
 *
 * Created by Wenxuan on 2016/1/13.
 * Email: wenxuan-zhang@outlook.com
 */
public class Interval {
    /**
     * @param lower lower bound of interval
     * @param upper upper bound of interval
     */
    public Interval(double lower, double upper) {
        Assign(lower, upper);
    }

    /**
     * Test if range contains the value
     *
     * @param value The value to test
     * @return True if contains, false other wise
     */
    public boolean Contains(double value) {
        return value > lower && value <= upper;
    }

    /**
     * Get width of the range
     *
     * @return Width of the range
     */
    public double width() {
        return upper - lower;
    }

    private double lower;
    private double upper;

    /**
     * Get lower bound of the interval
     *
     * @return Lower bound of the interval
     */
    public double getLower() {
        return lower;
    }

    /**
     * Set lower bound of the interval
     *
     * @param lower lower bound to set
     */
    public void setLower(double lower) {
        Preconditions.checkArgument(lower <= this.upper, "lower bound cannot be greater than upper bound");
        this.lower = lower;
    }

    /**
     * Get upper bound of the interval
     *
     * @return upper bound of the interval
     */
    public double getUpper() {
        return upper;
    }

    /**
     * Set upper bound of the interval
     *
     * @param upper upper bound to set
     */
    public void setUpper(double upper) {
        Preconditions.checkArgument(upper >= this.lower, "upper bound cannot be less than lower bound");
        this.upper = upper;
    }

    /**
     * Assign lower and upper bound to the interval
     *
     * @param lower lower bound to assign
     * @param upper upper bound to assign
     */
    public void Assign(double lower, double upper) {
        Preconditions.checkArgument(lower <= upper, "lower bound cannot be greater than upper bound");

        this.lower = lower;
        this.upper = upper;
    }
}
