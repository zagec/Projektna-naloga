import React, {useState} from "react";
import {BsFillPersonFill} from "react-icons/bs";
import {MdEmail} from "react-icons/md";
import {AiFillLock} from "react-icons/ai";

const CreateUser = ()=>{
    const [username, setUsername] = useState([]);
    const [password, setPassword] = useState([]);
    const [email, setEmail] = useState([]);
    const [repPassword, setRepPassword] = useState([]);
    const [error, setError] = useState([]);

    async function register(e){
        e.preventDefault();
        const res = await fetch("http://localhost:3001/users", {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                email: email,
                username: username,
                password: password,
                repPassword: repPassword
            })
        });
        const data = await res.json();
        if(data._id !== undefined){
            window.location.href="/adminpanel";
        }
        else{
            setUsername("");
            setPassword("");
            setEmail("");
            setError("Neuspešna registracija :(");
        }
    }

    return (
        <div className='mt-12 ml-auto mr-auto px-6 w-1/3 '>
            <form className="ml-auto  mr-auto " onSubmit={register}>
                <div className='bg-slate-100 p-6 rounded-xl'>
                    <div className='font-bold text-xl text-slate-600'>
                        Ustvari uporabnika
                    </div>
                    <div className="mb-6 flex mt-5">
                        <div className='bg-sunshine w-8 text-center'><BsFillPersonFill className='pt-2 ml-auto mr-auto' size={25}/></div>
                        <input className='w-11/12 text-base p-1 bg-slate-600 text-white rounded-r-xl' type="text" id="username" placeholder='Uporabniško ime ali email' name="username" required value={username} onChange={(e) => (setUsername(e.target.value))} /><br />
                    </div>
                    <div className="mb-6 flex">
                        <div className='bg-sunshine w-8 text-center'><MdEmail className='pt-2 ml-auto mr-auto' size={25} /></div>
                        <input className='w-11/12 text-base p-1 bg-slate-600 text-white rounded-r-xl' type="text" id="mail"placeholder='E-pošta' name="mail" value={email} onChange={(e)=>(setEmail(e.target.value))} required/><br />
                        <label className="error" id="mailErr"></label><br />
                    </div>
                    <div className="mb-6 flex">
                        <div className='bg-sunshine w-8 text-center'><AiFillLock className='pt-2 ml-auto mr-auto' size={25} /></div>
                        <input className='w-11/12 text-base p-1 bg-slate-600 text-white rounded-r-xl' type="password" id="password"placeholder='Geslo' name="password" required value={password} onChange={(e) => (setPassword(e.target.value))}/><br />
                    </div>
                    <div className="mb-2 flex">
                        <div className='bg-sunshine w-8 text-center'><AiFillLock className='pt-2 ml-auto mr-auto' size={25} /></div>
                        <input className='w-11/12 text-base p-1 bg-slate-600 text-white rounded-r-xl' type="password" id="repPassword"placeholder='Ponovi geslo' name="repPassword" value={repPassword} onChange={(e)=>(setRepPassword(e.target.value))} required /><br />
                        <label className="error" id="repPswrdErr"></label> <br />
                    </div>
                    <label className='text-sm text-red-700 ml-8 animate-pulse'>{error}</label>
                </div>
                <div className='w-48  ml-auto mr-auto'>
                    <button type="submit" name="send" className="bg-gradient-to-b from-slate-500  to-slate-100 p-2 rounded-b-xl text-base text-center font-semibold w-48 text-slate-800 duration-200">Ustvari</button>
                </div>
            </form>
        </div>
    )
}
export default CreateUser