/**
 * Particle class
 *
 * Created by Wenxuan on 2016/1/13.
 * Email: wenxuan-zhang@outlook.com
 */
public class Particle {
    /**
     * @param domainInfo Init domain info
     * @param velocity Init velocity
     */
    public Particle(DomainInfo domainInfo, double[] velocity) {
        this.Current = domainInfo;
        this.Best =  Current.clone();
        this.Velocity = velocity;
    }

    public void UpdateHistoryBest() {
        if (Current.distance < Best.distance) {
            Best.distance = Current.distance;
            Best.position = Current.position.clone();
        }
    }

    public DomainInfo Current;
    public DomainInfo Best;
    public double[] Velocity;
}

class DomainInfo implements Cloneable {
    public DomainInfo(double[] position, double distance) {
        this.position = position;
        this.distance = distance;
    }

    public DomainInfo clone() {
        DomainInfo cloned = null;
        try {
            cloned = (DomainInfo) super.clone();
            cloned.position = position.clone();
            cloned.distance = distance;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cloned;
    }

    public double[] position;
    public double distance;
}
