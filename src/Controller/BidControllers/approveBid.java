package Controller.BidControllers;

import Entity.Bid;

public class approveBid {
    public String approveOneBid(int bid_id){
        Bid bidEntity = new Bid();
        String result = bidEntity.approveOneBid(bid_id);
        return result;
    }
}
