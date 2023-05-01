import {useEffect, useState} from "react";
import {acquireGetParams} from "../../../utils/Gets";
import {fillInForm, serializeForm} from "../../../utils/Forms";

export default function IngridientMgmt ()
{
	const [editting, setEditting] = useState(null);

	useEffect(() => {
		const _get = acquireGetParams();
		if (_get != null)
			if (Object.keys(_get).includes('uuid'))
			{ /* also fetch */
				setEditting(_get.uuid);
				fillInUserInfo(_get.uuid);
			}
	}, []);

	const fillInUserInfo = function (uuid)
	{
		fetch('http://bandurama.ddns.net:2023/api/ingridient/info', {
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
					fillInForm('ingridient', resp.data);
				}
				else
				{
					throw new Error(resp.msg);
				}
			});

	}


	const eventEdit = function (e)
	{
		const datagram = serializeForm("ingridient");
		datagram['uuid'] = editting;
		console.log(datagram);

		fetch('http://bandurama.ddns.net:2023/api/ingridient/edit', {
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
					alert("Nie udało się edytować składnika: " + resp.msg);
					throw new Error(resp.msg);
				}
			});

	}


	const eventInsert = function (e)
	{
		const datagram = serializeForm("ingridient");
		datagram['uuid'] = "";

		fetch('http://bandurama.ddns.net:2023/api/ingridient/insert', {
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
					window.location.href = `/admin/ingridientsmgmt?uuid=${resp.data.uuid}`;
				}
				else
				{
					alert("Nie udało się dodać składnika: " + resp.msg);
					throw new Error(resp.msg);
				}
			});

	}


	return (
		<>
			<div className="container">
				<div className="py-4 text-center">
					<h2>Zarządzanie listą składników</h2>
				</div>
				<div className="row g-4">
					<div className="col-md-7 col-lg-8">
						<h4 className="mb-3">Dodaj do listy składników</h4>
						<div className="row g-3">
							<form id="ingridient">
								<div className="col-sm-6">
									<label htmlFor="name" className="form-label">Nazwa</label>
									<input type="text" className="form-control" name="name" placeholder="" required/>
								</div>
								<div className="col-sm-6">
									<label htmlFor="icon" className="form-label">Zdjęcie</label>
									<input type="text" className="form-control" name="icon" placeholder="" required/>
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
