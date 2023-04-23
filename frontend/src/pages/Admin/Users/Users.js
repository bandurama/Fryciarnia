import {useEffect, useState} from "react";

export default function Users ()
{
	const [list, setList] = useState([]);

	const fetchData = function ()
	{
		fetch('http://bandurama.ddns.net:2023/api/user/list', {
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
					setList(resp.data);
				}
				else
				{
					throw new Error(resp.msg);
				}
			});
	}

	useEffect(() => {
		fetchData();
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
					fetchData();
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
				<div className="table-responsive">
					<div className="table-wrapper">
						<div className="table-title">
							<div className="row">
								<div className="col-sm-8"><h2>Lista użytkowników</h2></div>
							</div>
						</div>
						<table className="table table-striped">
							<thead>
							<tr>
								<th>#</th>
								<th>uuid</th>
								<th>Konto Google</th>
								<th>Mail</th>
								<th>Nazwisko</th>
								<th>Imie</th>
								<th>Typ konta</th>
								<th>Operacje</th>
							</tr>
							</thead>
							<tbody id="lista_render">
							{
								list.filter((item) => item.type != 'Admin').map((item, index) => (
									<tr key={item.uuid}>
										<td>{index + 1}</td>
										<td>{item.uuid}</td>
										<td>{item.isGoogleAccount ? 'tak' : 'nie'}</td>
										<td>{item.mail}</td>
										<td>{item.surname}</td>
										<td>{item.name}</td>
										<td>{item.type}</td>
										<td>
											<a href={`/admin/usersmgmt?uuid=${item.uuid}`} className="edit" title="Edytuj użytkownika" data-toggle="tooltip" >
												<i className="material-icons">
													&#xE254;
												</i>
											</a>
											<a href="#" className="delete" title="Usuń użytkownika" data-toggle="tooltip" onClick={(e) => eventRemove(item.uuid)}>
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