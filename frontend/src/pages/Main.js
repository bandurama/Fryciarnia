import {useEffect, useState} from "react";
import "../styles/Main.css";
import TopNav from "../components/TopNav";

export default function Main() {
	useEffect(() => {
		document.title = 'Strona Główna - Fryciarnia'
	}, []);

	const [activeSlide, setActiveSlide] = useState(0);
	const __slide_count = 5;

	const slideBack = function (e)
	{
		setActiveSlide(activeSlide == 0
			? __slide_count
			: activeSlide - 1);
	}

	const slideForward = function (e)
	{
		setActiveSlide((activeSlide + 1) % (__slide_count + 1));
		console.log('forw', e);
	}

	return (
		<>
			<TopNav useAccountButton={true} />
			<div className="slides">
				<img src={`./slides/slide${activeSlide}.png`}/>
				<button>POKAŻ WIĘCEJ</button>
			</div>
			<div className="slides-controller">
				<a href="#"><img src="./icons/back.png" onClick={slideBack}/></a>
				<a href="#"><img src="./icons/forward.png" onClick={slideForward}/></a>
			</div>
			<div className="wrapper">
				<div className="card" id="card-menu">
					<div className="title">Miłość w cenie</div>
					<div className="sub-title">niewiarygodne smaki</div>
					<button>ZAPOZNAJ SIĘ Z NASZĄ OFERTĄ</button>
				</div>
				<div className="card" id="card-apps">
					<div className="title">
						Pobierz naszą apkę na telefon
					</div>
					<div className="sub-title">
						Miej Fryciarnie zawszę pod ręką
					</div>
					<div className="downloads">
						<a href="https://play.google.com/store/search?q=fryciarnia&c=apps&hl=pl&gl=US"><img src="./icons/android.png"/></a>
						<a href="#"><img src="./icons/ios.png"/></a>
					</div>
				</div>
				<div className="card" id="card-search">
					<div className="alt-title">
						Znajdź miejscówkę
					</div>
					<div className="search-box">
						<div className="search-bar">
							<div className="pict">
								<img src="./icons/gps.png"/>
							</div>
							<select>
								<option value="">Kielce, Aleja 100-lecia</option>
							</select>
						</div>
						<button>IDŹ</button>
					</div>
				</div>
				<div className="card" id="card-team">
					<div className="title">Dołącz do najlepszego teamu</div>
					<button>ROBOTA WE FRYCIARNI</button>
				</div>
			</div>
			<Footer/>
		</>
	)
}