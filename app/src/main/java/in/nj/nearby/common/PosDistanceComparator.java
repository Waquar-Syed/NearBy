package in.nj.nearby.common;

import java.util.Comparator;

import in.nj.nearby.model.POSModel;

/**
 * Created by nitesh on 12-12-2017.
 */

public class PosDistanceComparator implements Comparator<POSModel> {

    @Override
    public int compare(POSModel posModel, POSModel t1) {
        double distance1 = Double.parseDouble(posModel.getDistance());
        double distance2 = Double.parseDouble(t1.getDistance());
        if(distance1<distance2)
            return -1;
        else if (distance1>distance2)
            return 1;
        return 0;
    }
}
