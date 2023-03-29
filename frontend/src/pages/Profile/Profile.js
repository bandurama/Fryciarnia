import TopNav from "../../components/TopNav";
import '../../styles/Forms.css'
import {useEffect, useState} from "react";

export default function Profile ()
{
	const [tbxMail, setTbxMail] = useState('');
	const [tbxName, setTbxName] = useState('');
	const [tbxSurname, setTbxSurname] = useState('');

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
				<button>ZAPISZ</button>
			</div>
		</>
	)

	const dispSettings = () => (
		<>
			<div className="formtitle">
				Ustawienia
			</div>
			<div className="formrow">
				<input type="password" placeholder="Stare hasło" />
			</div>
			<div className="formrow">
				<input type="password" placeholder="Nowe hasło" />
			</div>
			<div className="formrow">
				<input type="password" placeholder="Powtórz nowe hasło" />
			</div>
			<div className="formrow">
				<button>ZMIEŃ HASŁO</button>
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
	const [mainDisplay, setMainDisplay] = useState(dispMyProfile);

	return (
		<>
			<TopNav />
			<div className="formwrapper">
				<div className="formbox">
					<div className="test">
						<div className="button" onClick={(e) => setMainDisplay(dispMyProfile())}>
							Mój Profil
						</div>
						<div className="button">
							Historia
						</div>
						<div className="button" onClick={(e) => setMainDisplay(dispSettings())}>
							Ustawienia
						</div>
					</div>
					{mainDisplay}
				</div>
			</div>
		</>
	)
}