package Controller.BidControllers;

import Entity.Bid;

public class rejectBid {
    public String rejectOneBid(int bid_id){
        Bid bidEntity = new Bid();
        String result = bidEntity.rejectOneBid(bid_id);
        return result;
    }
}
