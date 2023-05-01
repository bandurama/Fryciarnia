import TopNav from "../components/TopNav";
import Footer from "../components/Footer";

import '../styles/Menu.css';
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";

export default function Menu ()
{
	const { holding } = useParams();
	const [menuList, setMenuList] = useState([]);

	const fetchMenu = function ()
	{
		fetch('http://bandurama.ddns.net:2023/api/menu', {
			method: 'POST',
			body: JSON.stringify(holding == 'all' ? {} : { uuid: holding }),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					console.log(resp.data);
					setMenuList(resp.data);
				}
				else
				{
					throw new Error(resp.msg);
				}
			});
	}

	useEffect(() => {
		fetchMenu();
	}, []);

	const MealItem = function ({ meal })
	{
		const evtClick = function (e)
		{
			window.location.href = `http://bandurama.ddns.net/meal/${holding}/${meal.uuid}`;
		}

		return (
			<div className="menu-item" onClick={evtClick}>
				<div className="menu-item-image" style={{backgroundImage: `url("${meal.icon}")`}}></div>
				<div className="menu-item-title">
					{meal.name}
				</div>
			</div>
		)
	}

	return (
		<>
			<TopNav useAccountButton={true}/>
			<div className="slides">
				<img src="/menu/strip.jpg"/>
			</div>
			<div className="sub-menu" onClick={(e) => window.location = "/" }>
				<img src="/icons/back.png"/>
				Wróć do <b>&nbsp;Strony Głównej</b>
			</div>
			<div className="wrapper">
				{menuList.map((meal) => <MealItem meal={meal} key={meal.uuid}/>)}
			</div>
			<Footer/>
		</>
	)
}