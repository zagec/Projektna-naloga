import {Link, useNavigate} from "react-router-dom"

function RedirectButton(props) {
    const navigate = useNavigate()
    const value = props.value
    const toUpdateUser = ()=>{
        navigate("/user/"+props.value,{state:{id:value}})
    }
    return (
        <div className="pt-2">
            <a className="bg-blue-500 hover:bg-blue-700 hover:cursor-pointer text-white font-bold py-2 px-4 rounded-full" onClick={toUpdateUser}>
                {props.name}
            </a>
        </div>
    )
}

export default RedirectButton