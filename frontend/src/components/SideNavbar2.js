import React from 'react'
import { useState } from 'react'
import {NavLink} from 'react-router-dom'
import { AiOutlineRight } from 'react-icons/ai'

const SideNav = () => {
    const [open, setOpen] = useState(false);
    const showMenu = '';
    const hideMenu = 'invisible';
  return (
    <div className='w-56'>
        { open ? 
        <div onMouseLeave={() => setOpen(false)} className={open && 'mt-2 visible bg-orange-300 rounded-tr-3xl rounded-br-3xl  border-b-2 border-r-2 h-80 border-slate-300' }>
            <div className='flex border-b-2 border-b-slate-500 ml-4 mr-16'>
                <div className='pt-4 pl-4 mb-2  text-xl text-slate-800 font-semibold'>
                    Meni
                </div>
            </div>
            <div className='text-base mt-4 px-4'>
                <div class="link link-underline link-underline-black mb-3"><NavLink className="hover:cursor-pointer" to="/findNearMe">Najdi restavracije v bližini</NavLink></div>
                <div class="link link-underline link-underline-black w-16 mb-3"><NavLink className="hover:cursor-pointer" to="/">Drugo</NavLink></div>
            </div>
        </div> : 
            <div onMouseOver={() => setOpen(true)} className='mt-2 flex'>
                <div className='rounded-tr-3xl rounded-br-3xl bg-orange-300 h-80 w-4'></div>
                <div className='h-16 mt-16 pt-4 rounded-tr-3xl rounded-br-3xl bg-orange-300 text-slate-700'><AiOutlineRight size={30}/></div>
            </div>
        }
        
    </div>
  )
}

export default SideNav