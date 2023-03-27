import {useEffect} from "react";

export default function Login() {
	useEffect(() => {
		document.title = 'Zaloguj się - Fryciarnia'
	}, []);
	return (
		<>
			<p>Login</p>
			<p>Loginining</p>
			<a href="https://accounts.google.com/o/oauth2/v2/auth?client_id=268913103926-nvg6lersjlvopnouhesjcvts6ttrj14v.apps.googleusercontent.com&redirect_uri=http://bandurama.ddns.net/gauth&response_type=token&scope=https://www.googleapis.com/auth/userinfo.profile&include_granted_scopes=true&state=pass-through%20value">Goówno OaAuth 2.0</a>
		</>
	)
}