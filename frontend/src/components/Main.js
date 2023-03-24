import {useEffect} from "react";
import "../styles/Main.css";

export default function Main() {
	useEffect(() => {
		document.title = 'Strona Główna - Fryciarnia'
	}, []);

	return (
		<>
			<div className="top-nav">
				<div className="logo">
					<div className="plain"></div>
				</div>
				<div className="nav">
					<a href="#">MENU</a>
					<a href="#">MIEJSCÓWKI</a>
					<a href="#"></a>
					<a href="#"></a>
				</div>
				<div className="auth">
					Auth
				</div>
			</div>
		</>
	)
}