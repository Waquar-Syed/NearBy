package in.nj.nearby.common;

import java.util.Comparator;

import in.nj.nearby.model.POSModel;

/**
 * Created by nitesh on 09-12-2017.
 */

public class PosOffersComparator implements Comparator<POSModel> {

    @Override
    public int compare(POSModel posModel, POSModel t1) {
        if(posModel.getRewardMultiplier()>t1.getRewardMultiplier())
            return -1;
        else if (posModel.getRewardMultiplier()<t1.getRewardMultiplier())
            return 1;
        return 0;
    }
}
