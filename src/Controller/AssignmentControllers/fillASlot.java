package Controller.AssignmentControllers;

import Entity.Assignment;

public class fillASlot {
    public String fillASlot(int slot_id, int staff_id){
        Assignment assignmentEntity = new Assignment();
        String result = assignmentEntity.fillASlot(slot_id, staff_id);
        return result;
    }

}
