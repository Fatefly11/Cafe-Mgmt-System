package Controller.WorkSlotControllers;

import Entity.WorkSlot;

import java.util.List;

public class searchSlot {
    public List<WorkSlot> searchSlotBySubString(String substring){
        WorkSlot workSlotEntity = new WorkSlot();
        List<WorkSlot> workSlots = workSlotEntity.searchSlotBySubString(substring);
        return workSlots;
    }
}
