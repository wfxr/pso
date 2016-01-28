/**
 * Data structure to store pso result
 *
 * Created by Wenxuan on 2016/1/13.
 * Email: wenxuan-zhang@outlook.com
 */
public class PSOResult {
    /**
     * @param Position the best position pso algorithm found
     * @param error error of the result
     */
    public PSOResult(double[] Position, double error) {
        this.Position = Position;
        this.Error = error;
    }

    public String toString() {
        String ret = "Error:\n";
        ret += String.format("\tError = %e\n", Error);
        ret += "Position:\n";
        for (int i = 0; i < Position.length; ++i)
            ret += String.format("\tx%d = %f\n", i, Position[i]);
        return ret;
    }

    /**
     * The best position pso algorithm found
     */
    public double[] Position;

    /**
     * Error of the result
     */
    public double Error;
}
