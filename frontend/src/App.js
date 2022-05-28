import React, {useState, useEffect} from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import { UserContext } from "./userContext";
import Header from './components/Header';
import Login from './components/Login';
import Logout from './components/Logout';
import Home from './components/Home';
import Registration from './components/Registration';


function App() {

  const [user, setUser] = useState(localStorage.user ? JSON.parse(localStorage.user) : null);
  const updateUserData = (userInfo) => {
    localStorage.setItem("user", JSON.stringify(userInfo));
    setUser(userInfo);
  }

  return (
    <BrowserRouter>
      <UserContext.Provider value={{
          user: user,
          setUserContext: updateUserData
        }}>
      < Header/>
      <Routes>
           <Route path="/" exact element={<Home />}></Route>
          <Route path="/login" element={<Login />}></Route>
          <Route path="/register" element={<Registration />}></Route>
          <Route path="/logout" element={<Logout />}></Route>
          {/*<Route path="/profile" element={<Profile />}></Route>
          <Route path="/photos/:id" element={<ShowPhoto />}></Route> */}
        </Routes>
      </UserContext.Provider>
    </BrowserRouter>

  );
}

export default App;