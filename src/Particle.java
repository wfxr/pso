/**
 * Particle class
 *
 * Created by Wenxuan on 2016/1/13.
 * Email: wenxuan-zhang@outlook.com
 */
class Particle {
    /**
     * @param domainInfo Init domain info
     * @param velocity Init velocity
     */
    public Particle(DomainInfo domainInfo, double[] velocity) {
        this.Current = domainInfo;
        this.Best =  Current.clone();
        this.Velocity = velocity;
    }

    /**
     * Update history best domain info
     */
    public void UpdateHistoryBest() {
        if (Current.distance < Best.distance)
            Best = Current.clone();
    }

    /**
     * Current domain info
     */
    public DomainInfo Current;

    /**
     * History best domain info the particle ever found
     */
    public DomainInfo Best;

    /**
     * Velocity of the particle
     */
    public double[] Velocity;
}

/**
 * Data structure to store domain info
 */
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

    /**
     * Position vector
     */
    public double[] position;

    /**
     * Distance from the position to the target evaluation function value
     */
    public double distance;
}
