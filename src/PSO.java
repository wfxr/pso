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
    private Domain domain;

    private Random rand = new Random();

    public PSO(Domain problemDomain) {
        this.domain = problemDomain;
        this.D = this.domain.D;
    }

    public PSOResult Execute(int swarmCount, int max_iter) {
        Particle[] swarm = domain.RandomSwarm(swarmCount);
        DomainInfo globalBest = Domain.FindBest(swarm);

        for (int t = 0; t < max_iter; ++t) {
            double w = getW(t, max_iter);

            for (int i = 0; i < swarmCount; ++i) {
                double r1 = rand.nextDouble();
                double r2 = rand.nextDouble();
                Particle particle = swarm[i];
                double[] v = particle.Velocity;
                double[] x = particle.Current.position;
                double[] p = particle.Best.position;
                double[] g = globalBest.position;

                for (int j = 0; j < D; ++j)
                    v[j] = w * v[j] + C1 * r1 * (p[j] - x[j]) + C2 * r2 * (g[j] - x[j]);

                // TODO:将v限制在指定的范围内

                for (int j = 0; j < D; ++j)
                    x[j] += v[j];

                for (int j = 0; j < D; ++j)
                    x[j] = Restrain(x[j], domain.PositionDomain[j]);

                particle.Current.distance =  domain.TargetError(x);
                particle.UpdateHistoryBest();
            }
            globalBest = Domain.FindBest(swarm);
        }

        return new PSOResult(globalBest.position, globalBest.distance);
    }

    private double Restrain(double value, Interval interval) {
        if (value < interval.getLower()) return interval.getLower();
        if (value > interval.getUpper()) return interval.getUpper();
        return value;
    }

    private double getW(int current_iter, int max_iter) {
        return W.getUpper() - W.width() * current_iter / max_iter;
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
