
import {fillInForm, serializeForm} from "../../../utils/Forms";
import {useEffect, useState} from "react";
import {acquireGetParams} from "../../../utils/Gets";

export default function UserMgmt ()
{
	const [editting, setEditting] = useState(null);
	const [userType, setUserType] = useState(null);

	const [listOfHoldings, setListOfHoldings] = useState([]);


	const __symlinked_user_types = ["Kitchen", "Display", "Terminal"];

	const reloadHoldingsList = function (callback)
	{

		fetch('http://bandurama.ddns.net:2023/api/holding/list', {
			method: 'POST',
			body: JSON.stringify({}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					console.log(resp.data);
					setListOfHoldings(resp.data);
					callback();
				}
				else
				{
					throw new Error(resp.msg);
				}
			});
	}

	useEffect(() => {
		const _get = acquireGetParams();

		if (_get != null)
			if (Object.keys(_get).includes('uuid'))
			{ /* also fetch */
				setEditting(_get.uuid);
				// fillInUserInfo(_get.uuid);
				reloadHoldingsList(() => fillInUserInfo(_get.uuid))
			}

		if (_get == null)
			reloadHoldingsList(() => {});


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
				<div className="row g-4">
					<div className="col-md-7 col-lg-8">
						<h4 className="mb-3">Dodaj do listy użytkowników</h4>
						<div className="row g-3">
							<form id="user">
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
									<select className="form-select" name="type" required value={userType} onChange={(e) => setUserType(e.target.value)}>
										<option value="" disabled selected>Wybierz</option>
										<option value="Manager">Menedżer</option>
										<option value="Kitchen">Kucharz</option>
										<option value="Display">Wyświetlacz w restauracji</option>
										<option value="Terminal">Terminal w restauracji</option>
										<option value="Web">Zwykły użytkownik</option>
									</select>
								</div>

								<div className="col-sm-6">
									<label htmlFor="holding" className="form-label">Powiązana Franczyza</label>
									<select className="form-select" name="holding" required disabled={!__symlinked_user_types.includes(userType)}>
										<option value="" disabled selected>Wybierz</option>
										{
											listOfHoldings.map((holding) => (
												<option key={holding.uuid} value={holding.uuid}>{holding.localization}</option>
											))
										}
									</select>
								</div>

								<div className="form-check">
									<input type="checkbox" className="form-check-input" name="isGoogleAccount"/>
										<label className="form-check-label" htmlFor="isGoogleAccount">Autoryzacja google</label>
								</div>

							</form>
								<hr className="mt-5" />

								<div className="row mt-5">
									<div className="col text-center">
										{
											editting != null
												? <button className="w-20 btn btn-warning btn-lg mr-3" onClick={eventEdit}>Edycja</button>
												: <button className="w-20 btn btn-success btn-lg mr-3" onClick={eventInsert}>Dodaj</button>
										}
										<button className="w-20 btn btn-danger btn-lg">Anuluj</button>
									</div>
								</div>
						</div>
					</div>
				</div>
			</div>
		</>
	)
}
