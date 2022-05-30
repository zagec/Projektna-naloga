import React from 'react'
import { useState } from 'react'
import {NavLink} from 'react-router-dom'

const SideNav = () => {
    const [open, setOpen] = useState(false);
    const showMenu = '';
    const hideMenu = 'invisible';
  return (
    <div className='w-56'>
        { open ? 
        <div className={open && 'bg-slate-300 rounded-br-xl border-b-2 border-r-2 border-slate-500' }>
            <div className='flex'>
                <div className='pt-2 pl-4 text-xl text-lemon font-semibold'>
                    Meni
                </div>
                <button onClick={() => setOpen(!open)} className="flex items-center space-x-2 focus:outline-none ml-auto mr-4 ">
                    <span className="font-medium text-lg  w-2 h-10"></span>
                    <div className="w-6 flex items-center justify-center relative">
                        <span className={open ? 'translate-y-0 rotate-45 transform transition w-full h-px bg-current absolute' : '-translate-y-2 transform transition w-full h-px bg-current absolute' }></span>
                        <span className={open ? 'opacity-0 translate-x-3 transform transition w-full h-px bg-current absolute' : 'opacity-100 transform transition w-full h-px bg-current absolute'}></span>
                        <span className={open ? 'translate-y-0 -rotate-45 transform transition w-full h-px bg-current absolute' : 'translate-y-2 transform transition w-full h-px bg-current absolute'}></span>
                    </div>
                </button>
            </div>
            <div className='text-base mt-2 px-4'>
                <div class="link link-underline link-underline-black mb-3"><NavLink className="hover:cursor-pointer" to="/findNearMe">Najdi restavracije v bližini</NavLink></div>
                <div class="link link-underline link-underline-black w-16 mb-3"><NavLink className="hover:cursor-pointer" to="/">Drugo</NavLink></div>
            </div>
        </div> : 
        <div>
            <button onClick={() => setOpen(!open)} className="flex items-center space-x-2 focus:outline-none">
                <span className="font-medium text-lg  w-2 h-10"></span>
                <div className="w-6 flex items-center justify-center relative">
                    <span className={open ? 'translate-y-0 rotate-45 transform transition w-full h-px bg-current absolute' : '-translate-y-2 transform transition w-full h-px bg-current absolute' }></span>
                    <span className={open ? 'opacity-0 translate-x-3 transform transition w-full h-px bg-current absolute' : 'opacity-100 transform transition w-full h-px bg-current absolute'}></span>
                    <span className={open ? 'translate-y-0 -rotate-45 transform transition w-full h-px bg-current absolute' : 'translate-y-2 transform transition w-full h-px bg-current absolute'}></span>
                </div>
            </button>
           
        </div>
        }
        
    </div>
  )
}

export default SideNav