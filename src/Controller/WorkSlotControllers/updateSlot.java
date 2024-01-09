package Controller.WorkSlotControllers;

import Entity.Account;
import Entity.WorkSlot;

public class updateSlot {
    public String updateOneSlot(int slot_id, String slot_date, String shift_time){
        WorkSlot workSlotEntity = new WorkSlot();
        String result = workSlotEntity.updateOneSlot(slot_id, slot_date, shift_time);
        System.out.println(result);
        return result;
    }
}
