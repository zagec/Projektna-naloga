import {useEffect, useState} from "react";
import {useLocation} from "react-router-dom";

const UpdateUser = (props) => {
    const location = useLocation()
    const [user, setUsers] = useState({});
    useEffect(function () {
        const getUser = async function () {
            const res = await fetch("http://localhost:3001/users/" + location.state.id);
            const data = await res.json()
            setUsers(data)
        }
        getUser()
    }, []);
    return (

        <div className="w-3/5 ml-auto mr-auto mt-12 content-center">
            <div className="bg-white text-slate-800 p-6 mb-6 rounded-lg">
                <div>
                    <div className="mb-2 font-bold text-lg text-center">Username:<br/>
                        <input className="bg-gray-400 rounded-md" placeholder={user.username}/>
                    </div>
                </div>
                <div>
                    <div className="mb-2 font-bold text-lg text-center">Email:<br/>
                        <input className="bg-gray-400 rounded-md" placeholder={user.email}/>
                    </div>
                </div>
                <div className="text-center">
                    <input type="radio" name="admistatus"
                           value="true"/> True
                    <span> </span>
                    <input type="radio" name="admistatus"
                           value="false"/> False <br/>
                    <p>Creation date: {user.date}</p>
                </div>
            </div>
        </div>
    )
}

export default UpdateUser