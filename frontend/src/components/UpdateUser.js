import {useEffect, useState} from "react";
import {useLocation} from "react-router-dom";
import React from "react";

const UpdateUser = (props) => {
    const location = useLocation()
    const [user, setUsers] = useState([]);
    const [username] = useState([]);
    const [admin] = useState([]);
    const [email] = useState([]);
    useEffect(function () {
        const getUser = async function () {
            const res = await fetch("http://localhost:3001/users/" + location.state.id);
            const data = await res.json()
            setUsers(data)
        }
        getUser()
    }, []);

    async function update(e) {
        e.preventDefault()
        const res = await fetch("http://localhost:3001/users", {
            method: "PUT",
            credentials: 'include',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                username: username,
                email: email,
                admin: admin
            })
        })
        const data = await res.json()
        if (data._id !== undefined) {
            window.location.href = "/adminpanel";
        }
    }

    return (
        <form className="ml-auto  mr-auto " onSubmit={update}>
            <div className="w-3/5 ml-auto mr-auto mt-12 content-center">
                <div className="bg-white text-slate-800 p-6 mb-6 rounded-lg">
                    <div>
                        <div className="mb-2 font-bold text-lg text-center">Username:<br/>
                            <input className="bg-gray-400 rounded-md" placeholder={user.username} name="username"/>
                        </div>
                    </div>
                    <div>
                        <div className="mb-2 font-bold text-lg text-center">Email:<br/>
                            <input className="bg-gray-400 rounded-md" placeholder={user.email} name="email"/>
                        </div>
                    </div>
                    <div className="text-center">
                        <input type="radio" name="admin"
                               value="true"/> True
                        <span> </span>
                        <input type="radio" name="admin"
                               value="false"/> False <br/>
                        <p>Creation date: {user.date}</p>
                    </div>
                    <div className="w-48  ml-auto mr-auto">
                        <button type="submit" name="send" className="bg-gradient-to-b from-slate-500  to-slate-100 p-2 rounded-xl text-base text-center font-semibold w-48 text-slate-800 duration-200">Posodobi</button>
                    </div>
                </div>
            </div>
        </form>
    )
}

export default UpdateUser