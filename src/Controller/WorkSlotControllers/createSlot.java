package Controller.WorkSlotControllers;

import Entity.Bid;
import Entity.WorkSlot;

import java.sql.Date;
import java.time.LocalDate;

public class createSlot {
    public String createNewSlot(String slot_date, int cafe_owner_id, String shift_time){
        WorkSlot workSlotEntity = new WorkSlot();
        String result = workSlotEntity.createNewSlot(slot_date,cafe_owner_id,shift_time);
        System.out.println(result);
        return result;
    }
}
