import { useState } from "react";

const StarRating = ({submitRating2}) => {
    const [rating, setRating] = useState(0);
    const [hover, setHover] = useState(0);

    function set(index){
        setRating(index)
        submitRating2(index)
    }

  return (
    <div className="star-rating text-3xl ml-4">
      {[...Array(5)].map((star, index) => {
        index += 1;
        return (
          <button
            type="button"
            key={index}
            className={index <= (hover || rating) ? " bg-transparent border-0 outline-none cursor-pointer text-yellow-400" : " bg-transparent border-0 outline-none cursor-pointer text-slate-400"}
            onClick={() =>set(index)}
            onMouseEnter={() => setHover(index)}
            onMouseLeave={() => setHover(rating)}
          >
            <span className="star">&#9733;</span>
          </button>
        );
      })}
    </div>
  )
}

export default StarRating