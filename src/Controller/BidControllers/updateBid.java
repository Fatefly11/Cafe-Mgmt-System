package Controller.BidControllers;

import Entity.Bid;

public class updateBid {
    public String updateOneBid(int bid_id, int staff_id, int slot_id, String status){
        Bid bidEntity = new Bid();
        String result = bidEntity.updateOneBid(bid_id,staff_id,slot_id,status);
        System.out.println(result);
        return result;
    }
}
