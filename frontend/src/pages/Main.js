import {useEffect, useRef, useState} from "react";
import "../styles/Main.css";
import TopNav from "../components/TopNav";
import Footer from "../components/Footer";

export default function Main() {
	useEffect(() => {
		document.title = 'Strona Główna - Fryciarnia';
		fetchHoldings();
		setInterval(() => {
			// setActiveSlide((activeSlide) => (activeSlide + 1) % __slide_count);
			slideForward(null);
		}, 5000);
	}, []);

	const [activeSlide, setActiveSlide] = useState(0);
	const [holdingList, setHoldingList] = useState([]);
	const holdingSelectBox = useRef();
	const __slide_count = 7;

	const slideBack = function (e)
	{
		setActiveSlide(activeSlide <= 0
			? __slide_count - 1
			: activeSlide - 1 );
	}

	const slideForward = function (e)
	{
		const nextSlide = (activeSlide + 1) % __slide_count;
		setActiveSlide(nextSlide);
	}

	const fetchHoldings = function ()
	{
		fetch('http://bandurama.ddns.net:2023/api/holding/list', {
			method: 'POST',
			body: JSON.stringify({}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp => {
					if (resp.ok)
						setHoldingList(resp.data);
				}
			)
	}

	const evtIdz = function (e)
	{
		const selectedHoldingUUID = holdingSelectBox.current.value;
		window.location.href = `http://bandurama.ddns.net/menu/${selectedHoldingUUID}`;
	}

	return (
		<>
			<TopNav useAccountButton={true} />
			<div className="slides">
				<picture>
					<source srcSet={`./slides/slide${activeSlide}.avif`} type="image/avif"/>
					<img src={`./slides/slide${activeSlide}.webp`} type="image/webp"/>
				</picture>
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
					<button onClick={(e) => window.location.href = `/menu/all` }>ZAPOZNAJ SIĘ Z NASZĄ OFERTĄ</button>
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
							<select ref={holdingSelectBox}>
								{holdingList.map((holding) => <option value={holding.uuid} key={holding.uuid}>{holding.localization}</option>)}
							</select>
						</div>
						<button onClick={evtIdz}>IDŹ</button>
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