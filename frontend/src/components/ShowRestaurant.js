import React, {useEffect, useState} from 'react'
import Map from './Map'

const ShowRestaurant = () => {
    const [restaurant, setRestaurant] = useState(null)
    useEffect(function(){
        var url = window.location.href;
        var arr = url.split('/');
        const getRestaurants = async function(){
            const res = await fetch('http://localhost:3001/restaurants/' + arr[4]);
            const data = await res.json();
            setRestaurant(data)
            console.log(data)
        }
        getRestaurants();
    }, []);

    const meni = (restaurant) && restaurant.meni.map((food) => {
        return <div key={food} className='bg-slate-300 mt-2 text-sm p-3'>
                    <p className='font-medium'>{food[0]}</p>
                    <div className='ml-4 mt-2'>
                        {food[1][0] != "" && <div>-{food[1][0]}</div>}
                        {food[1][1] != "" && <div>-{food[1][1]}</div>}
                        {food[1][2] != "" && <div>-{food[1][2]}</div>}
                    </div>
                </div>
    })

    

  return restaurant ? (
    <div className='grid grid-cols-2'>
        <div className='w-3/4 ml-auto mr-auto'>
            <div className='bg-slate-300  rounded-3xl  mt-16 p-4 pb-8'>

                <p className='text-2xl font-bold text-slate-200 text-rainbow-animation'>
                    {restaurant.ime}                
                </p>
                <div className='flex mt-2'>
                    <div className=''>
                        <p className='text-slate-700 '>{restaurant.lokacija}</p>
                        <div className='mt-4 text-sm flex'>Cena menija brez študentskega bona: <p className='ml-2'>{restaurant.cenaBrezStudentskegaBona}</p>€</div>
                        <div className='mt-2 text-sm flex'>Cena menija z študentskim bonom: <p className='ml-2'>{restaurant.cenaSStudentskimBonom}</p>€</div>
                    </div>

                    <div className='text-sm leading-4 grid grid-cols-2 w-40 ml-auto mr-4 mt-2'>
                        <div>
                            <p>Ponedeljek:</p>
                            <p>Torek:</p>
                            <p>Sreda:</p>
                            <p>Četrtek:</p>
                            <p>Petek:</p>
                            <p>Sobota:</p>
                            <p>Nedelja:</p>
                        </div>
                        <div>
                            <p>{restaurant.delovniCas[0]}</p>
                            <p>{restaurant.delovniCas[1]}</p>
                            <p>{restaurant.delovniCas[2]}</p>
                            <p>{restaurant.delovniCas[3]}</p>
                            <p>{restaurant.delovniCas[4]}</p>
                            <p>{restaurant.delovniCas[5]}</p>
                            <p>{restaurant.delovniCas[6]}</p>
                        </div>
                    </div>
            </div>
            <div className='mt-12 ml-auto mr-auto'>
                <Map restaurants={restaurant} position={ [restaurant.loc[0],restaurant.loc[1]]} width={"500px"} height={"400px"} oneMarker={true}/>
            </div>
            </div>
        </div>
        <div className='mr-2 mt-4 pt-12'>
            <p className='text-2xl font-semibold text-lemon w-24 text-center rounded-3xl'>Meni:</p>
            <div className='mr-4'>{meni}</div>
        </div>
    </div>
  ) : <></>
}

export default ShowRestaurant