import { useState, useEffect } from "react";

const Boop = ({ rotation = 20, timing = 200, children }) => {
    const [isBooped, setIsBooped] = useState(false);


    const style = {
      display: 'inline-block',
      backfaceVisibility: 'hidden',
      transform: isBooped
        ? `rotate(${rotation}deg)`
        : `rotate(0deg)`,
      transition: `transform ${timing}ms`,
    };
    useEffect(() => {
        console.log(isBooped)
      if (!isBooped) {
        return;
      }
      const timeoutId = window.setTimeout(() => {
        setIsBooped(false);
      }, timing);
      return () => {
        window.clearTimeout(timeoutId);
      };
    }, [isBooped, timing]);
    const trigger = () => {
      setIsBooped(true);
    };
    return (
      <span onMouseEnter={trigger} className={"inline-block "} style={style}>
        {children}
      </span>
    );
  };

  export default Boop