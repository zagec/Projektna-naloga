import {useState, useEffect} from 'react'
import React from 'react'
import Map from './Map';
import RestaurantsList from './RestaurantsList';
import {AiFillEdit} from 'react-icons/ai'
import {AiFillCheckCircle} from 'react-icons/ai'
import Boop from './Boop'


const RestaurantsNearMe = () => {
  const [restaurants, setRestaurants] = useState([]);
  const  [myPosition, setMyPosition] = useState([ 46.549567 , 15.649397])
  const  [distanceMeters, setDistanceMeters] = useState("200")
  const  [editState, setEditState] = useState(false)

  useEffect(function(){
      navigator.geolocation.getCurrentPosition(function(position) {
          // setMyPosition([position.coords.latitude,position.coords.longitude]);
          setMyPosition([46.558248,15.645244]);
          const search = distanceMeters /  1609.344
          const getRestaurants = async function(){
            // 46.558248, 15.645244
              // const res = await fetch('http://localhost:3001/restaurants/nearMe/' + position.coords.latitude + '/' +position.coords.longitude + '/' +search);
              const res = await fetch('http://localhost:3001/restaurants/nearMe/' + 46.558248 + '/' + 15.645244 + '/' +search);
              const data = await res.json();
              setRestaurants(data)
          }
          getRestaurants();
      });
  }, []);

  const restaurantsDisplay = restaurants.map((restaurant) => {
      return (<div key={restaurant.ime}>{restaurant.ime}</div>)
  })

  

    const markerIcon = {
      path: "/icon2.png",
      fillColor: '#0000ff',
      fillOpacity: 1.0,
      strokeWeight: 0,
      scale: 1.5
    }    

    const changeMeters = (e) => {
      console.log(e)
      // setDistance(e / 1609.344)
    }

    async function updateMeters(e){
      setEditState(!editState)
      const search = distanceMeters /  1609.344
      e.preventDefault();
      console.log(search)
      const getRestaurants = async function(){
          // const res = await fetch('http://localhost:3001/restaurants/nearMe/' + position.coords.latitude + '/' +position.coords.longitude + '/' +distance);
          const res = await fetch('http://localhost:3001/restaurants/nearMe/' + myPosition[0] + '/' +myPosition[1] + '/' +search);
          const data = await res.json();
          setRestaurants(data)
      }
      getRestaurants();

  }

        
  return(
    <div className='flex'>
      <div className='bg-slate-200 ml-12'>
        <div className='flex  w-5/6 ml-auto mr-auto'>
          <div className=' ml-auto mr-auto mt-4 text-center font-semibold bg-slate-400 rounded-2xl  h-12 pt-3 px-1 flex'>
              Najdel {restaurants.length} restavracij v radiusu 
              <p className={(!editState) ? 'mx-1.5' : 'hidden'}>{parseInt((distanceMeters), 10)}</p> 
              <input type="text" id="meters" name="meters" value={String(distanceMeters)} onChange={(e) => {setDistanceMeters(e.target.value)}} className={(editState) ? 'mx-1.5 w-12 h-6 pl-2' : 'hidden'} />
              metrov
          </div>
          <div className='mt-8'><Boop rotation={20} timing={150} children={<AiFillEdit size={20} className={(!editState) ? 'mx-1.5 hover:scale-110 text-blue-700' : 'hidden'} onClick={() => setEditState(!editState)}/>} /></div>
          <div className='mt-8'><Boop rotation={40} children={<AiFillCheckCircle size={20} className={(editState) ? 'mx-1.5 hover:scale-125 text-green-700 duration-200' : 'hidden'} onClick={(e) => {updateMeters(e)}}/>} /></div>
        </div>

          <div className=' p-6'>
            {restaurants.length === 0 ? 
                <p className='text-2xl ml-auto text-center mr-auto'> Restavracije trenutno niso na voljo</p>
                :
                <RestaurantsList restaurants={restaurants} resPerPage={5} />
            }
          </div>
      </div>
        <div className='ml-auto mr-12 mt-12'>
          <Map restaurants={restaurants} position={ [myPosition[0], myPosition[1]]} width={"700px"} height={"500px"} oneMarker={false}/>
          {/* <Map restaurants={restaurants} position={[myPosition[0], myPosition[1]]}/> */}
        </div>
    </div>
  )
}

export default RestaurantsNearMe