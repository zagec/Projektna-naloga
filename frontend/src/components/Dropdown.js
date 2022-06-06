import * as React from 'react';

const Dropdown = ({onChange, values, setResPerPage}) => {
    const [value, setValue] = React.useState(values[0]);

    const handleChange = (event) => {
        setValue(event.target.value);
        onChange(event.target.value)
    };

    const show = values.map((value) => {
      return (
        <option value={value}>{value}</option>
      )
    })

  return (
    <div>
      <select className='ml-3 block px-1 py-1.5 text-base text-slate-700 bg-white border border-solid border-gray-300 rounded transition ease-in-out' 
      value={value} onChange={handleChange}>
        {show}
      </select>
    </div>
  )
}

export default Dropdown