package Controller.WorkSlotControllers;

import Entity.WorkSlot;

public class viewSlot {
    public WorkSlot viewOneSlot(int slot_id){
        WorkSlot workSlotEntity = new WorkSlot();
        WorkSlot workSlot = workSlotEntity.viewOneSlot(slot_id);
        return workSlot;
    }
}
