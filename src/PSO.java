import java.util.*;

/**
 * Created by Wenxuan on 2016/1/13.
 * Email: wenxuan-zhang@outlook.com
 */
public class PSO {
    private int D;
    private double C1 = 2.0;
    private double C2 = 2.0;
    private Interval W = new Interval(0.0, 1.0);
    private ProblemDomain problemDomain;

    private Random rand = new Random();

    public PSO(ProblemDomain problemProblemDomain) {
        this.problemDomain = problemProblemDomain;
        this.D = this.problemDomain.D;
    }

    public PSOResult Execute(int swarmCount, int max_iter) {
        Particle[] swarm = problemDomain.GenerateSwarm(swarmCount);
        DomainInfo globalBest = problemDomain.FindBest(swarm);

        for (int t = 0; t < max_iter; ++t) {
            double w = getW(t, max_iter);

            for (int i = 0; i < swarmCount; ++i) {
                double r1 = rand.nextDouble();
                double r2 = rand.nextDouble();
                Particle particle = swarm[i];
                double[] v = particle.velocity;
                double[] x = particle.current.position;
                double[] p = particle.historyBest.position;
                double[] g = globalBest.position;

                for (int j = 0; j < D; ++j)
                    v[j] = w * v[j] + C1 * r1 * (p[j] - x[j]) + C2 * r2 * (g[j] - x[j]);

                // TODO:将v限制在指定的范围内

                for (int j = 0; j < D; ++j)
                    x[j] += v[j];

                for (int j = 0; j < D; ++j)
                    x[j] = Restrain(x[j], problemDomain.regionIntervals[j]);

                particle.current.distance =  problemDomain.TargetDistance(x);
                particle.UpdateHistoryBest();
            }
            globalBest = problemDomain.FindBest(swarm);
        }

        return new PSOResult(globalBest.position, globalBest.distance);
    }

    private double Restrain(double value, Interval interval) {
        if (value < interval.lower) return interval.lower;
        if (value > interval.upper) return interval.upper;
        return value;
    }

    private double getW(int current_iter, int max_iter) {
        return W.upper - W.width() * current_iter / max_iter;
    }
}

class PSOResult {
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

    public double[] Position;
    public double Error;
}
