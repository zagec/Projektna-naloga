import {useState, useEffect} from 'react'
import { AiFillStar } from "react-icons/ai";
import { AiOutlineStar } from "react-icons/ai";

const Restaurant = ({ restaurant }) => {
    const location = (restaurant.lokacija).substring(0, (restaurant.lokacija).indexOf('('));
    const [avgRating, setAvgRating] = useState(0)

    useEffect(function(){
        const getRatings = async function(){
            const res = await fetch('http://localhost:3001/ratings/'+restaurant._id);
            const data = await res.json();
            let ratings = 0;
            data.forEach((rating) => {
                ratings += rating.starRating
            })
            if(data.length != 0)
                setAvgRating(ratings/data.length)
            else 
                setAvgRating("ocen še ni")
        }
        getRatings();
    }, []);

    function showStars(num){
        var stars = []
        num = Math.round(num)
        console.log(num)
        for(var i=0; i<num; i++){
            stars.push(<AiFillStar className='text-slate-800' />)
        }
        for(i=0; i<5-num; i++){
            stars.push(<AiOutlineStar className='text-slate-800' />)
        }

        return stars
    }

  return (
    <div className='bg-white text-slate-800 p-6 mb-6 transition ease-in-out delay-150 hover:-translate-y-1 hover:scale-105 group hover:bg-slate-400 duration-300 rounded-tl-3xl rounded-br-3xl'>
        <div className='mb-2 font-bold text-lg group-hover:text-white delay-150 duration-300'>
            {restaurant.ime}
        </div>
            <div className="group-hover:text-white delay-150 duration-300">
                <div className="ml-4 flex">
                    <div>
                        <div className="flex">Povprečna ocena: <p className="ml-2 flex ">{avgRating != 'ocen še ni' ? <p className='mt-1 flex'>{showStars(avgRating)}</p> : 'ocen še ni'}</p></div>
                        <div className="mt-1.5 text-sm text-gray-600">{location}</div>

                    </div>
                    <div className="ml-auto mr-10 text-sm">
                        <div className="flex">Osnovna cena: <p className="ml-2">{restaurant.cenaBrezStudentskegaBona}</p></div>
                        <div className="flex">Cena z študentskim bonom: <p className="ml-2">{restaurant.cenaSStudentskimBonom}</p></div>
                    </div>
                </div>
            </div>
    </div>
  )
}

export default Restaurant