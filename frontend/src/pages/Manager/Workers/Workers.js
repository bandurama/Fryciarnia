import {useEffect, useState} from "react";

export default function Workers ()
{
	const [cooksList, setCooksList] = useState([]);
	const [hardwareList, setHardwareList] = useState([]);

	const fetchCooksData = function ()
	{
		fetch('http://bandurama.ddns.net:2023/api/user/kitchen/list', {
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
					setCooksList(resp.data);
				}
				else
				{
					throw new Error(resp.msg);
				}
			});
	}


	const fetchHardwareData = function ()
	{
		fetch('http://bandurama.ddns.net:2023/api/user/hardware/list', {
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
					setHardwareList(resp.data);
				}
				else
				{
					throw new Error(resp.msg);
				}
			});
	}
	useEffect(() => {
		fetchCooksData();
		fetchHardwareData();
	}, []);

	const eventRemove = function (uuid)
	{
		fetch('http://bandurama.ddns.net:2023/api/user/remove', {
			method: 'POST',
			body: JSON.stringify({uuid: uuid}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					fetchCooksData();
					fetchHardwareData();
				}
				else
				{
					alert("Błąd: " + resp.msg);
				}
			});
	}

	const onAddDisplay = function (e)
	{
		apiAddHardware("Display");
	}

	const onAddTerminal = function (e)
	{
		apiAddHardware("Terminal");
	}

	const apiAddHardware = function (type)
	{

		fetch('http://bandurama.ddns.net:2023/api/user/hardware/register', {
			method: 'POST',
			body: JSON.stringify({type: type}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					console.log(resp.data);
					fetchHardwareData();
				}
				else
				{
					alert("Nie udało się dodać osprzetu: " + resp.msg);
					throw new Error(resp.msg);
				}
			});
	}

	return (
		<>
			<div className="container-lg">
				<div className="table-responsive">
					<div className="table-wrapper">
						<div className="table-title">
							<div className="row">
								<div className="col-sm-8"><h2>Lista Kucharzy</h2></div>
							</div>
						</div>
						<table className="table table-striped">
							<thead>
							<tr>
								<th>#</th>
								<th>Imie</th>
								<th>Nazwisko</th>
								<th>Mail</th>
								<th>Pensja</th>
								<th>Operacje</th>
							</tr>
							</thead>
							<tbody>
							{
								cooksList.map((item, index) => (
									<tr key={item.uuid}>
										<td>{index + 1}</td>
										<td>{item.name}</td>
										<td>{item.surname}</td>
										<td>{item.mail}</td>
										<td>
											<button type="button" className="btn btn-danger btn-sm" onClick={(e) => eventRemove(item.uuid)}>Zwolnij</button>
										</td>
									</tr>
								))
							}
							</tbody>
						</table>
					</div>
				</div>

				<div className="table-responsive">
					<div className="table-wrapper">
						<div className="table-title">
							<div className="row">
								<div className="col-sm-8"><h2>Terminale i wyświetlacze w restauracji</h2></div>
								<div className="col-sm-4">
									<button type="button" className="btn btn-info" onClick={onAddTerminal}> Dodaj terminal </button>
									<button type="button" className="btn btn-info" onClick={onAddDisplay}> Dodaj wyświetlacz </button>
								</div>
							</div>
						</div>
						<table className="table table-striped">
							<thead>
							<tr>
								<th>#</th>
								<th>Rodzaj Sprzętu</th>
								<th>Login i Hasło</th>
								<th>Operacje</th>
							</tr>
							</thead>
							<tbody>
							{
								hardwareList.map((item, index) => (
									<tr key={item.uuid}>
										<td>{index + 1}</td>
										<td>{item.name}</td>
										<td>{item.mail}</td>
										<td>
											<a href="#" className="delete" title="Zwolnij pracownika" data-toggle="tooltip" onClick={(e) => eventRemove(item.uuid)}>
												<i className="material-icons">
													&#xE872;
												</i>
											</a>
										</td>
									</tr>
								))
							}
							</tbody>

						</table>
					</div>
				</div>
			</div>
		</>
	)
}