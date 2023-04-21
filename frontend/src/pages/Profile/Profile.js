import TopNav from "../../components/TopNav";
import '../../styles/Forms.css'
import {useEffect, useState} from "react";

export default function Profile ()
{

	useEffect(() => {
		document.title = 'Mój Profil - Fryciarnia'


		fetch('http://bandurama.ddns.net:2023/api/user/info', {
			method: 'POST',
			body: JSON.stringify({}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp => {
				console.log(resp);
				if (!resp.ok)
					return;
				setTbxMail(resp.data.mail ?? "Impossible");
				setTbxName(resp.data.name ?? "");
				setTbxSurname(resp.data.surname ?? "");
			})

	}, []);

	const [tbxMail, setTbxMail] = useState('');
	const [tbxName, setTbxName] = useState('');
	const [tbxSurname, setTbxSurname] = useState('');

	const [tbxOldPassword, setTbxOldPassword] = useState('');
	const [tbxOldPassword2, setTbxOldPassword2] = useState('');
	const [tbxNewPassword, setTbxNewPassword] = useState('');

	const eventUpdateUserInfo = function (e)
	{
		const _datagram =
		{
			uuid: '',
			isGoogleAccount: false,
			mail: '',
			name: tbxName,
			surname: tbxSurname,
			password: '',
			type: 'Web'
		}

		fetch('http://bandurama.ddns.net:2023/api/user/edit', {
			method: 'POST',
			body: JSON.stringify(_datagram),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp => {
				if (resp.ok)
				{ /* udało się utworzyć użytkownika */
						console.log('SUCCESS');
				}
			})
	}


	const eventUpdateUserPassword = function (e)
	{
		if (tbxOldPassword != tbxOldPassword2)
		{
			alert("Passwordy sie nie pokraywaja");
			return;
		}

		const _datagram =
		{
			uuid: '',
			isGoogleAccount: false,
			mail: tbxOldPassword,
			name: '',
			surname: '',
			password: tbxNewPassword,
			type: 'Web'
		}

		fetch('http://bandurama.ddns.net:2023/api/user/password/set', {
			method: 'POST',
			body: JSON.stringify(_datagram),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp => {
				console.log(resp);
				if (resp.ok)
				{ /* udało się utworzyć użytkownika */
					window.location.href = '/';
				}
			})
	}
	const dispMyProfile = () => (
		<>
			<div className="formtitle">
				Mój Profil
			</div>
			<div className="formrow">
				<input type="text" disabled placeholder="E-Mail" value={tbxMail}/>
			</div>
			<div className="formrow">
				<input type="text" placeholder="Imię" onChange={(e) => setTbxName(e.target.value)}  value={tbxName}/>
			</div>
			<div className="formrow">
				<input type="text" placeholder="Nazwisko"  onChange={(e) => setTbxSurname(e.target.value)} value={tbxSurname}/>
			</div>
			<div className="formrow">
				<button onClick={eventUpdateUserInfo}>ZAPISZ</button>
			</div>
		</>
	)

	const dispSettings = () => (
		<>
			<div className="formtitle">
				Ustawienia
			</div>
			<div className="formrow">
				<input type="password" placeholder="Stare hasło" value={tbxOldPassword} onChange={(e) => setTbxOldPassword(e.target.value)} />
			</div>
			<div className="formrow">
				<input type="password" placeholder="Nowe hasło" value={tbxOldPassword2} onChange={(e) => setTbxOldPassword2(e.target.value)} />
			</div>
			<div className="formrow">
				<input type="password" placeholder="Powtórz nowe hasło" value={tbxNewPassword} onChange={(e) => setTbxNewPassword(e.target.value)} />
			</div>
			<div className="formrow">
				<button onClick={eventUpdateUserPassword}>ZMIEŃ HASŁO</button>
			</div>
			<div className="formrow">
				Możesz też
				<a href="../Register">
					usunąć
				</a>
				&nbsp;swoje konto
			</div>
		</>
	)
	const [mainDisplay, setMainDisplay] = useState('profile');
	const displayOptions =
	{
		profile: dispMyProfile,
		settings: dispSettings
	}

	return (
		<>
			<TopNav />
			<div className="formwrapper">
				<div className="formbox">
					<div className="test">
						<div className="button" style={{backgroundColor: mainDisplay == 'profile' ? '#ffbf53' : "inherit", borderStartStartRadius: 25}} onClick={(e) => setMainDisplay('profile')}>
							Mój Profil
						</div>
						<div className="button" onClick={(e) => window.location.href = './profile/history'}>
							Historia
						</div>
						<div className="button" style={{backgroundColor: mainDisplay == 'settings' ? '#ffbf53' : "inherit", borderTopRightRadius: 25}} onClick={(e) => setMainDisplay('settings')}>
							Ustawienia
						</div>
					</div>
					{displayOptions[mainDisplay]()}
				</div>
			</div>
		</>
	)
}