import React, {useEffect, useState} from 'react'
import StarRating from './StarRating';
import { UserContext } from "../userContext";
import { useContext } from 'react'
import { AiFillStar } from "react-icons/ai";
import { AiOutlineStar } from "react-icons/ai";
import Button from './Button';

const moment = require('moment');

const Ratings = () => {
    const userContext = useContext(UserContext); 
    const [ratings, setRatings] = useState([])
    const [rating, setRating] = useState(0)
    
    var url = window.location.href
    var arr = url.split('/');
    const restId = arr[4]


    useEffect(function(){
        const getRatings = async function(){
            const res = await fetch('http://localhost:3001/ratings/'+restId);
            const data = await res.json();
            setRatings(data)
            console.log(data)
        }
        getRatings();
    }, []);

    async function submitRating(e){
        console.log(e)
        e.preventDefault();
        const res = await fetch("http://localhost:3001/ratings", {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                starRating: rating,
                date: new Date(),
                user_tk: userContext.user._id,
                restaurant_tk: restId
            })
        });
        const data = await res.json();
        console.log(typeof(data))
        setRatings([...ratings, data])
      }

    function test (index) {
        setRating(index)
    }

    function showStars(num){
        var stars = []
        for(var i=0; i<num; i++){
            stars.push(<AiFillStar className='text-slate-800' />)
        }
        for(i=0; i<5-num; i++){
            stars.push(<AiOutlineStar className='text-slate-800' />)
        }

        return stars
    }

    function getPrettyDate(date1){
        const time = moment(date1);
        return time.format("DD/MM/YY HH:mm")
      }

  return (
    <div>
        <UserContext.Consumer>
            {context => (  context.user &&
                <div className='flex mt-2 mb-4'>
                    <form onSubmit={submitRating} className="flex">
                        <div className='pt-2'>Add a rating: </div>
                        <StarRating submitRating2={test}/>
                        <div className='ml-2'><Button  text="submit"/></div>
                    </form>
                </div>
            )}    
        </UserContext.Consumer>
        <div>
        {ratings.map((rating) => (
            <div className=' mb-4 p-2' key={rating._id}>
            <div className='text-sm text-blue-700 flex border-2 border-slate-400 p-4 w-3/4'>
                {rating.user_tk.username}
                <p className='ml-4 text-lg text-black flex'>{showStars(rating.starRating)}</p>
                <p className='ml-auto mr-2 '>{getPrettyDate(rating.date)}</p>
            </div>
            </div>
        ))}
        </div>
    </div>
  )
}

export default Ratings