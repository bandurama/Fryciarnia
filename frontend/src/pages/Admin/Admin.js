import {useParams} from "react-router-dom";
import {useEffect} from "react";
import {usingBootstrap} from "../../utils/Bootstrap";
import Holding from "./Holdings/Holdings";
import HoldingMgmt from "./Holdings/HoldingMgmt";
import Ingridients from "./Ingridients/Ingridients";
import IngridientMgmt from "./Ingridients/IngridientMgmt";
import Orders from "./Orders/Orders";
import OrderMgmt from "./Orders/OrderMgmt";
import Recipe from "./Recipe/Recipe";
import RecipeMgmt from "./Recipe/RecipeMgmt";
import Users from "./Users/Users";
import UserMgmt from "./Users/UserMgmt";
import Meal from "./Meal/Meal";
import MealMgmt from "./Meal/MealMgmt";



export default function ()
{
	const { option } = useParams();

	const views = {};

	views['holding'] = (<Holding/>);
	views['holdingmgmt'] = (<HoldingMgmt/>);

	views['ingridients'] = (<Ingridients/>);
	views['ingridientsmgmt'] = (<IngridientMgmt/>);

	views['orders'] = (<Orders/>);
	views['ordersmgmt'] = (<OrderMgmt/>)

	views['users'] = (<Users/>);
	views['usersmgmt'] = (<UserMgmt/>);

	views['meals'] = (<Meal/>);
	views['mealsmgmt'] = (<MealMgmt/>);

	const displayView = function ()
	{
		return Object.keys(views).includes(option)
			? views[option]
			: (<>error!</>)
	}

	useEffect(() => {
		console.log(option);
		const x = <Meal style={{width: 100}} field="imie"/>;
		console.log('type', x.type.name, x.props);
		usingBootstrap();
	}, [])

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

	return (
		<>
			<header className="bg-light">
				<div className="container-fluid ">
					<nav className="navbar navbar-light">
						<a className="navbar-brand" href="#">PANEL ADMINISTRACYJNY</a>
						<div className="navbar-nav">
							<button className="btn btn-info btn-sm mx-4" id="wyloginator" onClick={logMeOut}>Wyloguj</button>
						</div>
					</nav>
				</div>
			</header>
			<div className="container-fluid">
				<div className="row">
					<nav className="col bg-light">
						<ul className="nav flex-column">


							<h3>Franczyzy</h3>
							<li className="nav-item px-1">
								<a className="nav-link" href="/admin/holding">Wyświetl franczyzy</a>
							</li>
							<li className="nav-item px-1">
								<a className="nav-link" href="/admin/holdingmgmt">Zarządzanie</a>
							</li>

							<h3>Posiłki</h3>
							<li className="nav-item px-1">
								<a className="nav-link" href="/admin/meals">Wyświetl posiłki</a>
							</li>
							<li className="nav-item px-1">
								<a className="nav-link" href="/admin/mealsmgmt">Zarządzanie</a>
							</li>

							<h3>Przepisy</h3>
							<li className="nav-item px-1">
								<a className="nav-link" href="/admin/recipe">Wyświetl przepisy</a>
							</li>
							<li className="nav-item px-1">
								<a className="nav-link" href="/admin/recipemgmt">Zarządzanie</a>
							</li>


							<h3>Składniki</h3>
							<li className="nav-item px-1">
								<a className="nav-link" href="/admin/ingridients">Wyświetl składniki</a>
							</li>
							<li className="nav-item px-1">
								<a className="nav-link" href="/admin/ingridientsmgmt">Zarządzanie</a>
							</li>


							<h3>Zamówienia</h3>
							<li className="nav-item px-1">
								<a className="nav-link" href="/admin/orders">Wyświetl zamówienia</a>
							</li>
							<li className="nav-item px-1">
								<a className="nav-link" href="/admin/ordersmgmt">Zarządzanie</a>
							</li>


							<h3>Użytkownicy</h3>
							<li className="nav-item px-1">
								<a className="nav-link" href="/admin/users">Wyświetl użytkowników</a>
							</li>
							<li className="nav-item px-1">
								<a className="nav-link" href="/admin/usersmgmt">Zarządzanie</a>
							</li>

						</ul>
					</nav>
					<main className="col-10 h-100 px-1 py-5 mb-5">
						{displayView()}
					</main>
				</div>
			</div>
			<footer>
			</footer>
		</>
	)
}