import java.util.Random;
import java.util.function.Function;

/**
 * Created by Wenxuan on 2016/1/13.
 * Email: wenxuan-zhang@outlook.com
 */
public class ProblemDomain {
    public final int D;
    public final Interval[] regionIntervals;
    public final double[] MaxSpeed;
    public final double VelocityRate = 0.15;
    public Function<double[], Double> EvaluationFunc;
    public double TargetValue = 0;
    private static final Random rand = new Random();

    public double TargetDistance(double[] position) {
        double evaluation;
        try {
            evaluation = EvaluationFunc.apply(position);
        } catch (Exception e) {
            return Double.POSITIVE_INFINITY;
        }

        return Math.abs(TargetValue - evaluation);
    }

    public ProblemDomain(Interval[] regionIntervals, Function<double[], Double> evaluationFunc) {
        D = regionIntervals.length;
        this.regionIntervals = regionIntervals;
        MaxSpeed = CreateVelocityBounds(regionIntervals);
        EvaluationFunc = evaluationFunc;
    }

    public ProblemDomain(Interval[] regionIntervals, Function<double[], Double> evaluationFunc, double targetValue) {
        D = regionIntervals.length;
        this.regionIntervals = regionIntervals;
        MaxSpeed = CreateVelocityBounds(regionIntervals);
        EvaluationFunc = evaluationFunc;
        TargetValue = targetValue;
    }

    private double[] CreateVelocityBounds(Interval[] regionIntervals) {
        double[] maxSpeed = new double[regionIntervals.length];
        for (int i = 0; i < regionIntervals.length; ++i)
            maxSpeed[i] = regionIntervals[i].width() * VelocityRate;

        return maxSpeed;
    }

    public Particle GenerateParticle() {
        double[] position = RandomPosition();
        double distance = TargetDistance(position);
        DomainInfo domainInfo = new DomainInfo(position, distance);
        return new Particle(domainInfo, RandomVelocity());
    }

    public Particle[] GenerateSwarm(int count) {
        Particle[] swarm = new Particle[count];
        for (int i = 0; i < count; ++i)
            swarm[i] = GenerateParticle();
        return swarm;
    }

    public double[] RandomPosition() {
        return RandomVector(regionIntervals);
    }

    public double[] RandomVelocity() {
        double[] vec = new double[D];
        for (int i = 0; i < D; ++i)
            vec[i] = Random(-MaxSpeed[i], MaxSpeed[i]);
        return vec;
    }

    public DomainInfo FindBest(Particle[] swarm) {
        int minIdx = 0;
        for (int i = 0; i < swarm.length; ++i)
            if (swarm[i].historyBest.distance < swarm[minIdx].historyBest.distance)
                minIdx = i;
        return swarm[minIdx].historyBest;
    }

    private static double[] RandomVector(Interval[] intervals) {
        double[] vec = new double[intervals.length];
        for (int i = 0; i < intervals.length; ++i)
            vec[i] = Random(intervals[i]);
        return vec;
    }

    public static double Random(Interval interval) {
        return Random(interval.lower, interval.upper);
    }

    public static double Random(double min, double max) {
        return min + (max - min) * rand.nextDouble();
    }
}

