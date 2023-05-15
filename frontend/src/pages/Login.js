import {useEffect, useState} from "react";
import TopNav from "../components/TopNav";

import '../styles/Forms.css'

export default function Login()
{

	useEffect(() =>
	{
		document.title = 'Zaloguj się - Fryciarnia'
	}, []);

	const googleLoginTrigger = function (e)
	{
		const __google_api_endpoint = "https://accounts.google.com/o/oauth2/v2/auth?client_id=268913103926-nvg6lersjlvopnouhesjcvts6ttrj14v.apps.googleusercontent.com&redirect_uri=http://bandurama.ddns.net/gauth&response_type=token&scope=https://www.googleapis.com/auth/userinfo.profile&include_granted_scopes=true&state=pass-through%20value";
		window.location.href = __google_api_endpoint;
	}

	const eventLoginUser = function (e)
	{
			const _router =
			{
				Admin: '/admin/holding',
				Web: '/',
				Manager: '/manager/stock',
				Display: '/tickets',
				Terminal: '/order/0',
				Kitchen: '/kitchen'
			};


			const _datagram =
			{
				uuid: '',
				isGoogleAccount: false,
				mail: document.querySelector('#tbx-mail').value,
				name: '',
				surname: '',
				password: document.querySelector('#tbx-passwd').value,
				type: 'Web'
			}

		fetch('http://bandurama.ddns.net:2023/api/user/login', {
			method: 'POST',
			body: JSON.stringify(_datagram),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp => {
				console.log(resp);
				if (resp.ok)
				{ /* udało się zqalogować użytkownika */
					if (Object.keys(_router).includes(resp.data.type))
						window.location.href = _router[resp.data.type];
					else
						throw new Error("Login router not implemented for user type: " + resp.data.type);
				}
				else
				{
					alert(resp.msg);
				}
			})
	}

	return (
		<>
			<TopNav />
			<div className="formwrapper">
				<div className="formbox">
					<div className="formtitle">
						Zaloguj się
					</div>
					<div className="formrow">
						<input type="text" placeholder="E-Mail" id="tbx-mail" onKeyDown={(e) => {if (e.key == 'Enter') { eventLoginUser(e) } }}/>
					</div>
					<div className="formrow">
						<input type="password" placeholder="Hasło" id="tbx-passwd" onKeyDown={(e) => {if (e.key == 'Enter') { eventLoginUser(e) } }}/>
					</div>
					<div className="formrow">
						<button onClick={eventLoginUser}>ZALOGUJ</button>
					</div>
					<div className="formrow" style={{marginTop: '0px'}}>
						<img src="./icons/google.png" onClick={googleLoginTrigger}/>
					</div>
					<div className="formrow">
						Lub <a href="./register">zarejestruj się</a>
					</div>
				</div>
			</div>
		</>
	)
}