import {useState} from "react";

export default function Stock ()
{

	const [list, setList] = useState([]);

	return (
		<>
			<div className="container-lg">
				<div className="table-responsive">
					<div className="table-wrapper">
						<div className="table-title">
							<div className="row">
								<div className="col-sm-8"><h2>Stany magazynowe</h2></div>
							</div>
						</div>
						<table className="table table-striped">
							<thead>
							<tr>
								<th>#</th>
								<th>Nazwa składnika</th>
								<th>Ilość dostępna</th>
							</tr>
							</thead>
							<tbody id="lista_render">
							{
								list.map((item, index) => (
									<tr key={item.uuid}>
										<td>{index + 1}</td>
										<td>{item.manager}</td>
										<td>
											<a href={`/admin/holdingmgmt?uuid=${item.uuid}`} className="edit" title="Edytuj użytkownika" data-toggle="tooltip" >
												<i className="material-icons">
													&#xE254;
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