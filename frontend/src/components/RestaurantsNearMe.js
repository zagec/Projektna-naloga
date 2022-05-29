import {useState, useEffect} from 'react'

const RestaurantsNearMe = () => {
    const [restaurants, setRestaurants] = useState([]);
    const  [myPosition, setMyPosition] = useState([])


    useEffect(function(){
        navigator.geolocation.getCurrentPosition(function(position) {
            setMyPosition([position.coords.latitude,position.coords.longitude]);
            const getRestaurants = async function(){
                const res = await fetch('http://localhost:3001/restaurants/nearMe/' + position.coords.latitude + '/' +position.coords.longitude + '/10');
                const data = await res.json();
                setRestaurants(data)
                console.log(data)
               
            }
            getRestaurants();
        });
        }, []);

        // console.log(restaurants)
        // const displayRest = restaurants
        // .slice(0, 10)
        // .map((restaurant) => {
        //    return (
        //        <div>{restaurant.location_id.name}</div>
        //    )
        // })

  return (
    <div>
        sd
    </div>
  )
}

export default RestaurantsNearMe