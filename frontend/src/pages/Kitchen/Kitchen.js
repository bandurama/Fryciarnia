
import '../../styles/Kitchen.css'
import {useEffect, useState} from "react";
import KBtn from "./KBtn";
import KCard from "./KCard";

export default function Kitchen ()
{

	const [stockList, setStockList] = useState([]);
	const [orderList, setOrderList] = useState([]);
	const [doneList, setDoneList] = useState([]);

	const updateStockList = function ()
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
					setStockList(resp.data);
				}
				else
				{
					throw new Error(resp.msg);
				}
			});
	}

	const updateOrders = function ()
	{

		fetch('http://bandurama.ddns.net:2023/api/kitchen/orders', {
			method: 'POST',
			body: JSON.stringify({}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					setOrderList(resp.data);
				}
				else
				{
					throw new Error(resp.msg);
				}
			});
	}

	const performGeneralDataUpdate = function ()
	{
		setInterval(() => {
			updateStockList();
			updateOrders();
		}, 1000);
	}

	useEffect(() => {
		document.body.style = "background-color: #212121";
		performGeneralDataUpdate(); /* onMountError ahead */
	}, []);


	const evtLogout = function (e)
	{
		fetch('http://bandurama.ddns.net:2023/api/user/logout', {
			method: 'POST',
			body: JSON.stringify({}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp => {
					window.location.href = '/';
				}
			)
	}

	return (
		<>
			<div className="k-upper-bar">
				<div className="k-stock">
					{stockList.sort((a, b) => a.dbStock.quantity - b.dbStock.quantity).map((item) => <KBtn data={item} key={item.dbStock.uuid} />)}
				</div>
				<div className="k-nav">

					<div className="k-btn" style={{cursor: "pointer"}} onClick={evtLogout}>
						<div>
							<img src="/icons/logout.png" />
						</div>
						<div >
							Wyloguj
						</div>
					</div>

				</div>
			</div>
			<div className="k-contents">
				{orderList.sort((a, b) => a.adpOrdersAdpOrderMealDbHolding.dbOrders.orderStatus.length - b.adpOrdersAdpOrderMealDbHolding.dbOrders.orderStatus.length ).map((n) => <KCard dl={doneList} sdl={setDoneList} data={n} key={n.adpOrdersAdpOrderMealDbHolding.dbOrders.uuid} />)}
			</div>
		</>
	)
}