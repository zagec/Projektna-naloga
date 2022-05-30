import React from 'react'
import RestaurantMenu from './RestaurantMenu'
import SideNav from './SideNav'
import SideNav2 from './SideNavbar2'

const Home = () => {
    
  return (
    <div className='flex'>
        <SideNav2 />
        <RestaurantMenu  />
    </div>
  )
}

export default Home