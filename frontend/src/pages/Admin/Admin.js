import {useParams} from "react-router-dom";
import {useEffect} from "react";
import {usingBootstrap} from "../../utils/Bootstrap";
import Holding from "./Holdings/Holdings";
import HoldingMgmt from "./Holdings/HoldingMgmt";
import Ingridients from "./Ingridients/Ingridients";
import IngridientMgmt from "./Ingridients/IngridientMgmt";
import Users from "./Users/Users";
import UserMgmt from "./Users/UserMgmt";
import Meal from "./Meal/Meal";
import MealMgmt from "./Meal/MealMgmt";
import Manager from "../Manager/Manager";
import Management from "../../utils/Management";
import '../../styles/Tables.css'
import Orders from "../AdminManager/Orders";



export default function ()
{
	const views = {};

	views['holding'] = (<Holding/>);
	views['holdingmgmt'] = (<HoldingMgmt/>);

	views['ingridients'] = (<Ingridients/>);
	views['ingridientsmgmt'] = (<IngridientMgmt/>);

	views['orders'] = (<Orders/>);

	views['users'] = (<Users/>);
	views['usersmgmt'] = (<UserMgmt/>);

	views['meals'] = (<Meal/>);
	views['mealsmgmt'] = (<MealMgmt/>);

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

	const navTree =
	[
		{
			title: "Franczyzy",
			routes:
			[
				{ name: "Wyświetl franczyzy", href: "/admin/holding" },
				{ name: "Zarządzanie", href: "/admin/holdingmgmt" }
			]
		},
		{
			title: "Posiłki",
			routes:
			[
				{ name: "Wyświetl posiłki", href: "/admin/meals" },
				{ name: "Zarządzanie", href: "/admin/mealsmgmt" }
			]
		},
		{
			title: "Składniki",
			routes:
			[
				{ name: "Wyświetl składniki", href: "/admin/ingridients" },
				{ name: "Zarządzanie", href: "/admin/ingridientsmgmt" }
			]
		},
		{
			title: "Zamówienia",
			routes:
			[
				{ name: "Wyświetl zamówienia", href: "/admin/orders" }
			]
		},
		{
			title: "Użytkownicy",
			routes:
				[
					{ name: "Wyświetl użytkowników", href: "/admin/users" },
					{ name: "Zarządzanie", href: "/admin/usersmgmt" }
				]
		}
	];

	useEffect(() => {
		window.document.title = "Panel Administratora - Fryciarnia";
	}, []);

	return (
		<Management title="PANEL ADMINISTRACYJNY" onLogout={logMeOut} views={views} nav={navTree}/>
	)
}