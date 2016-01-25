/**
 * Created by Wenxuan on 2016/1/14.
 * Email: wenxuan-zhang@outlook.com
 */
public class SolveEquation {
    public static void main(String args[]) {
        Interval[] regionIntervals = new Interval[]{
                new Interval(-10, 10),
                new Interval(-10, 10)
        };
        ProblemDomain domain = new ProblemDomain(regionIntervals, SolveEquation::EvaluationFunction);
        PSO pso = new PSO(domain);
        PSOResult result = pso.Execute(16, 100);
        System.out.print(result);
    }

    public static double EvaluationFunction(double[] input) {
        double x = input[0];
        double y = input[1];

        return Math.pow(2.8125 - x + x * Math.pow(y, 4), 2) +
                Math.pow(2.25 - x + x * Math.pow(y, 2), 2) +
                Math.pow(1.5 - x + x * y, 2);
    }
}
