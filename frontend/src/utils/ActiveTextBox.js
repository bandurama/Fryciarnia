import {useState} from "react";

export default function ActiveTextBox ({ type, value, endpoint, propName, defaultProps, style })
{

    const [selfState, setSelfState] = useState(value);

    const activeChangeListener = function (e)
    {
        const value = e.target.value;
        setSelfState(value);
        /* make call to API endpoint */
        const _datagram = defaultProps;
        _datagram[propName] = value;

        console.log()
        if (type === "number" && (value == "" || value == null))
        {
            _datagram[propName] = "0f";
        }

        fetch(endpoint, {
            method: 'POST',
            body: JSON.stringify(_datagram),
            credentials: 'include'
        })
          .then((response) => response.json())
          .then(resp =>
          {
              console.log("Active TextBox resp:", resp);
          });
    }

    return (
      <input className="form-control" type={type} value={selfState} onChange={activeChangeListener} style={style}/>
    )
}