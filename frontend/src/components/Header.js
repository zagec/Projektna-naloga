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
      <NavLink className="mr-4" to="/">Domov</NavLink>
      <UserContext.Consumer>
        {context => (
            context.user ?
              <>
                <NavLink className='mr-4' to="/profile">Profil</NavLink>
                <NavLink className='mr-4' to="/logout">Odjavi se</NavLink>
              </>
            :
              <>
                <NavLink className='mr-4' to='/login'>Prijava</NavLink>
                <NavLink className='' to='/register'>Registracija</NavLink>
              </>
          )}
        </UserContext.Consumer>
    </div>
</div>
  )
}

export default Header