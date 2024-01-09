import React, { useState, useEffect } from "react";
import "./ModalWorkSlotOwner.css"

import {
  Modal,
  ModalDialog,
  DialogTitle,
  DialogContent,
  Button,
  Input,
  FormLabel,
  Textarea,
  Select,
  Option,
  MenuItem
} from "@mui/joy";

function ModalUpdateWorkSlotOwner({ open, onClose, userId, slotId }) {
  const [slotDate, setSlotDate] = useState("");
  const [shiftTime, setShiftTime] = useState("");

  const workSlotDate = [
    "2023-11-27",
    "2023-11-28",
    "2023-11-29",
    "2023-11-30",
    "2023-12-01",
    "2023-12-02",
    "2023-12-03"
  ];
  
  const workSlotShift = [
    "9AM-1PM",
    "1PM-5PM"
  ];

  useEffect(() => {
    fetchWorkSlotData();
  }, []);

  const fetchWorkSlotData = async () => {
    try {
      const response = await fetch(`http://43.134.110.180:8080/api/workSlot/${slotId}`);
      if (response.ok) {
        const data = await response.json();
        setSlotDate(data.slot_date)
        setShiftTime(data.shift_time)

      } else {
        console.error("Failed to fetch work slot data.");
      }
    } catch (error) {
      console.error("Error fetching work slot:", error);
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    const slot = {
      slot_date: slotDate,
      cafe_owner_id: userId,
      shift_time: shiftTime
    };

    console.log(slot)

    try {
      const response = await fetch(`http://43.134.110.180:8080/api/workSlot/${slotId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(slot),
      });
  
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
  
      const data = await response.text();
      console.log("Server response:", data);

      alert(data);

      onClose();

      window.location.reload();
    } catch (error) {
      console.error("Error:", error);
    }
  };

    return (
      <Modal open={open} onClose={onClose}>
        <ModalDialog>
          <DialogTitle>Update Work Slot</DialogTitle>
          <DialogContent>
          <p>Slot ID: {slotId}</p>
              <FormLabel>Date of Work Slot</FormLabel>
              <div className="custom-select-wrapper">
                <select
                  className="custom-select"
                  required
                  value={slotDate}
                  onChange={(e) => setSlotDate(e.target.value)}
                >
                  {workSlotDate.map((date) => (
                    <option key={date} value={date}>
                      {date}
                    </option>
                  ))}
                </select>
              </div>
              <FormLabel>Shift of Work Slot</FormLabel>
              <div className="custom-select-wrapper">
                <select
                  className="custom-select"
                  required
                  value={shiftTime}
                  onChange={(e) => setShiftTime(e.target.value)}
                >
                  {workSlotShift.map((shift) => (
                    <option key={shift} value={shift}>
                      {shift}
                    </option>
                  ))}
                </select>
              </div>
              <Button type="cancel" onClick={onClose}>
                Cancel
              </Button>
              <Button type="submit" onClick={handleSubmit}>
                Update
              </Button>
          </DialogContent>
        </ModalDialog>
      </Modal>
    );
  }

export default ModalUpdateWorkSlotOwner;
