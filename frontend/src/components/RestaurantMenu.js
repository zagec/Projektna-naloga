import {useState, useEffect } from 'react'
import React from 'react'
import Dropdown from './Dropdown'
import RestaurantsList from './RestaurantsList'

const RestaurantMenu = () => {
    const [restaurants, setRestaurants] = useState([]);
    const [tag, setTag ] = useState('');
    const [searchActive, setSearchActive ] = useState(false);
    const sad = ':('    
    useEffect(function(){
        const getRestaurants = async function(){
            const res = await fetch('http://localhost:3001/restaurants/');
            const data = await res.json();
            setRestaurants(data)
        }
        getRestaurants();
    }, []);



  return (
    <div className='w-3/5 justify-center ml-20 mt-12'>
        <div className='flex mb-2'>
            <div className='pt-1.5'>Razvrsti po: </div>
            <Dropdown />
        </div>
        <div className=''>
            <div className=' p-6'>
                {restaurants.length === 0 ? 
                    <p className='text-2xl ml-auto text-center mr-auto'>{tag === '' ? "Restavracije trenutno niso na voljo" : "Ni restavracij/e pod iskalnim nizom \"" + tag + "\""} {sad}</p>
                    :
                    <RestaurantsList restaurants={restaurants} />
                }
            </div>
        </div>
    </div>
  )
}

export default RestaurantMenu