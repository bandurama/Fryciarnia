import TopNav from "../components/TopNav";
import Footer from "../components/Footer";
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import '../styles/Meal.css'

export default function Meal ({ props })
{
	const { holding, meal } = useParams();

	const [myInfo, setMyInfo] = useState(null);

	const fetchMyInfo = function ()
	{
		fetch('http://bandurama.ddns.net:2023/api/menu/meal', {
			method: 'POST',
			body: JSON.stringify({uuid: meal}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					console.log(resp);
					/**
					 * WARN: Remove duplicate ingredients
					 */
					const adpIngs = {};
					for (const [i, e] of resp.data.adpRecipeIngridient.entries())
						if (!Object.keys(adpIngs).includes(e.dbIngridient.uuid))
							adpIngs[e.dbIngridient.uuid] = e;
					resp.data['adpRecipeIngridient'] = Object.entries(adpIngs).map((n) => n.at(1));
					setMyInfo(resp.data);
				}
				else
				{
					throw new Error(resp.msg);
				}
			});
	}

	useEffect(() => {
		fetchMyInfo();
	}, []);


	const Ingridient = function ({ ing })
	{
		return (
			<div className="meal-ingridient">
				<div>
					<div className="icon">
						<img src={ing.dbIngridient.icon} />
					</div>
					<div className="title">
						{ing.dbIngridient.name}
					</div>
				</div>
			</div>
		)
	}

	return (
		<>
			<TopNav useAccountButton={true} />

			<div className="slides">
				{ myInfo && <img src={myInfo.dbMeal.image} style={{width: 800}}/> }
			</div>
			<div className="sub-menu" onClick={(e) => window.location = `/menu/${holding}` }>
				<img src="/icons/back.png"/>
				Wróć do <b>&nbsp;Menu</b>
			</div>
			<div className="wrapper">
				<div className="card">
					<div className="alt-subtitle">
						Zamów online i odbierz w restauracji
					</div>
					{ myInfo && !myInfo.dbMeal.isListed && <button>TOWAR NIEDOSTĘPNY</button> }
					{ myInfo && myInfo.dbMeal.isListed && <button>ZAMÓW ONLINE</button> }
				</div>
				<div className="card">
					<div className="price">
						{myInfo && myInfo.dbMeal.price} zł
					</div>
				</div>
			</div>
			<div className="wrapper">
				<div className="meal-title">
					Składniki
				</div>
			</div>
			<div className="wrapper">
				<div className="meal-title">
				</div>
				{ myInfo && myInfo.adpRecipeIngridient.map((ing) => <Ingridient ing={ing} key={ing.dbRecipe.uuid}/>) }
			</div>
			<Footer/>
		</>
	)
}