package Controller.WorkSlotControllers;

import Entity.WorkSlot;

import java.util.List;

public class ownerViewSlot {
    public List<WorkSlot> viewAllWorkSlots(){
        List<WorkSlot> workSlots;
        WorkSlot workSlotEntity = new WorkSlot();
        workSlots = workSlotEntity.viewAllWorkSlots();
        return workSlots;
    }
}
