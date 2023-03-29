import {useEffect, useState} from "react";
import TopNav from "../../components/TopNav";

import '../../styles/History.css';
import {usingBootstrap} from "../../utils/Bootstrap";

export default function History()
{

	useEffect(() => {
		document.title = 'Historia zamówień - Fryciarnia'
		usingBootstrap();
	}, []);


	const [history, setHistory] = useState
	([{ /* from adapter */
		item:
		{
			/* main order */
			ctime: "2023-01-01 11:35",
			holding: "Restauracja Galeria Echo Kielce",
			isCashed: true,
			isOut: false,
			isTakeout: true,
			isCanceled: false

		},
		list:
		[{
			/* items  */
			name: "Frytki",
			price: 10.99
		},
			{
				name: "Ketchup",
				price: 0.99
			}]
	},
		{ /* from adapter */
			item:
				{
					/* main order */
					ctime: "2023-01-10 13:35",
					holding: "Restauracja Galeria Echo Kielce", /* rebound in controller */
					isCashed: true,
					isOut: false,
					isTakeout: true,
					isCanceled: false

				},
			list:
				[{
					/* items  */
					name: "Frytki Belgijskie",
					price: 7.99
				},
					{
						name: "Majonez",
						price: 1.49
					}]
		}
	]);

	const [droppedDown, setDroppedDown]= useState([]);

	const renderList = function ()
	{
		const renderDroppedDown = function (n, i)
		{
			if (!droppedDown.includes(i))
				return (
					<>
					</>
				)

			return (
				<>
					{n.list.map((m) =>
						<>
							<tr>
								<td></td>
								<td></td>
								<td>{m.name}</td>
								<td>{m.price} zł</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
						</>
					)}
					<tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
					</tr>
				</>
			)
		}

		const renderBoolIcon = (test, title = null) => (
			<>
				<img src={test ? '/icons/check.png' : '/icons/uncheck.png'} className="tableIcon" title={title}/>
			</>
		)

		return (
			<>
				{history.map((n, i) =>
					<>
						<tr>
							<td>{i + 1}.</td>
							<td>{n.item.ctime}</td>
							<td>{n.item.holding}</td>
							<td>{n.list.reduce((a, b) => a + b.price, 0)} zł</td>
							<td>{renderBoolIcon(n.item.isCashed, "Czy opłata została umieszczona")}</td>
							<td>{renderBoolIcon(n.item.isOut, "Czy zamówienie zostało wydane")}</td>
							<td>{n.item.isTakeout ? 'na miejscu' : 'na wynos'}</td>
							<td>
								<img
									src={!droppedDown.includes(i) ? '/icons/down.png' : '/icons/up.png'}
									className="tableIcon dropdown"
									onClick={(e) => setDroppedDown(droppedDown.includes(i)
											? droppedDown.filter((a) => a != i)
											: [...droppedDown, i])}
									title="rozwiń"
								/>
							</td>
						</tr>
						{renderDroppedDown(n, i)}
					</>
				)}
			</>
		)
	}

	return (
		<>
			<TopNav useAccountButton={true}/>
			<div className="formwrapper">
				<div className="bounding-box history-container">
					<div className="container-lg ">
						<div className="table-responsive history-table">
							<div className="table-wrapper">
								<div className="table-title">
									<div className="row">
										<div className="col-sm-12 mb-3">
											<h2>
												Historia zamówień
											</h2>
										</div>
									</div>
								</div>
								<table className="table table-striped">
									<thead>
									<tr>
										<th></th>
										<th>Data zamówienia</th>
										<th>Restauracja</th>
										<th>Koszt</th>
										<th>Opłata</th>
										<th>Odebrano</th>
										<th>Rodzaj</th>
										<th>Operacje</th>
									</tr>
									</thead>
									<tbody id="lista_render">
										{renderList()}
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>

			</div>
		</>
	)
}