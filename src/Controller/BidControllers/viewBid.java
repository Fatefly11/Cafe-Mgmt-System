package Controller.BidControllers;

import Entity.Bid;

public class viewBid {
    public Object viewOneBid(int bid_id){
        Bid bidEntity = new Bid();
        Object bid = bidEntity.viewOneBid(bid_id);
        return bid;
    }
}
