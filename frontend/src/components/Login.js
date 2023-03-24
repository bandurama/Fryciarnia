import {useEffect} from "react";

export default function Login() {
	useEffect(() => {
		document.title = 'Zaloguj się - Fryciarnia'
	}, []);
	return (
		<>
			<p>Login</p>
			<p>Loginining</p>
		</>
	)
}