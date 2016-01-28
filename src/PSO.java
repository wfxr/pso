import java.util.*;

/**
 * Particle swarm optimization algorithm implementation
 * <p>
 *
 * Particle swarm optimization (PSO) is a computational method that optimizes
 * a problem by iteratively trying to improve a candidate solution with regard
 * to a given measure of quality.
 * <p>
 *
 * See <a href="https://en.wikipedia.org/wiki/Particle_swarm_optimization">
 *     wikipedia: Particle swarm optimization</a>
 * <p>
 *
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

    /**
     * @param problemDomain Problem domain
     */
    public PSO(Domain problemDomain) {
        this.domain = problemDomain;
        this.D = this.domain.D;
    }

    /**
     * Execute algorithm with specified swarm count and max iteration times
     *
     * @param swarmCount count of particles of swarm
     * @param max_iter max iteration times
     * @return pso result
     */
    public PSOResult Execute(int swarmCount, int max_iter) {
        Particle[] swarm = domain.RandomSwarm(swarmCount);
        DomainInfo globalBest = Domain.FindBest(swarm);

        for (int t = 0; t < max_iter; ++t) {
            double w = getW(t, max_iter);

            for (int k = 0; k < swarmCount; ++k) {
                double r1 = rand.nextDouble();
                double r2 = rand.nextDouble();
                Particle particle = swarm[k];
                double[] v = particle.Velocity;
                double[] x = particle.Current.position;
                double[] p = particle.Best.position;
                double[] g = globalBest.position;

                for (int i = 0; i < D; ++i)
                    v[i] = w * v[i] + C1 * r1 * (p[i] - x[i]) + C2 * r2 * (g[i] - x[i]);

                // TODO: Restrain v to the proper bound

                for (int i = 0; i < D; ++i)
                    x[i] += v[i];

                for (int i = 0; i < D; ++i)
                    x[i] = Restrain(x[i], domain.PositionDomain[i]);

                particle.Current.distance =  domain.TargetError(x);
                particle.UpdateHistoryBest();
            }
            globalBest = Domain.FindBest(swarm);
        }

        return new PSOResult(globalBest.position, globalBest.distance);
    }

    /**
     * Restrain the value within specified interval
     *
     * @param value the value to restrain
     * @param interval interval to restrain the value
     * @return restrained value
     */
    private double Restrain(double value, Interval interval) {
        if (value < interval.getLower()) return interval.getLower();
        if (value > interval.getUpper()) return interval.getUpper();
        return value;
    }

    /**
     * Get the value of W at current iteration times
     *
     * @param current_iter current iteration times
     * @param max_iter max iteration times
     * @return value of W
     */
    private double getW(int current_iter, int max_iter) {
        return W.getUpper() - W.width() * current_iter / max_iter;
    }
}

