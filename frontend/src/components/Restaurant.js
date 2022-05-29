
const Restaurant = ({ restaurant }) => {
    const location = (restaurant.lokacija).substring(0, (restaurant.lokacija).indexOf('('));

  return (
    <div className='bg-white text-slate-800 p-6 mb-6 transition ease-in-out delay-150 hover:-translate-y-1 hover:scale-105 group hover:bg-slate-400 duration-300'>
        <div className='mb-2 font-bold text-lg group-hover:text-white delay-150 duration-300'>
            {restaurant.ime}
        </div>
            <div className="group-hover:text-white delay-150 duration-300">
                <div className="ml-4 flex">
                    <div>
                        <div className="flex">Povprečna ocena: <p className="ml-2">{}</p></div>
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