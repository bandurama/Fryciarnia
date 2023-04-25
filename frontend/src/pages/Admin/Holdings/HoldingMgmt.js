
import {fillInForm, serializeForm} from "../../../utils/Forms";
import {useEffect, useState} from "react";
import {acquireGetParams} from "../../../utils/Gets";

export default function HoldingMgmt ()
{
	const [editting, setEditting] = useState(null);
	const [listOfManagers, setListOfManagers] = useState([]);

	const fetchAllManagers = function ( callback )
	{

		fetch('http://bandurama.ddns.net:2023/api/holding/managers', {
			method: 'POST',
			body: JSON.stringify({}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					setListOfManagers(resp.data);
					callback();
				}
				else
				{
					throw new Error(resp.msg);
				}
			});
	}

	useEffect(() => {

		/* FIRST: fetch all possible managers cause they
							are used in both cases (editing and
							insertion).
		 */
		fetchAllManagers(() => {
			/* THEN: proceed with modes */
			const _get = acquireGetParams();
			if (_get != null)
				if (Object.keys(_get).includes('uuid'))
				{ /* also fetch */
					setEditting(_get.uuid);
					fillInUserInfo(_get.uuid);
				}
		});

	}, []);


	const fillInUserInfo = function (uuid)
	{
		fetch('http://bandurama.ddns.net:2023/api/holding/info', {
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
					fillInForm('holding', resp.data);
				}
				else
				{
					throw new Error(resp.msg);
				}
			});

	}


	const eventEdit = function (e)
	{
		const datagram = serializeForm("holding");
		datagram['uuid'] = editting;
		// datagram['password'] = datagram['name'] + "1234";
		console.log(datagram);

		fetch('http://bandurama.ddns.net:2023/api/holding/edit', {
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
					alert("Nie udało się edytować franczyzy: " + resp.msg);
					throw new Error(resp.msg);
				}
			});

	}


	const eventInsert = function (e)
	{
		const datagram = serializeForm("holding");
		datagram['uuid'] = "";

		fetch('http://bandurama.ddns.net:2023/api/holding/insert', {
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
					window.location.href = `/admin/holdingmgmt?uuid=${resp.data.uuid}`;
				}
				else
				{
					alert("Nie udało się dodać franczyzy: " + resp.msg);
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
							<form id="holding">
								<div className="col-sm-6">
									<label htmlFor="localization" className="form-label">Lokalizacja</label>
									<input type="text" className="form-control" name="localization" placeholder="" required/>
								</div>

								<div className="col-sm-6">
									<label htmlFor="manager" className="form-label">Menedżer</label>
									<select className="form-select" name="manager" required>
										<option value="" disabled selected>Wybierz</option>
										{
											listOfManagers.map((manager) =>
												<option key={manager.uuid} value={manager.uuid}>{manager.name} {manager.surname}</option>)
										}
									</select>
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