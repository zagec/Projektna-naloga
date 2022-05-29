import {NavLink} from "react-router-dom";

function RedirectButton(props) {
    const link = "/user:"+props.value
    return (
        <div className="pt-2">
            <NavLink className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-full" to={link} >
                {props.name}
            </NavLink>
        </div>
    )
}

export default RedirectButton