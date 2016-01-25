/**
 * Created by Wenxuan on 2016/1/13.
 * Email: wenxuan-zhang@outlook.com
 */
public class Particle {
    public Particle(DomainInfo domainInfo, double[] velocity) {
        this.current = domainInfo;
        this.historyBest =  current.clone();
        this.velocity = velocity;
    }

    public void UpdateHistoryBest() {
        if (current.distance < historyBest.distance) {
            historyBest.distance = current.distance;
            historyBest.position = current.position.clone();
        }
    }

    public DomainInfo current;
    public DomainInfo historyBest;
    public double[] velocity;
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
