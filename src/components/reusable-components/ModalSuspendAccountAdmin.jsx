// import React, { useState, useEffect } from "react";
// import {
//   Modal,
//   ModalDialog,
//   DialogTitle,
//   DialogContent,
//   Button,
//   Input,
//   FormLabel,
// } from "@mui/joy";

// function ModalSuspendAccountAdmin({ open, onClose, accountId }) {
//   // Initialize state variables for user data and modal control
//   const [username, setUsername] = useState("");
//   const [fullname, setFullname] = useState("");
//   const [userProfile, setUserProfile] = useState("");
//   const [maxWorkSlot, setMaxWorkSlot] = useState("");
//   const [userPhoto, setUserPhoto] = useState("");
//   const [showConfirmationModal, setShowConfirmationModal] = useState(false);

//   // Fetch user data when the modal is opened and the accountId is provided
//   useEffect(() => {
//     if (open && accountId) {
//       fetchAccountData(accountId);
//     }
//   }, [open, accountId]);

//   // map profile ID to profile type
//   const profileTypeMap = {
//     1: "Cafe Owner",
//     2: "Cafe Manager",
//     3: "System Admin",
//     4: "Cafe Staff (chef)",
//     5: "Cafe Staff (cashier)",
//     7: "Cafe Staff (waiter)"
  
//   };

//   //fetch user data from the API
//   const fetchAccountData = async (accountId) => {
//     try {
//       const response = await fetch(`http://43.134.110.180:8080/api/account/${accountId}`);
//       if (!response.ok) {
//         throw new Error("Network response was not ok");
//       }
//       const data = await response.json();
//       setUsername(data.username);
//       setFullname(data.full_name);
//       setUserProfile(profileTypeMap[data.p_id] || "Unknown");
//       setMaxWorkSlot(data.max_slot.toString());
//       if (data.profile_photo) {
//         setUserPhoto(`data:image/png;base64,${data.profile_photo}`);
//       }
//     } catch (error) {
//       console.error("Error fetching account data:", error);
//     }
//   };

//   //show the confirmation modal for suspending an account
//   const handleSuspendAccount = () => {
//     setShowConfirmationModal(true);
//   };

//   //call the API to suspend the user account
//   const handleSubmit = async (event) => {
//     event.preventDefault();
//     try {
//       let requestBody = { ...userData };
//       if (userPhoto) {
//         requestBody = new FormData();
//         requestBody.append("photo", userPhoto);
//         for (const key in userData) {
//           requestBody.append(key, userData[key]);
//         }
//       } else {
//         requestBody = JSON.stringify(userData);
//       }

//       const response = await fetch(
//         `http://43.134.110.180:8080/api/account/${accountId}`,
//         {
//           method: "DELETE",
//           headers: {
//             'Content-Type': userPhoto ? 'multipart/form-data' : 'application/json',
//           },
//           body: requestBody
//         }
//       );

//       if (!response.ok) {
//         const errorData = await response.json();
//         throw new Error(errorData.message || "Failed to suspend account");
//       }

//       console.log("Account created successfully.");
//       onClose();
//     } catch (error) {
//       console.error("Error creating account:", error);
//     }
//   };

//   return (
//     <>
//       <Modal open={open} onClose={onClose}>
//         <ModalDialog>
//           <DialogTitle>Suspend User Account</DialogTitle>
//           <DialogContent>
//             <p>AccountID: {accountId}</p>
//             <FormLabel>Username</FormLabel>
//             <Input required value={username} disabled />
//             <FormLabel>Full name</FormLabel>
//             <Input required value={fullname} disabled />
//             <FormLabel>User profile type</FormLabel>
//             <Input value={userProfile} disabled />
//             <FormLabel>Max work slot</FormLabel>
//             <Input value={maxWorkSlot} disabled />
//             <FormLabel>Profile Photo</FormLabel>
//             {userPhoto && (
//               <div>
//                 <img src={userPhoto} alt="User Profile" style={{ maxWidth: "100%", maxHeight: "auto" }} />
//               </div>
//             )}
//             <Button type="button" onClick={onClose}>Close</Button>
//             <Button type="button" onClick={handleSuspendAccount}>Suspend Account</Button>
//           </DialogContent>
//         </ModalDialog>
//       </Modal>

//       {showConfirmationModal && (
//         <Modal open={showConfirmationModal} onClose={() => setShowConfirmationModal(false)}>
//           <ModalDialog>
//             <DialogTitle>Are you sure you want to suspend this account?</DialogTitle>
//             <DialogContent>
//               <p>This action is permanent and cannot be undone.</p>
//               <Button type="button" onClick={() => setShowConfirmationModal(false)}>Cancel</Button>
//               <Button type="button" onClick={setShowConfirmationModal(true)}>Yes</Button>
//             </DialogContent>
//           </ModalDialog>
//         </Modal>
//       )}
//     </>
//   );
// }

// export default ModalSuspendAccountAdmin;

import React, { useState, useEffect } from "react";
import {
  Modal,
  ModalDialog,
  DialogTitle,
  DialogContent,
  Button,
  Input,
  FormLabel,
} from "@mui/joy";

function ModalSuspendAccountAdmin({ open, onClose, accountId }) {
  const [username, setUsername] = useState("");
  const [fullname, setFullname] = useState("");
  const [userProfile, setUserProfile] = useState("");
  const [maxWorkSlot, setMaxWorkSlot] = useState("");
  const [userPhoto, setUserPhoto] = useState("");
  const [showConfirmationModal, setShowConfirmationModal] = useState(false);
  const [profiles, setProfiles] = useState([]);

  useEffect(() => {
    if (open && accountId) {
      fetchAccountData(accountId);
    }
    fetchProfiles();
  }, [open, accountId]);

  const fetchAccountData = async (accountId) => {
    try {
      const response = await fetch(`http://43.134.110.180:8080/api/account/${accountId}`);
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      const data = await response.json();
      setUsername(data.username);
      setFullname(data.full_name);
      setUserProfile(data.p_id);
      setMaxWorkSlot(data.max_slot.toString());
      if (data.profile_photo) {
        setUserPhoto(`data:image/png;base64,${data.profile_photo}`);
      }
    } catch (error) {
      console.error("Error fetching account data:", error);
    }
  };

  const fetchProfiles = async () => {
    try {
      const response = await fetch("http://43.134.110.180:8080/api/profiles");
      if (response.ok) {
        const data = await response.json();
        setProfiles(data);
      } else {
        console.error("Failed to fetch profiles.");
      }
    } catch (error) {
      console.error("Error fetching profiles:", error);
    }
  };

  const handleSuspendAccount = () => {
    setShowConfirmationModal(true);
  };

  const handleConfirmSuspend = async () => {
    try {
      const response = await fetch(`http://43.134.110.180:8080/api/account/${accountId}`, {
        method: "DELETE",    //ask boyu if backend want to save the data or not. if yes then use PUT. but status will be suspended.
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ suspended: true }), 
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
  
      const data = await response.text();
      console.log("Server response:", data);

      alert(data);
      
      setShowConfirmationModal(false);
      onClose();
      window.location.reload();
    } catch (error) {
      console.error("Error:", error);
    }
  };

  return (
    <>
      <Modal open={open} onClose={onClose}>
        <ModalDialog>
          <DialogTitle>Suspend User Account</DialogTitle>
          <DialogContent>
            <p>AccountID: {accountId}</p>
            <FormLabel>Username</FormLabel>
            <Input value={username} disabled />
            <FormLabel>Full name</FormLabel>
            <Input value={fullname} disabled />
            <FormLabel>User profile type</FormLabel>
            <Input value={profiles.find((profile) => profile.profile_id === userProfile)?.profile_name || ''} disabled />
            <FormLabel>Max work slot</FormLabel>
            <Input value={maxWorkSlot} disabled />
            {/*<FormLabel>Profile Photo</FormLabel>
            {userPhoto && (
              <div>
                <img src={userPhoto} alt="User Profile" style={{ maxWidth: "100%", maxHeight: "auto" }} />
              </div>
            )} */}
            <Button type="button" onClick={onClose}>Close</Button>
            <Button type="button" onClick={handleSuspendAccount}>Suspend Account</Button>
          </DialogContent>
        </ModalDialog>
      </Modal>

      {showConfirmationModal && (
        <Modal open={showConfirmationModal} onClose={() => setShowConfirmationModal(false)}>
          <ModalDialog>
            <DialogTitle>Are you sure you want to suspend this account?</DialogTitle>
            <DialogContent>
              <p>This action is irreversible and cannot be undone.</p>
              <Button type="button" onClick={() => setShowConfirmationModal(false)}>Cancel</Button>
              <Button type="button" onClick={handleConfirmSuspend}>Confirm Suspend</Button>
            </DialogContent>
          </ModalDialog>
        </Modal>
      )}
    </>
  );
}

export default ModalSuspendAccountAdmin;
