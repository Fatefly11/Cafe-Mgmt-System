package Controller.BidControllers;

import Entity.Bid;

import java.util.List;

public class viewPendingBid {
    public List<Object> viewPendingBidsByStaffId(int staff_id){
        Bid bidEntity = new Bid();
        List<Object> bids = bidEntity.viewPendingBidsByStaffId(staff_id);
        return bids;
    }
}
