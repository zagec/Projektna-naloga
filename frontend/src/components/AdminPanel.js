import React from 'react'
import UserList from './UserList'
import {NavLink} from "react-router-dom";

const AdminPanel = () => {

    return (
        <>
            <div className="p-4">
                <NavLink to="/createUser"
                         className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-full">Create
                    user</NavLink>
            </div>
            <UserList/>
        </>
    )
}

export default AdminPanel