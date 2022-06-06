import React,{useState} from 'react'
import UserList from './UserList'
import {NavLink} from "react-router-dom";

const AdminPanel = () => {
    const [error, setError] = useState("")

    async function importData(e){
        const getRestaurants = async function(){
            const res = await fetch('http://localhost:3001/restaurants/importDataFromJson/restavracije.json');
            const data = await res.json();
            setError(data)
            console.log(data)
        }
        getRestaurants();
  
    }
  

    return (
        <>
            <div className="p-4 flex">
                <div><NavLink to="/createUser"
                         className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-full mr-4">Create
                    user</NavLink></div>
            <div className='bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-full ' onClick={() => importData()}>Import Data</div>{error}
            </div>
            <UserList/>
        </>
    )
}

export default AdminPanel