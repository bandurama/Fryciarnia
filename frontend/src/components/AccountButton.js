import {useEffect, useState} from "react";


export default function AccountButton ()
{
	const [isLoggedIn, setIsLoggedIn] = useState(false);

	useEffect(() => {
		fetch('http://bandurama.ddns.net:2023/api/user/ping', {
			method: 'POST',
			body: JSON.stringify({}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp => {
				console.log('ping', resp);
				setIsLoggedIn(resp.ok);
				if (resp.ok && resp.data.type === 'Terminal' && window.location.pathname === '/')
					window.location.href = '/order/0';
			}
		)
	}, []);

	const btnClick = function ( e )
	{
		console.log(e);
		window.location.href = isLoggedIn
			? '/profile'
			: '/login'
	}

	return <button onClick={btnClick}>{isLoggedIn ? "MÓJ PROFIL" : "DOŁĄCZ DO NAS"}</button>
}