
import {useEffect, useState} from "react";
import {usingBootstrap} from "../utils/Bootstrap";


export default function OrdersTable ()
{
	const [list, setList] = useState([]);

	const reloadList = function ()
	{
		fetch('http://bandurama.ddns.net:2023/api/orders/list', {
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
		document.title = 'Historia zamówień - Fryciarnia'
		usingBootstrap();
		reloadList();
	}, []);

	const RenderCtlBox = function ({ dbOrders })
	{
		if (window.location.pathname === '/profile/history')
			return <></>;

		return (
			<>
				{dbOrders.orderStatus == 'PAYING' && <img
					src={"/icons/payment.png"}
					className="tableIcon dropdown"
					title="Oznacz jako zapłacone"
					style={{marginRight: 10}}
					onClick={(e) => {
						fetch(`http://bandurama.ddns.net:2023/api/orders/paid/${dbOrders.uuid}`, {
							method: 'POST',
							body: JSON.stringify({}),
							credentials: 'include'
						})
							.then((response) => response.json())
							.then(resp =>
							{
								if (!resp.ok)
									alert(resp.msg);
								else
									window.location.reload();
							});
					}}
				/>}
				{['PAYING', 'PAID'].includes(dbOrders.orderStatus) && <img
					src={"/icons/cancel.png"}
					className="tableIcon dropdown"
					title="Oznacz jako anulowane"
					style={{marginRight: 10}}
					onClick={(e) => {
						fetch(`http://bandurama.ddns.net:2023/api/orders/fail/${dbOrders.uuid}`, {
							method: 'POST',
							body: JSON.stringify({}),
							credentials: 'include'
						})
							.then((response) => response.json())
							.then(resp =>
							{
								if (!resp.ok)
									alert(resp.msg);
								else
									window.location.reload();
							});
					}}
				/>}
			</>
		);
	}

	const RenderTableRow = function ({ n, i, subkey })
	{
		const renderDroppedDown = function (n, i)
		{
			if (!droppedDown.includes(i))
				return <></>

			const RenderDroppedDownRow = function ({ m })
			{
				return (
					<>
						<tr>
							<td></td>
							<td></td>
							<td style={{textAlign: 'right'}}><i>{m.dbOrder.quantity}x {m.dbMeal.name}</i></td>
							<td style={{textAlign: 'left'}}><i>{(m.dbMeal.price * m.dbOrder.quantity).toFixed(2)} zł</i></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</>
				)
			}

			return (
				<>
					{n.adpOrderMeals.map((m) => <RenderDroppedDownRow key={`${subkey}-${m.dbMeal.uuid}`} m={m}/>)}
					<tr>
						<th></th>
						<th></th>
						<th style={{textAlign: 'right'}}>Posiłek</th>
						<th style={{textAlign: 'left'}}>Cena</th>
						<th></th>
						<th></th>
						<th></th>
						<th></th>
					</tr>
					<tr>
						<th colSpan={8}>&nbsp;</th>
					</tr>
				</>
			)
		}
		return (
			<>
				<tr>
					<td>{i + 1}.</td>
					<td>{n.dbOrders.ctime}</td>
					<td style={{textAlign: 'right'}}>{n.dbHolding.localization}</td>
					<td style={{textAlign: 'left'}}>{n.adpOrderMeals.reduce((a, b) => a + b.dbMeal.price * b.dbOrder.quantity, 0).toFixed(2)} zł</td>
					<td><div title={__status_translator[n.dbOrders.orderStatus][1]}>{__status_translator[n.dbOrders.orderStatus][0]}</div></td>
					<td>{n.dbOrders.ticket != 0 && n.dbOrders.ticket}</td>
					<td>{n.dbOrders.isTakeout && <img src="/icons/takeout_black.png" style={{width: 26, height: 26}}/> }</td>
					<td>
						<img
							src={!droppedDown.includes(i) ? '/icons/down.png' : '/icons/up.png'}
							className="tableIcon dropdown"
							onClick={(e) => setDroppedDown(droppedDown.includes(i)
								? droppedDown.filter((a) => a != i)
								: [...droppedDown, i])}
							title="rozwiń"
							style={{marginRight: 10}}
						/>
						<RenderCtlBox dbOrders={n.dbOrders}/>
					</td>
				</tr>
				{renderDroppedDown(n, i)}
			</>
		)
	}

	const [droppedDown, setDroppedDown]= useState([]);

	const renderList = function ()
	{

		const renderBoolIcon = (test, title = null) => (
			<>
				<img src={test ? '/icons/check.png' : '/icons/uncheck.png'} className="tableIcon" title={title}/>
			</>
		)

		return (
			<>
				{list.map((n, i) => <RenderTableRow n={n} i={i} key={n.dbOrders.uuid} subkey={n.dbOrders.uuid}/>)}
			</>
		)
	}

	return (
		<>
			<table className="table table-striped">
				<thead>
				<tr>
					<th>#</th>
					<th>Data zamówienia</th>
					<th style={{textAlign: 'right'}}>Restauracja</th>
					<th style={{textAlign: 'left'}}>Koszt</th>
					<th>Status</th>
					<th>Numerek</th>
					<th>Na wynos</th>
					<th>Operacje</th>
				</tr>
				</thead>
				<tbody id="lista_render">
				{renderList()}
				</tbody>
			</table>
		</>
	)
}