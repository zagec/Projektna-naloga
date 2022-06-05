import * as React from 'react';

const Dropdown = ({onChange}) => {
    const [value, setValue] = React.useState('Abeceda');

    const handleChange = (event) => {
        setValue(event.target.value);
        onChange(event.target.value)
    };

  return (
    <div>
      <select className='ml-3 block px-1 py-1.5 text-base text-slate-700 bg-white border border-solid border-gray-300 rounded transition ease-in-out' 
      value={value} onChange={handleChange}>
        <option value="abeceda">Abeceda</option>
        <option value="cena">Cena</option>
      </select>
    </div>
  )
}

export default Dropdown