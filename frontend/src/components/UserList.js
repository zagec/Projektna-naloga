import Users from "./Users";
import React, {useEffect, useState} from "react";

const UserList = () => {
    const [users, setUsers] = useState([]);
    useEffect(function () {
        const getUsers = async function () {
            const res = await fetch("http://localhost:3001/users/");
            const data = await res.json()
            setUsers(data)
            console.log(data)
        }
        getUsers()
    }, []);

    return (
        <div className="w-3/5 ml-auto mr-auto mt-12">
            <Users users={users}/>
        </div>
    )
}

export default UserList