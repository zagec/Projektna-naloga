import React, {useState} from 'react'
import RestaurantMenu from './RestaurantMenu'
import SideNav from './SideNav'
import SideNav2 from './SideNavbar2'

const Home = () => {
  const [searchByType, setSearchByType] = useState(false);

    
  return (
    <div className='flex'>
        <SideNav2  onChange={(e) => setSearchByType(e)}/>
        <RestaurantMenu searchByType={searchByType} />
    </div>
  )
}

export default Home