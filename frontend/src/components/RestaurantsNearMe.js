import {useState, useEffect} from 'react'

const RestaurantsNearMe = () => {
    const [restaurants, setRestaurants] = useState([]);
    const  [myPosition, setMyPosition] = useState([])
    const  [distance, setDistamce] = useState(0.2)


    useEffect(function(){
        navigator.geolocation.getCurrentPosition(function(position) {
            setMyPosition([position.coords.latitude,position.coords.longitude]);
            const getRestaurants = async function(){
                const res = await fetch('http://localhost:3001/restaurants/nearMe/' + position.coords.latitude + '/' +position.coords.longitude + '/' +distance);
                const data = await res.json();
                setRestaurants(data)
                console.log(data)
               
            }
            getRestaurants();
        });
        }, []);

        const restaurantsDisplay = restaurants.map((restaurant) => {
            return (<div>{restaurant.ime}</div>)
        })
  return (
    <div>
        <div>
            Najdel {restaurants.length} restavracij v radiusu {parseInt((distance * 1609.344), 10)} metrov
        </div>

       <div className='font-bold'>Restavracije</div> 
        <div>
            {restaurantsDisplay}
        </div>
    </div>
  )
}

export default RestaurantsNearMe