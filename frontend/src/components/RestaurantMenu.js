import {useState, useEffect } from 'react'
import React from 'react'
import Dropdown from './Dropdown'
import Boop from './Boop'
import RestaurantsList from './RestaurantsList'
import {AiOutlineArrowUp} from 'react-icons/ai' 
import {AiOutlineArrowDown} from 'react-icons/ai' 
import {FaWindowClose} from 'react-icons/fa' 

const RestaurantMenu = ({ searchByType, setSearchByType }) => {
    const [restaurants, setRestaurants] = useState([]);
    const [tag, setTag ] = useState('');
    const [searchActive, setSearchActive ] = useState(false);
    const [searchBy , setSearchBy] = useState("abeceda")
    const [upOrDown , setUporDown] = useState("up")
    const [resPerPage , setResPerPage] = useState(15)
    const [searchByThisType, setSearchByThisType] = useState(null)

    const ponudbaPoVrsti = ['Meso', 'Vegetarijansko', 'Riba', 'Mešano', 'Solata', 'Pizza', 'Hitra hrana', 'Celiakiji prijazni obroki', 'Špageti', 'Burger', 'Sendvič', 'Juha', 'Burek', 'Raca', 'Piščanec', 'Svinjina', 'Govedina', 'Lazanja']
    
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

 
    function typeSearch(value) { 
        setSearchByThisType(value)
        const search = (value == 'abeceda') ? "byAbeceda" : "byPrice"
        const getRestaurants = async function(){
            const res = await fetch('http://localhost:3001/restaurants/byAbeceda/up/'+value);
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

    const data = ponudbaPoVrsti.map((vrsta) => {
        return <a className={searchByThisType == vrsta ? 'mt-1 font-bold text-warm' : 'mt-1 hover:underline'} onClick={() => typeSearch(vrsta)}>{vrsta}</a>
    })

    console.log(searchByThisType)

  return (
    <div className='w-3/5 justify-center ml-20 mt-12'>
        {!searchByType ? <div className='flex mb-2'>
            <div className='pt-1.5'>Razvrsti po: </div>
            <Dropdown setSearchBy={setSearchBy} onChange={(e) => handleChange(e)} values={['abeceda', 'cena']}/>
            {upOrDown == 'up' ? <AiOutlineArrowUp size={25} className={upOrDown == 'down' ? 'ml-2 mt-1.5 pointer-events:none' : 'ml-2 mt-1.5'} onClick={() => changedUporDown('down')} /> : 
                                <AiOutlineArrowDown size={25} className={upOrDown == 'up' ? 'ml-2 mt-1.5 pointer-events:none' : 'ml-2 mt-1.5'} onClick={() => changedUporDown('up')}/>}
            <div className='flex ml-auto mr-6'>
                <div className='pt-1.5'>Prikaz restavracij na stran: </div>
                <Dropdown setSearchBy={setResPerPage} onChange={(e) => setResPerPage(e)} values={[15, 5, 10, 20, 30]}/>
            </div>
        </div> : 
        <div>
            <div className='flex'>
                <p  className='mb-2 font-semibold text-slate-800'>Izberi vrsto hrane:</p>
                <p className='ml-auto mr-2'><Boop children={<FaWindowClose className="text-red-600" size={20} onClick={() => setSearchByType(false)}/>} /></p>
                </div>
            <div className='grid grid-cols-5 gap-4 ml-20'>
                {data}
            </div>
        </div>
        }
        <div className=''>
        {searchByType && <div className='flex mt-6 bg-slate-500 rounded-full p-2 w-1/2 text-center font-semibold text-slate-200 pl-5'>Restavracije, ki nudijo hrano "<p className='font-semibold'>{searchByThisType}</p>"</div>}
            <div className=' p-6'>
                {restaurants.length === 0 ? 
                    <p className='text-2xl ml-auto text-center mr-auto'>{tag === '' ? "Restavracije trenutno niso na voljo" : "Ni restavracij/e pod iskalnim nizom \"" + tag + "\""} {sad}</p>
                    :
                    <RestaurantsList restaurants={restaurants}  resPerPage={resPerPage} />
                }
            </div>
 
        </div>
    </div>
  )
}

export default RestaurantMenu