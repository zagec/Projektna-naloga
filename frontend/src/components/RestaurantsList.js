import React from 'react'
import ReactPaginate from 'react-paginate'
import { useState } from 'react'
import Restaurant from './Restaurant'
import {BsFillCaretRightFill} from 'react-icons/bs'

const RestaurantsList = ({ restaurants, resPerPage }) => {
  const [pageNum, setPageNum] = useState(0);

  const restaurantsPerPage = resPerPage;
  const restaurantsDisplayed = restaurantsPerPage * pageNum;

  const displayRest = restaurants
  .slice(restaurantsDisplayed, restaurantsDisplayed + restaurantsPerPage)
  .map((restaurant) => {
    return (
      <div key={restaurant._id}  onClick={() => {window.location.href="/restaurant/"+restaurant._id}}><Restaurant restaurant={restaurant}/></div>
      )
  })

  const pageCount = Math.ceil(restaurants.length / restaurantsPerPage)

  const changePage = ({selected}) => {
    setPageNum(selected)
  };

  return (
    <div>
      <div>
        {displayRest}
      </div>
      {(restaurants.length > restaurantsPerPage && 
       <ReactPaginate 
       previousLabel={'Nazaj'}
       nextLabel={'Naprej'}
       pageCount={pageCount}
       onPageChange={changePage}
       containerClassName={'paginationBtns flex mt-8 w-1/2 justify-center ml-auto mr-auto'}
       previousLinkClassName={'p-1.5 rounded-l-xl'}
       nextLinkClassName={'p-1.5  rounded-r-xl'}
       disabledClassName={'display-none'}
       activeClassName={'paginationActive'}
       />
      )}
    </div>
  )
}

export default RestaurantsList