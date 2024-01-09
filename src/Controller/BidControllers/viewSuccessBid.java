package Controller.BidControllers;

import Entity.Bid;

import java.util.List;

public class viewSuccessBid {
    public List<Object> viewSuccessBidsByStaffId(int staff_id){
        Bid bidEntity = new Bid();
        List<Object> bids = bidEntity.viewSuccessBidsByStaffId(staff_id);
        return bids;
    }
}
