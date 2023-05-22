import {useEffect, useState} from "react";


export default function Holding ()
{
	const [list, setList] = useState([]);
	const reloadList = function ()
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
					setList(resp.data);
				}
				else
				{
					throw new Error(resp.msg);
				}
			});
	}

	useEffect(() => {
		reloadList();
	}, []);

	const eventRemove = function (uuid)
	{
		fetch('http://bandurama.ddns.net:2023/api/holding/remove', {
			method: 'POST',
			body: JSON.stringify({uuid: uuid}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					reloadList();
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
								<div className="col-sm-8"><h2>Lista franczyz</h2></div>
							</div>
						</div>
						<table className="table table-striped">
							<thead>
							<tr>
								<th>#</th>
								<th>uuid</th>
								<th>Lokalizacja</th>
								<th>Menedżer</th>
								<th>Operacje</th>
							</tr>
							</thead>
							<tbody id="lista_render">
							{
								list.map((item, index) => (
									<tr key={item.uuid}>
										<td>{index + 1}</td>
										<td>{item.uuid}</td>
										<td>{item.localization}</td>
										<td>{item.manager}</td>
										<td>
											<a href={`/admin/holdingmgmt?uuid=${item.uuid}`} className="edit" title="Edytuj franczyzę" data-toggle="tooltip" >
												<i className="material-icons">
													&#xE254;
												</i>
											</a>
											<a href="#" className="delete" title="Usuń franczyzę" data-toggle="tooltip" onClick={(e) => eventRemove(item.uuid)}>
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