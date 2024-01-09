package Controller.AssignmentControllers;

import Entity.Assignment;

import java.util.ArrayList;
import java.util.List;

public class viewAssignmentByWorkSlot {
    public List<Object> viewAssignmentByWorkSlot(int slot_id){
        List<Object> assignments = new ArrayList<>();
        Assignment assignmentEntity = new Assignment();
        assignments = assignmentEntity.viewAssignmentByWorkSlot(slot_id);
        return assignments;
    }
}
