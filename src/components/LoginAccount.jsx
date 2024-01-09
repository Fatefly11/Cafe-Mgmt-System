import React, { useState, useEffect } from "react";
import './LoginAccount.css';
import { Button, Chip, Divider, Input, Option, Select, Stack, Switch } from '@mui/joy';
import NavbarEmpty from './reusable-components/NavbarEmpty';
import { Link, useNavigate } from 'react-router-dom';

function LoginAccount() {
  const [userProfile, setUserProfile] = useState("admin")
  const [username, setUsername] = useState("")
  const [password, setPassword] = useState("")
  const navigate = useNavigate()

  useEffect(() => {
    sessionStorage.removeItem("username")
    sessionStorage.removeItem("userProfile");
    }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();
  
    const user = {
      username: username,
      password: password,
      p_id: profileTypeMap[userProfile],
    };
    
    console.log(JSON.stringify(user));

    let url;

    if (userProfile === "admin") {
      url = `http://43.134.110.180:8080/api/adminLogin`;
    } else if (userProfile === "owner") {
      url = `http://43.134.110.180:8080/api/ownerLogin`;
    } else if (userProfile === "manager") {
      url = `http://43.134.110.180:8080/api/managerLogin`;
    } else if (userProfile === "staff") {
      url = `http://43.134.110.180:8080/api/staffLogin`;
    }

    try {
      const response = await fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(user),
      });
  
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
  
      const data = await response.text();
      console.log("Server response:", data);

      alert(data);

      if(data === "Staff Login successful." || 
         data === "Cafe Owner Login successful." || 
         data === "Admin Login successful." || 
         data === "Manager Login successful.") {

        sessionStorage.setItem('username', username);
        sessionStorage.setItem('userProfile', userProfile);
        
        if (userProfile === "admin") {
          navigate(`/AdminDashboard`);
        } else if (userProfile === "owner") {
          navigate(`/OwnerDashboard`);
        } else if (userProfile === "manager") {
          navigate(`/ManagerDashboard`);
        } else if (userProfile === "staff") {
          navigate(`/StaffDashboard`);
        }
      }

    } catch (error) {
      console.error("Error:", error);
    }
  };

  const profileTypeMap = {
    "owner": 1,
    "manager": 2,
    "admin": 3,
    "staff": 4,
  };

  return (
      <div className="login-account">
        <NavbarEmpty />
        <Divider />
          <div className="welcome-message-login">
            <h1>Login to Your Account</h1>
            <h3>The Future of Cafe Staff Management: Your Cafe, Your Schedule,</h3>
            <h3>Their Choice</h3>
          </div>
          <div className="select-profile">
            I am the 
          </div>
          <div style={{width: "50%", display: "inline-block", marginRight: 0}}>
            <select
              className="profile-custom-select"
              value={userProfile}
              onChange={(e) => setUserProfile(e.target.value)}
            >
              <option value="admin">ADMIN</option>
              <option value="owner">OWNER</option>
              <option value="manager">MANAGER</option>
              <option value="staff">STAFF</option>
            </select>
          </div>
          <div className="login">
            <Stack spacing={1}>
              <Input 
                className="login-fields"
                placeholder="Username" 
                variant="solid"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                />
              <Input 
                className="login-fields"
                placeholder="Password" 
                variant="solid"  
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                />
              <Button 
                className="login-fields"
                type="submit" 
                style={{color: "white"}}
                onClick={handleSubmit}>
                  Login
              </Button>
            </Stack>
          </div>
      </div>
  );
}

export default LoginAccount;
