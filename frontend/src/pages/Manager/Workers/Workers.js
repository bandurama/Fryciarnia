import {useEffect, useRef, useState} from "react";
import ActiveTextBox from "../../../utils/ActiveTextBox";

export default function Workers ()
{
	const [cooksList, setCooksList] = useState([]);
	const [hardwareList, setHardwareList] = useState([]);

	const [hireUrl, setHireUrl] = useState('');
	const hireBox = useRef();

	const fetchHireUrl = function ()
	{

		fetch('http://bandurama.ddns.net:2023/api/worker/hire', {
			method: 'POST',
			body: JSON.stringify({}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					setHireUrl(resp.data);
				}
				else
				{
					throw new Error(resp.msg);
				}
			});
	}

	const reloadData = function ()
	{
		fetch('http://bandurama.ddns.net:2023/api/worker/list', {
			method: 'POST',
			body: JSON.stringify({}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					setCooksList(resp.data.people);
					setHardwareList(resp.data.hardware)
					console.log(resp.data);
				}
				else
				{
					throw new Error(resp.msg);
				}
			});
	}

	useEffect(() => {
		reloadData();
		fetchHireUrl();
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
					reloadData();
				}
				else
				{
					alert("Błąd: " + resp.msg);
				}
			});
	}


	return (
		<>
			<div className="container-lg">
				<div className="table-responsive mb-5">
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
									<tr key={item.user.uuid}>
										<td>{index + 1}</td>
										<td><ActiveTextBox value={item.user.name} type="text" defaultProps={{uuid: item.user.uuid}} propName="name" endpoint="http://bandurama.ddns.net:2023/api/worker/cook/edit" style={{width:300}}/></td>
										<td><ActiveTextBox value={item.user.surname} type="text" defaultProps={{uuid: item.user.uuid}} propName="surname" endpoint="http://bandurama.ddns.net:2023/api/worker/cook/edit" style={{width:300}}/></td>
										<td>{item.user.mail}</td>
										<td><ActiveTextBox value={item.worker.salary} type={"number"} defaultProps={{uuid: item.user.uuid}} propName="salary" endpoint="http://bandurama.ddns.net:2023/api/worker/cook/edit" style={{width:300}}/></td>
										<td>
											<button type="button" className="btn btn-danger btn-sm" onClick={(e) => eventRemove(item.user.uuid)}>Zwolnij</button>
										</td>
									</tr>
								))
							}
							</tbody>
						</table>
					</div>
				</div>

				<div className="table-responsive mt-2">
					<div className="table-wrapper">
						<div className="table-title">
							<div className="row">
								<div className="col-sm-8"><h2>Terminale i wyświetlacze w restauracji</h2></div>
								<div className="col-sm-4">
									{/*<button type="button" className="btn btn-info" onClick={onAddTerminal}> Dodaj terminal </button>*/}
									{/*<button type="button" className="btn btn-info" onClick={onAddDisplay}> Dodaj wyświetlacz </button>*/}
								</div>
							</div>
						</div>
						<table className="table table-striped">
							<thead>
							<tr>
								<th>#</th>
								<th>Rodzaj Sprzętu</th>
								<th>Login i Hasło</th>
								{/*<th>Operacje</th>*/}
							</tr>
							</thead>
							<tbody>
							{
								hardwareList.map((item, index) => (
									<tr key={item.user.uuid}>
										<td>{index + 1}</td>
										<td>{item.user.name}</td>
										<td>{item.user.mail}</td>
									</tr>
								))
							}
							</tbody>
						</table>
					</div>
				</div>
				<hr className="my-5" />
				<div>Link do rejestracji konta dla pracownika kuchni:</div>
				<div className="input-group mb-3" style={{width: 500}}>
					<input type="text" className="form-control" id="betterthenreact" value={hireUrl} disabled={true}/>
						<div className="input-group-append">
							<button className="btn btn-outline-secondary" onClick={(e) => window.location.href = hireUrl}>Przejdź</button>
						</div>
				</div>
			</div>
		</>
	)
}