package Controller.WorkSlotControllers;

import Entity.Account;
import Entity.WorkSlot;

public class deleteSlot {
    public String deleteOneSlot(int slot_id){
        WorkSlot workSlotEntity = new WorkSlot();
        String result = workSlotEntity.deleteOneSlot(slot_id);
        System.out.println(result);
        return result;
    }
}
