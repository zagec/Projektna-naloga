import React from 'react'
import { useEffect } from 'react';

const Verify = () => {

    useEffect(function(){
        const getRestaurants = async function(){
            var url = window.location.href;
            var arr = url.split('/');
            const res = await fetch('http://localhost:3001/users/confirm/'+arr[4]);
            const data = await res.json();
            window.location.href ="/"
        }
        getRestaurants();
    }, []);

  return (
    <div></div>
  )
}
export default Verify