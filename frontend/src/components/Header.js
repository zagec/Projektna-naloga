import React, {useState, useEffect} from 'react'
import { UserContext } from "../userContext";
import { MdOutlineFastfood } from "react-icons/md";
import {NavLink} from 'react-router-dom'
import { Icon } from './Icon';
import { AiOutlineSearch } from "react-icons/ai";

const Header = () => {
  const [searchActive, setSearchActive ] = useState(false);
  const [searchName, setSearchName] = useState("")

  return (
    <div className="bg-lemon p-3 text-xl flex text-slate-200 font-sans">
    <NavLink className="hover:cursor-pointer" to="/">
      <div className="ml-8 text-2xl font-bold flex">
        <Icon />
        Restavrantko
      </div>
    </NavLink>

    <div>
      <div className='flex'>
        <div className='ml-10 pt-2 hover:cursor-pointer hover:text-orange-500' onClick={() => setSearchActive(!searchActive)}>
            <AiOutlineSearch />
        </div>
        <div className={!searchActive ? 'hidden' : 'visible'}>
            <form className="flex" onSubmit={(e) => console.log("submit")}>
                <input type='text' className="rounded-xl pl-2 border-orange-400 border w-80 ml-28 mr-auto" size="10"  value={"d"} onChange={(e) => {console.log("click")}}></input>
            </form>
        </div>
     </div>
    </div>
  
    <div className="ml-auto mr-10">
      <NavLink className="mr-4 transition ease-in-out hover:text-slate-400 duration-300" to="/">Domov</NavLink>
      <UserContext.Consumer>
        {context => (
            context.user ? 
              <>
              {context.user.admin ?
                  <>
                    <NavLink className='mr-4 transition ease-in-out   group hover:text-slate-400 duration-300' to="/adminpanel">Admin panel</NavLink>
                  </>
                  :
                  <>
                  </>
              }
                <NavLink className='mr-4 transition ease-in-out   group hover:text-slate-400 duration-300' to="/profile">Profil</NavLink>
                <NavLink className='mr-4 transition ease-in-out  group hover:text-slate-400 duration-300' to="/logout">Odjavi se</NavLink>
              </>
            :
              <>
                <NavLink className='mr-4 transition ease-in-out  group hover:text-slate-400 duration-300' to='/login'>Prijava</NavLink>
                <NavLink className='transition ease-in-out  group hover:text-slate-400 duration-300' to='/register'>Registracija</NavLink>
              </>
          )}
        </UserContext.Consumer>
    </div>
</div>
  )
}

export default Header