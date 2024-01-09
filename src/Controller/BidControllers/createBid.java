package Controller.BidControllers;

import Entity.Bid;

public class createBid {
    public String createNewBid(int staff_id, int slot_id){
        Bid bidEntity = new Bid();
        String result = bidEntity.createNewBid(staff_id,slot_id);
        System.out.println(result);
        return result;
    }
}
