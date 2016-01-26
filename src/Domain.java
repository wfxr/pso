import com.google.common.base.Preconditions;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

/**
 * PSO problem domain class
 *
 * Created by Wenxuan on 2016/1/13.
 * Email: wenxuan-zhang@outlook.com
 */
public class Domain {
    /**
     * Dimension of problem domain
     */
    public final int D;

    /**
     * Position domain of the problem
     */
    public final Interval[] PositionDomain;

    /**
     * Maximum allowable speed (of particle)
     */
    public final double[] MaxSpeed;

    /**
     * Evaluation function of the problem
     */
    public Function<double[], Double> EvaluationFunc;

    /**
     * The evaluation function value to look for
     */
    public double TargetValue;

    private double speedRate = 0.15;
    private static final Random rand = new Random();

    /**
     * Compute the error between evaluation of the position and target value
     *
     * @param position position vector
     * @return error value
     */
    public double TargetError(double[] position) {
        double evaluation;
        try {
            evaluation = EvaluationFunc.apply(position);
        } catch (Exception e) {
            return Double.POSITIVE_INFINITY;
        }

        return Math.abs(TargetValue - evaluation);
    }

    /**
     * @param positionDomain Position domain
     * @param evaluationFunc Evaluation function
     */
    public Domain(Interval[] positionDomain, Function<double[], Double> evaluationFunc) {
        this(positionDomain, evaluationFunc, 0);
    }

    /**
     * @param positionDomain Position domain
     * @param evaluationFunc Evaluation function
     * @param targetValue The evaluation function value to look for
     */
    public Domain(Interval[] positionDomain, Function<double[], Double> evaluationFunc, double targetValue) {
        D = positionDomain.length;
        PositionDomain = positionDomain;
        MaxSpeed = ComputeMaxSpeed();
        EvaluationFunc = evaluationFunc;
        TargetValue = targetValue;
    }

    /**
     * @return Maximum allowable speed
     */
    private double[] ComputeMaxSpeed() {
        return Stream.of(PositionDomain).mapToDouble(x -> x.width() * speedRate).toArray();
    }

    /**
     * Create a random particle in the domain
     *
     * @return a random particle
     */
    public Particle RandomParticle() {
        double[] position = RandomPosition();
        double distance = TargetError(position);
        DomainInfo domainInfo = new DomainInfo(position, distance);
        return new Particle(domainInfo, RandomVelocity());
    }

    /**
     * Create a swarm of random particles in the domain
     *
     * @param count The count of the particles to create
     * @return a swarm of random particles
     */
    public Particle[] RandomSwarm(int count) {
        return Stream.generate(this::RandomParticle)
                .limit(count)
                .toArray(Particle[]::new);
    }

    /**
     * create a random position vector in the domain
     *
     * @return a random position vector
     */
    public double[] RandomPosition() {
        return Stream.of(PositionDomain)
                .mapToDouble(x -> Random(x.getLower(), x.getUpper()))
                .toArray();
    }

    /**
     * create a random velocity vector in the domain
     *
     * @return a random velocity vector
     */
    public double[] RandomVelocity() {
        return DoubleStream.of(MaxSpeed)
                .map(v -> Random(-v, v))
                .toArray();
    }

    /**
     * Find the particle who is the nearest to the target value and return its domain info
     *
     * @param swarm Particle swarm
     * @return The domain info of the particle who is the nearest to the target value
     */
    public static DomainInfo FindBest(Particle[] swarm) {
        return Stream.of(swarm)
                .min((p1, p2) -> Double.compare(p1.Best.distance, p2.Best.distance))
                .get().Best;
    }

    /**
     * Generate a random value in the range min and max specified
     *
     * @param min minimum value of the range, inclusive
     * @param max maximum value of the range, exclusive
     * @return a random value between min and max
     */
    private static double Random(double min, double max) {
        return min + (max - min) * rand.nextDouble();
    }

    /**
     * @return the speed rate
     */
    public double getSpeedRate() {
        return speedRate;
    }

    /**
     * @param speedRate the speed rate to set
     */
    public void setSpeedRate(double speedRate) {
        Preconditions.checkArgument(speedRate > 0 && speedRate <= 1);
        this.speedRate = speedRate;
    }
}

