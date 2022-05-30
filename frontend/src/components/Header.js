import React from 'react'
import { UserContext } from "../userContext";
import { MdOutlineFastfood } from "react-icons/md";
import {NavLink} from 'react-router-dom'
import { Icon } from './Icon';

const Header = () => {
  return (
    <div className="bg-lemon p-3 text-xl flex text-slate-200 font-sans">
    <div className="ml-8 text-2xl font-bold flex">
        <Icon />
        Restavrantko
    </div>
  
    <div className="ml-auto mr-10">
      <NavLink className="mr-4 transition ease-in-out hover:text-slate-400 duration-300" to="/">Domov</NavLink>
      <UserContext.Consumer>
        {context => (
            context.user ? 
              <>
              {context.user.admin ?
                  <>
                    <NavLink className='mr-4 transition ease-in-out   group hover:text-slate-400 duration-300' to="/profile">Admin panel</NavLink>
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