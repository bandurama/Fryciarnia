import TopNav from "../components/TopNav";
import Footer from "../components/Footer";
import {useEffect} from "react";
import {useParams} from "react-router-dom";
import '../styles/Meal.css'

export default function Meal ({ props })
{
	const { meal } = useParams();

	useEffect(() => {
		console.log('myparams', meal);
	}, []);
	return (
		<>
			<TopNav useAccountButton={true} />

			<div className="slides">
				<img src="/slides/slide1.png"/>
			</div>
			<div className="sub-menu" onClick={(e) => window.location = "/menu" }>
				<img src="/icons/back.png"/>
				Wróć do <b>&nbsp;Menu</b>
			</div>
			<div className="wrapper">
				<div className="card">
					<div className="alt-subtitle">
						Zamów online i odbierz w restauracji
					</div>
					<button>ZAMÓW ONLINE</button>
				</div>
				<div className="card">
					<div className="alt-subtitle">
						Skorzystaj z dostawy do domu
					</div>
					<button>ZAMÓW PRZEZ GLOVO</button>
				</div>
			</div>
			<div className="wrapper">
				<div className="meal-title">
					Składniki
				</div>
				<div className="meal-ingridients">


					<div className="meal-ingridient">
						<div>
							<div className="icon">
								<img src="https://cdn.mcdonalds.pl/uploads/20191002151712/opt/mcd-skladniki-pomidor.png" />
							</div>
							<div className="title">
								Pomidor
							</div>
						</div>
					</div>


					<div className="meal-ingridient">
						<div>
							<div className="icon">
								<img src="https://cdn.mcdonalds.pl/uploads/20191002151528/opt/mcd-skladniki-keczup.png" />
							</div>
							<div className="title">
								Kepucz
							</div>
						</div>
					</div>

				</div>


			</div>
			<Footer/>
		</>
	)
}