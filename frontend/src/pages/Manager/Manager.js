import Management from "../../utils/Management";
import Stock from "./Stock/Stock";
import Workers from "./Workers/Workers";

export default function Manager ()
{

	const views =
	{
		stock: <Stock/>,
		workers: <Workers/>
	}

	const navTree =
	[
		{
			title: "Statystyka",
			routes:
			[
				{ name: "Stany magazynowe", href: "/manager/stock" },
				{ name: "Sprzedaż", href: "/manager/stats" },
			]
		},
		{
			title: "Zamówienia",
			routes:
				[
					{ name: "Wyświetl zamówienia", href: "/manager/orders" }
				]
		},
		{
			title: "Obsługa",
			routes:
				[
					{ name: "Wyświetl obsługę", href: "/manager/workers" }
				]
		}
	]

	const logout = function (e)
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

	return (
		<Management title="FRYCIARNIA - PANEL MENADŻERA" onLogout={logout} views={views} nav={navTree}/>
	)
}