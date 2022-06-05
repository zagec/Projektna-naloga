import {useState, useEffect } from 'react'
import React from 'react'
import Dropdown from './Dropdown'
import RestaurantsList from './RestaurantsList'
import {AiOutlineArrowUp} from 'react-icons/ai' 
import {AiOutlineArrowDown} from 'react-icons/ai' 

const RestaurantMenu = () => {
    const [restaurants, setRestaurants] = useState([]);
    const [tag, setTag ] = useState('');
    const [searchActive, setSearchActive ] = useState(false);
    const [searchBy , setSearchBy] = useState("abeceda")
    const [upOrDown , setUporDown] = useState("up")

    const sad = ':('    
    useEffect(function(){
        const getRestaurants = async function(){
            const res = await fetch('http://localhost:3001/restaurants/byAbeceda/' + upOrDown);
            const data = await res.json();
            setRestaurants(data)
        }
        getRestaurants();
    }, []);

    function handleChange( value) { 
        setSearchBy(value)
        console.log(value)
        const search = (value == 'abeceda') ? "byAbeceda" : "byPrice"
        const getRestaurants = async function(){
            const res = await fetch('http://localhost:3001/restaurants/'+ search + '/' + upOrDown);
            const data = await res.json();
            setRestaurants(data)
        }
        getRestaurants();
    }

    function changedUporDown(value){
        setUporDown(value)
        const search = (searchBy == 'abeceda') ? "byAbeceda" : "byPrice"
        console.log(search + value)
        const getRestaurants = async function(){
            const res = await fetch('http://localhost:3001/restaurants/' + search + '/' + value);
            const data = await res.json();
            setRestaurants(data)
        }
        getRestaurants();
    }

  return (
    <div className='w-3/5 justify-center ml-20 mt-12'>
        <div className='flex mb-2'>
            <div className='pt-1.5'>Razvrsti po: </div>
            <Dropdown setSearchBy={setSearchBy} onChange={(e) => handleChange(e)}/>
            {upOrDown == 'up' ? <AiOutlineArrowUp size={25} className={upOrDown == 'down' ? 'ml-2 mt-1.5 pointer-events:none' : 'ml-2 mt-1.5'} onClick={() => changedUporDown('down')} /> : 
                                <AiOutlineArrowDown size={25} className={upOrDown == 'up' ? 'ml-2 mt-1.5 pointer-events:none' : 'ml-2 mt-1.5'} onClick={() => changedUporDown('up')}/>}
        </div>
        <div className=''>
            <div className=' p-6'>
                {restaurants.length === 0 ? 
                    <p className='text-2xl ml-auto text-center mr-auto'>{tag === '' ? "Restavracije trenutno niso na voljo" : "Ni restavracij/e pod iskalnim nizom \"" + tag + "\""} {sad}</p>
                    :
                    <RestaurantsList restaurants={restaurants}  resPerPage={15} />
                }
            </div>
        </div>
    </div>
  )
}

export default RestaurantMenu