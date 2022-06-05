import RedirectButton from "./RedirectButton";

function User(props){
    return(
        <div className="bg-white rounded-xl text-slate-800 p-6 mb-6 transition ease-in-out hover:-translate-y-1 hover:scale-105 group hover:bg-slate-400 duration-300">
            <div className="mb-2 font-bold text-lg group-hover:text-white duration-300">{props.user.username}</div>
            <div className="group-hover:text-white duration-300">{props.user.email}</div>
            <input type="hidden" value={props.user._id}/>
            <RedirectButton name="Modify" value={props.user._id}/>
        </div>
    )
}

export default User