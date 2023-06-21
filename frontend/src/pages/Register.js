import {useEffect} from "react";
import TopNav from "../components/TopNav";
import {acquireGetParams} from "../utils/Gets";

export default function Register() {
	useEffect(() => {
		document.title = 'Zarejestruj się - Fryciarnia'
	}, []);

	/**
	 * Event przycisku rejestruj
	 * @param e
	 */
	const register = function (e)
	{
		const p1 = document.querySelector('#tbx-passwd1').value;
		const p2 = document.querySelector('#tbx-passwd2').value;

		if (p1 != p2)
		{
			alert("Hasla musza byc takie same!");
			return;
		}

		const _datagram =
		{
			uuid: '',
			isGoogleAccount: false,
			mail: document.querySelector('#tbx-mail').value,
			name: '',
			surname: '',
			password: p1,
			type: 'Web'
		}

		/**
		 * HACK: If registering as cook in holding
		 *       alter datagram in such way
		 *       that NAME is SET to holdingUUID
		 *       also, change type to Kitchen
		 */

		const _get = acquireGetParams();

		if (_get != null && Object.keys(_get).includes("holding"))
		{ /* Registering new cook */
			_datagram.type = "Kitchen";
			_datagram.name = _get.holding;
		}

		fetch('http://bandurama.ddns.net:2023/api/user/register', {
			method: 'POST',
			body: JSON.stringify(_datagram),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp => {
				if (resp.ok)
				{ /* udało się utworzyć użytkownika */
					window.location.href = _datagram.type === "Web"
						? '/profile'
						: '/kitchen'
				}
				else
				{
					alert(resp.msg)
				}
		})
	}

	return (
		<>
			<TopNav />
			<div className="formwrapper">
				<div className="formbox">
					<div className="formtitle">
						Zarejestruj się
					</div>
					<div className="formrow">
						<input type="text" placeholder="E-Mail" id="tbx-mail"/>
					</div>
					<div className="formrow">
						<input type="password" placeholder="Hasło" id="tbx-passwd1"/>
					</div>
					<div className="formrow">
						<input type="password" placeholder="Powtórz Hasło" id="tbx-passwd2"/>
					</div>
					<div className="formrow">
						<button onClick={register}>ZAREJESTRUJ</button>
					</div>
					<div className="formrow">
						Lub <a href="./login">zaloguj się</a>
					</div>
				</div>
			</div>
		</>
	)
}