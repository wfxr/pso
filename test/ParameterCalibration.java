import XajModel.*;

import java.util.Collections;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Wenxuan on 1/17/2016.
 * Email: wenxuan-zhang@outlook.com
 */
public class ParameterCalibration {
    static double[] P = new double[]{
            10, 24.1, 20.4, 18.3, 10.1, 5.5, 0.6, 3.1, 1.9, 4.6, 5,
            4.8, 36.2, 29, 6, 3.6, 0.4, 0, 0.5, 3.8, 0, 1.8, 0.2, 0.3
    };
    static double[] EI = new double[]{
            0.1, 0, 0.1, 0.5, 0.7, 0.9, 0.8, 0.7, 0.5, 0.3, 0.2,
            0.1, 0, 0, 0.1, 0.6, 0.8, 1, 0.9, 0.8, 0.7, 0.5, 0.3, 0.1
    };
    static double[] ObsQ = new double[]{
            88.13, 220.9, 391.36, 452.51, 427.42, 296.26, 164.35, 88.81, 109.76,
            134.55, 232.84, 306.02, 966.71, 2162.41, 1665.03, 563.39, 219.63, 84.87,
            64.58, 100.6, 137.23, 88.17, 87.44, 70.01
    };

    static XajModel xaj = new XajModel();
    public static void main(String args[]) {
        Interval[] regionIntervals = new Interval[]{
                new Interval(5, 100),
                new Interval(50, 300),
                new Interval(5, 100),
                new Interval(0.5, 0.95),
                new Interval(0.05, 0.3),
                new Interval(0.15, 0.35),
                new Interval(0, 0.3),
                new Interval(5, 100),
                new Interval(0.5, 2),
                new Interval(0.65, 0.8),
                new Interval(0.05, 0.65),
                new Interval(0.2, 0.9),
                new Interval(0.5, 1.0),

                new Interval(0, 50),
                new Interval(20, 100),
                new Interval(40, 100),
                new Interval(0, 100),
                new Interval(0, 100),
                new Interval(0, 100),
        };

        Domain domain = new Domain(regionIntervals, ParameterCalibration::Evaluate, 1);
        PSO pso = new PSO(domain);
        PSOResult result = pso.Execute(1000, 100);
        System.out.println(result);
    }

    public static double Evaluate(double[] position) {
        xaj.SetSoilWaterStorageParam(position[0], position[1], position[2]);
        xaj.SetEvapotranspirationParam(position[3], position[4]);
        xaj.SetRunoffGenerationParam(position[5], position[6]);
        xaj.SetSourcePartitionParam(position[7], position[8], position[9], position[10]);
        xaj.SetRunoffConcentrationParam(position[11], position[12], 537);

        RunoffGenerationResult runoffGenerationResult = xaj.ComputeRunoffGeneration(P, EI, position[13], position[14], position[15]);
        SourcePartitionResult sourcePartitionResult = xaj.ComputeSourcePartition(runoffGenerationResult, position[16], 2);
        RunoffConcentrationResult runoffConcentrationResult = xaj.ComputeRunoffConcentration(sourcePartitionResult, position[17], position[18], 2);

        return NSE(ObsQ, runoffConcentrationResult.Q);
    }

    public static double NSE(double[] obs, double[] pre) {
        int n = obs.length;
        if (n != pre.length)
            throw new IllegalArgumentException("length Error");

        double avg = DoubleStream.of(obs).average().getAsDouble();
        double s1 = 0, s2 = 0;
        for (int i = 0; i < n; ++i) {
            s1 += Math.pow(obs[i] - pre[i], 2);
            s2 += Math.pow(obs[i] - avg, 2);
        }
        return 1 - s1 / s2;
    }
}
