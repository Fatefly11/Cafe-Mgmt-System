package Controller.BidControllers;

import Entity.Bid;

import java.util.ArrayList;
import java.util.List;

public class staffViewSlot {
    public List<Object> staffViewFinal(int staff_id){
        Bid bidEntity = new Bid();
        List<Object> bids = bidEntity.staffViewFinal(staff_id);
        return bids;
    }

}
