import React, {useState, useEffect} from 'react'
import { GoogleMap, useJsApiLoader,  MarkerClusterer, Marker, InfoWindow } from '@react-google-maps/api';


const Map = ({ restaurants, position, width, height, oneMarker}) => {
    const [selectedRestaurant, setSelectedRestaurant] = useState(null)
    const [zoom, setZoom] = useState(13)


    const center = {lat: position[0], lng: position[1] };

      const { isLoaded } = useJsApiLoader({
        id: 'google-map-script',
        googleMapsApiKey: "AIzaSyDGDkoE7TNwBSfARe6vwIUKWQ4S101auzc",
        language: 'sl'
      })
    
      const [map, setMap] = React.useState(null)
    
      const onLoad = React.useCallback(function callback(map) {
        const bounds = new window.google.maps.LatLngBounds(center);
        map.fitBounds(bounds);
        setMap(map)
      }, [])
    
      const onUnmount = React.useCallback(function callback(map) {
        setMap(null)
      }, [])
  

      const showIfOpened = () => {
        // const d = new Date("June 3, 2022 15:15:00");
        const d = new Date();
        let day = d.getDay();
        if(day == 0) day = 6
        else day = day -1
        let hours = d.getHours();
        let mins = d.getMinutes();

        let restaurantsWorkDay = selectedRestaurant.delovniCas[day];
        let restaurantsStartHour = parseInt(restaurantsWorkDay.substring(0,2));
        let restaurantsStartMins = parseInt(restaurantsWorkDay.substring(3,5));
        let restaurantsEndHour = parseInt(restaurantsWorkDay.substring(6,8));
        let restaurantsEndMins = parseInt(restaurantsWorkDay.substring(9,11));

        let closesIn = restaurantsEndHour - hours

        if(restaurantsWorkDay == 'Zaprto' || restaurantsStartHour > hours || restaurantsEndHour <= hours){
            return ( <p className='text-red-700 font-semibold'>Zaprto</p> )
        }
        else if((restaurantsStartHour < hours && restaurantsEndHour > hours) || restaurantsStartHour == hours && restaurantsEndHour > hours){
            let text = ''
            if(closesIn == 1) text = "uro"
            else if(closesIn == 2) text = "uri"
            else if(closesIn == 3) text = "ure"
            else text = "ur"
            return ( <div className='flex text-xs'>
                        <p className='text-green-700 font-semibold mr-3'>Odprto</p>
                        <p>Zapre se čez {closesIn} {text}</p>
                    </div> )
        }
       
        return <>{day}{restaurantsWorkDay} {hours}</>
      }

   

    function Map(){

        const containerStyle = {
            width: width,
            height: height
        };

        return (
        <GoogleMap mapContainerStyle={containerStyle} center={center} defaultZoom={12} onLoad={onLoad} onUnmount={onUnmount} clickableIcons={false}>
          <MarkerClusterer>
            {(clusterer) =>
               !oneMarker ? restaurants.map((restaurant) => (
                // clusterer={clusterer}
                <Marker 
                    key={restaurant.loc[0] + restaurant.ime} 
                    position={{lat: restaurant.loc[0], lng: restaurant.loc[1]}} 
                    cursor={"pointer"} 
                    onClick={() => {setSelectedRestaurant(restaurant)}}
                    clusterer={clusterer}/>
              )) :
              <Marker 
              key={restaurants.loc[0] + restaurants.ime} 
              position={{lat: restaurants.loc[0], lng: restaurants.loc[1]}} 
              cursor={"pointer"} 
              onClick={() => {setSelectedRestaurant(restaurants)}}
              clusterer={clusterer}/>
            }
          </MarkerClusterer>
          {selectedRestaurant && (
              <InfoWindow  position={{lat: selectedRestaurant.loc[0], lng: selectedRestaurant.loc[1]}} >
                  <div>
                      <div className='font-semibold'>{selectedRestaurant.ime}</div>
                      <div className=' mt-1'>
                          {showIfOpened()}
                          <a className='text-xs text-blue-700 ml-auto mr-1 cursor-pointer hover:text-blue-900 hover:font-semibold' href={`/restaurant/${selectedRestaurant._id}`}>Pojdi na restavracijo</a>
                        </div>
                  </div>
              </InfoWindow>
          )}
      </GoogleMap>
        )
    }

  return isLoaded ?  (
    <div>{Map()}</div>
  ) : <></>
}

export default Map