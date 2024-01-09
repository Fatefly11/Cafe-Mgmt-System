package Controller.AssignmentControllers;

import Entity.Assignment;

public class unfillASlot {
    public String unfillASlot(int assignment_id){
        Assignment assignmentEntity = new Assignment();
        String result = assignmentEntity.unfillASlot(assignment_id);
        return result;
    }
}
