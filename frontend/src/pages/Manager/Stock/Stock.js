import {useEffect, useState} from "react";
import ActiveTextBox from "../../../utils/ActiveTextBox";

export default function Stock ()
{

	const [list, setList] = useState([]);

	const reloadList = function ()
	{
		fetch('http://bandurama.ddns.net:2023/api/stock/list', {
			method: 'POST',
			body: JSON.stringify({}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					console.log(resp);
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
							<tbody>
							{
								list.map((item, index) => (
									<tr key={item.dbStock.uuid}>
										<td>{index + 1}</td>
										<td>{item.dbIngridient.name}</td>
										<td><ActiveTextBox value={item.dbStock.quantity} type={"number"} defaultProps={{uuid: item.dbStock.uuid}} propName="quantity" endpoint="http://bandurama.ddns.net:2023/api/stock/quantity/edit" style={{width:300}}/></td>
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