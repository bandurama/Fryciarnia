import {useEffect} from "react";
import TopNav from "../components/TopNav";

export default function Register() {
	useEffect(() => {
		document.title = 'Zarejestruj się - Fryciarnia'
	}, []);
	return (
		<>
			<TopNav />
			<div className="formwrapper">
				<div className="formbox">
					<div className="formtitle">
						Zarejestruj się
					</div>
					<div className="formrow">
						<input type="text" placeholder="E-Mail"/>
					</div>
					<div className="formrow">
						<input type="password" placeholder="Hasło"/>
					</div>
					<div className="formrow">
						<input type="password" placeholder="Powtórz Hasło"/>
					</div>
					<div className="formrow">
						<button>ZAREJESTRUJ</button>
					</div>
					<div className="formrow">
						Lub <a href="./login">zaloguj się</a>
					</div>
				</div>
			</div>
		</>
	)
}