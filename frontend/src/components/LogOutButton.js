
export default function LogOutButton ()
{
	const logMeOut = function (e)
	{
		fetch('http://bandurama.ddns.net:2023/api/user/logout', {
			method: 'POST',
			body: JSON.stringify({}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp => {
					console.log(resp);
					window.location.href = '/';
				}
			)
	}

	return <button onClick={logMeOut}> WYLOGUJ SIĘ</button>
}