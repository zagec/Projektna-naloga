// import Button from './Button';
import { useContext, useState, userContext } from 'react';
import { UserContext } from '../userContext';
import { IoPersonCircleSharp } from "react-icons/io5";
import { AiFillLock } from "react-icons/ai";
import { BsFillPersonFill } from "react-icons/bs";
import {NavLink} from 'react-router-dom'
import React from 'react'

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState("");
    const userContext = useContext(UserContext); 

    async function login(e){
        e.preventDefault();
        const res = await fetch("http://localhost:3001/users/login", {
            method: "POST",
            credentials: "include",
            headers: { 'Content-Type': 'application/json'},
            body: JSON.stringify({
                username: username,
                password: password
            })
        });
        const data = await res.json();
        if(data._id !== undefined){
            userContext.setUserContext(data);
            window.location.href="/";
        } else {
            setUsername("");
            setPassword("");
            setError("Invalid username or password");
        }
    }

  return (
    <div className='mt-12'>
        <div className='ml-auto mr-auto w-1/3'>
            <form className="ml-auto mr-auto" onSubmit={login}>
                <div className='bg-slate-300 px-6 rounded-xl'>
                    <IoPersonCircleSharp className='ml-auto mr-auto ' size={50}/>
                    <div className="mb-6 flex mt-5">
                        <div className='bg-sunshine w-8 text-center'><BsFillPersonFill className='pt-2 ml-auto mr-auto' size={25}/></div>
                        <input className='w-11/12 text-base p-1 bg-slate-600 text-white rounded-r-xl' type="text" id="username" placeholder='Uporabniško ime ali email' name="username" required value={username} onChange={(e) => (setUsername(e.target.value))} /><br />
                    </div>
                    <div className="mb-2 flex">
                        <div className='bg-sunshine w-8 text-center'><AiFillLock className='pt-2 ml-auto mr-auto' size={25} /></div>
                        <input className='w-11/12 text-base p-1 bg-slate-600 text-white rounded-r-xl' type="password" id="password"placeholder='Geslo' name="password" required value={password} onChange={(e) => (setPassword(e.target.value))}/><br />
                    </div>
                    <label className='text-sm text-red-700 font-bold ml-8 animate-pulse'>{error}</label>
                    <div className='flex'>
                    <div className='text-xs pb-3 ml-4 text-blue-700 hover:cursor-pointer hover:animate-pulse'><NavLink to='/register'>Pozabljeno geslo?</NavLink></div>
                        <div className='text-xs pb-3 ml-auto mr-4 text-blue-700 hover:cursor-pointer hover:animate-pulse'><NavLink to='/register'>Še nimate računa? Registrirajte se!</NavLink></div>
                    </div>
                </div>
                <div className='w-48  ml-auto mr-auto'>

                <button type="submit" name="send" className="bg-gradient-to-b from-slate-500 via-slate-400 to-slate-300 p-2 rounded-b-xl text-base text-center font-semibold w-48 text-slate-800">Prijavi se</button>
                </div>
            </form>
        </div>
    </div>
  )
}

export default Login