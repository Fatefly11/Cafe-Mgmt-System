package Controller.BidControllers;

import Entity.Bid;

import java.util.List;

public class viewRejectBid {
    public List<Object> viewRejectBidsByStaffId(int staff_id){
        Bid bidEntity = new Bid();
        List<Object> bids = bidEntity.viewRejectBidsByStaffId(staff_id);
        return bids;
    }
}
