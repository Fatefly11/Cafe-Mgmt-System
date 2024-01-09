package Controller.BidControllers;

import Entity.Bid;

import java.util.List;

public class viewStaffBid {
    public List<Object> viewAllBids(){
        Bid bidEntity = new Bid();
        List<Object> bids = bidEntity.viewAllBids();
        return bids;
    }
}
