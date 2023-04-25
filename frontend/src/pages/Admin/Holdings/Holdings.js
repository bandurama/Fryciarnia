

export default function Holding ()
{
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
											<a href={`/admin/holdingmgmt?uuid=${item.uuid}`} className="edit" title="Edytuj użytkownika" data-toggle="tooltip" >
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