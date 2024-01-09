package Controller.BidControllers;

import Entity.Bid;
import Entity.Profile;

import java.util.List;

public class searchBid {
    public List<Object> searchBidBySubString(int staff_id, String status, String substring){
        Bid bidEntity = new Bid();
        List<Object> bids;
        bids = bidEntity.searchBidBySubString(staff_id, status, substring);
        return bids;
    }
}
