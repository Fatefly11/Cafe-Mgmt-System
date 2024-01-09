package Controller.BidControllers;

import Entity.Bid;

public class deleteBid {
    public String deleteOneBid(int bid_id){
        Bid bidEntity = new Bid();
        String result = bidEntity.deleteOneBid(bid_id);
        System.out.println(result);
        return result;
    }
}
