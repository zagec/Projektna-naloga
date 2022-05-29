import { useContext, useState, userContext } from 'react';
import { UserContext } from '../userContext';
import { IoPersonCircleSharp } from "react-icons/io5";
import { MdOutgoingMail } from "react-icons/md";
import { AiFillLock } from "react-icons/ai";
import { FaRegThumbsUp } from "react-icons/fa";
import { BsFillPersonFill } from "react-icons/bs";
import { MdEmail } from "react-icons/md";
import {NavLink} from 'react-router-dom'
import React from 'react'
import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

const Registration = () => {
    const [username, setUsername] = useState([]);
    const [password, setPassword] = useState([]);
    const [email, setEmail] = useState([]);
    const [repPassword, setRepPassword] = useState([]);
    const [error, setError] = useState([]);
    const [successfullyRegistered, setSuccessfullyRegistered] = useState(false);
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
            window.location.href="/login";
        }
        else{
            if(data.message === "User was registered successfully! Please check your email"){
                setSuccessfullyRegistered(true);
            }
            else{
                setUsername("");
                setPassword("");
                setRepPassword("");
                setEmail("");
                setError("Neuspešna registracija :(");
            }
        }
    }


  return (
      <div className='mt-12 ml-auto mr-auto px-6 w-1/3 '>
          {!successfullyRegistered ?
            <form className="ml-auto  mr-auto " onSubmit={register}>
                <div className='bg-slate-100 p-6 rounded-xl'>
                    <div className='font-bold text-xl text-slate-600'>
                        Ustvari račun
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
                    <div className='flex'>
                        <div className='text-xs ml-4 text-blue-700 hover:cursor-pointer hover:animate-pulse'><NavLink to='/login'>Račun že ustvarjen? Prijavite se!</NavLink></div>
                        </div>
                </div>
                <div className='w-48  ml-auto mr-auto'>

                    <button type="submit" name="send" className="bg-gradient-to-b from-slate-500  to-slate-100 p-2 rounded-b-xl text-base text-center font-semibold w-48 text-slate-800 duration-200">Registriraj se</button>
                    {/* <button type="submit" name="send" className="m-2 p-10 text-white rounded-xl transition-all duration-500 bg-gradient-to-tl from-purple-800 via-purple-600 to-purple-400 bg-size-200 bg-pos-0 hover:bg-pos-100">Registriraj se</button> */}
                </div>
            </form>
        :
            <div className='bg-slate-100 p-6 rounded-xl'>
                <div className='mb-3   flex'>
                    <FaRegThumbsUp className='text-green-700' size={20} />
                    <MdOutgoingMail className='text-blue-700 ml-auto mr-2' size={20} />
                </div>
                <div className='flex'>
                    Uspešno ste registrirani. Na gmail smo vam poslali potrditveni link.
                </div>
            </div>
        }
        
      </div>
  )
}

export default Registration