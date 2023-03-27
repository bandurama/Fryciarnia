import {useEffect} from "react";
import "../styles/Main.css";

export default function Main() {
	useEffect(() => {
		document.title = 'Strona Główna - Fryciarnia'
	}, []);

	return (
		<>
			<div className="top-nav">
				<div className="soc">
					<a href="#"><img src="./icons/fb-icon.png"/></a>
					<a href="#"><img src="./icons/in-icon.png"/></a>
					<a href="#"><img src="./icons/yt-icon.png"/></a>
					<a href="#"><img src="./icons/tok-icon.png"/></a>
				</div>
				<div className="logo">
					<img src="./logo.png" />
				</div>
				<div className="acc">
					<button>DOŁĄCZ DO NAS</button>
				</div>
			</div>
		</>
	)
}