
import {fillInForm, serializeForm} from "../../../utils/Forms";
import {useEffect, useState} from "react";
import {acquireGetParams} from "../../../utils/Gets";

export default function UserMgmt ()
{
	const [editting, setEditting] = useState(null);
	const [userType, setUserType] = useState(null);

	useEffect(() => {
		const _get = acquireGetParams();

		if (_get != null)
			if (Object.keys(_get).includes('uuid'))
			{ /* also fetch */
				setEditting(_get.uuid);
				// fillInUserInfo(_get.uuid);
				fillInUserInfo(_get.uuid);
			}
	}, []);


	const fillInUserInfo = function (uuid)
	{
		fetch('http://bandurama.ddns.net:2023/api/user/info', {
			method: 'POST',
			body: JSON.stringify({uuid: uuid}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					console.log(resp.data);
					fillInForm('user', resp.data);
					setUserType(resp.data.type);
				}
				else
				{
					throw new Error(resp.msg);
				}
			});

	}


	const eventEdit = function (e)
	{
		const datagram = serializeForm("user");
		datagram['uuid'] = editting;
		datagram['password'] = datagram['name'] + "1234";
		console.log(datagram);

		fetch('http://bandurama.ddns.net:2023/api/user/edit', {
			method: 'POST',
			body: JSON.stringify(datagram),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					console.log(resp.data);
					window.location.reload();
				}
				else
				{
					alert("Nie udało się edytować użytkownika: " + resp.msg);
					throw new Error(resp.msg);
				}
			});

	}


	const eventInsert = function (e)
	{
		const datagram = serializeForm("user");
		datagram['uuid'] = "";
		datagram['password'] = datagram['name'] + "1234";

		fetch('http://bandurama.ddns.net:2023/api/user/register', {
			method: 'POST',
			body: JSON.stringify(datagram),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					console.log(resp.data);
					window.location.href = `/admin/usersmgmt?uuid=${resp.data.uuid}`;
				}
				else
				{
					alert("Nie udało się dodać użytkownika: " + resp.msg);
					throw new Error(resp.msg);
				}
			});

	}

	return (
		<>
			<div className="container">
				<div className="py-4 text-center">
					<h2>Zarządzanie listą użytkowników</h2>
				</div>
				<form id="user">
					<div className="row g-3">
						<div className="col-sm-8">
							<h4 className="mb-3">Dodaj do listy użytkowników</h4>
							<div className="row g-4">
								<div className="col-sm-6">
									<label htmlFor="firstName" className="form-label">Imię</label>
									<input type="text" className="form-control" name="name" placeholder="" required/>
								</div>

								<div className="col-sm-6">
									<label htmlFor="lastName" className="form-label">Nazwisko</label>
									<input type="text" className="form-control" name="surname" placeholder="" required/>
								</div>


								<div className="col-sm-6">
									<label htmlFor="firstName" className="form-label">Mail</label>
									<input type="text" className="form-control" name="mail" placeholder="" required/>
								</div>

								<div className="col-sm-6">
									<label htmlFor="lastName" className="form-label">Typ konta</label>
									<select className="form-select" name="type" required value={userType} onChange={(e) => setUserType(e.target.value)} disabled={editting != null}>
										<option value="" disabled selected>Wybierz</option>
										<option value="Manager">Menedżer</option>
										<option value="Kitchen">Kucharz</option>
										<option value="Display">Wyświetlacz w restauracji</option>
										<option value="Terminal">Terminal w restauracji</option>
										<option value="Web">Zwykły użytkownik</option>
									</select>
								</div>
								<div className="form-check">
									<input type="checkbox" className="form-check-input" name="isGoogleAccount"/>
										<label className="form-check-label" htmlFor="isGoogleAccount">Autoryzacja google</label>
								</div>
								<hr className="mt-5" />

								<div className="row mt-3">
									<div className="col text-center">
										{
											editting != null
												? <button className="w-20 btn btn-warning btn-lg mr-3" onClick={eventEdit}>Edycja</button>
												: <button className="w-20 btn btn-success btn-lg mr-3" onClick={eventInsert}>Dodaj</button>
										}
										<button className="w-20 btn btn-danger btn-lg" style={{marginLeft: 15}}>Anuluj</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</>
	)
}
