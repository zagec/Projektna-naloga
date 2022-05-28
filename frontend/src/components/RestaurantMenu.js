import {useState, useEffect } from 'react'
import React from 'react'
import Dropdown from './Dropdown'

const RestaurantMenu = () => {
    const [restaurants, setRestaurants] = useState([]);
    const [tag, setTag ] = useState('');
    const [searchActive, setSearchActive ] = useState(false);
    
    useEffect(function(){
        const getRestaurants = async function(){
            const res = await fetch('http://localhost:3001/restaurants/');
            const data = await res.json();
            setRestaurants(data)
        }
        getRestaurants();
    }, []);


  return (
    <div className='w-3/5 ml-auto mr-auto mt-20'>
        <div className='flex mb-2'>
            <div className='pt-1.5'>Razvrsti po: </div>
            <Dropdown />
        </div>
        <div className='border-4 border-sunshine'>
            {restaurants.length}
        </div>
    </div>
  )
}

export default RestaurantMenu