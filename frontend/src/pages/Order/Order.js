import TopNav from "../../components/TopNav";
import {useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {acquireGetParams} from "../../utils/Gets";
import '../../styles/Order.css'
import OrderItem from "./OrderItem";
import Footer from "../../components/Footer";
import OrderTakeoutBox from "./OrderTakeoutBox";

export default function Order ()
{
	const { holding } = useParams();

	const [mealList, setMealList] = useState([]);
	const [selected, setSelected] = useState({});
	const [prices, setPrices] = useState({});
	const [totalPrice, setTotalPrice] = useState(0);
	const [actualHolding, setActualHolding] = useState(null);

	const [displayTakoutBox, setDisplayTakeoutBox] = useState(false);

	const checkIfSignedIn = function ()
	{
			fetch('http://bandurama.ddns.net:2023/api/user/ping', {
				method: 'POST',
				body: JSON.stringify({}),
				credentials: 'include'
			})
				.then((response) => response.json())
				.then(resp => {
						if (!resp.ok)
							window.location.href = '/login';
					}
				);
	}
	/**
	 * Most vital element
	 * @param e
	 */
	const performOrder = function (isTakeout)
	{
		const datagram =
		{
			holdingUUID: actualHolding,
			orderedMeals: []
		};

		for (let mealUUID of Object.keys(selected))
			if (selected[mealUUID] > 0)
				datagram.orderedMeals.push
				({
					mealUUID: mealUUID,
					quantity: selected[mealUUID]
				});

		fetch(`http://bandurama.ddns.net:2023/api/order/${isTakeout ? 1 : 0}`, {
			method: 'POST',
			body: JSON.stringify(datagram),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					console.log(resp.data);
					window.location.href = `/payment/${resp.data.uuid}/`;
				}
				else
				{
					if (resp.msg == 'NOEXE')
					{ /* stany magazynowe lub siła wyższa */
						window.location.href = '/error/NO_ORDER';
					}
					throw new Error(resp.msg);
				}
			});

	}

	useEffect(() => {
		/**
		 * FIRST: Destructure _GET params
		 */
		checkIfSignedIn();
		const _GET = acquireGetParams();

		if (_GET != null && Object.keys(_GET).includes("select"))
		{
			const sl = selected;
			sl[_GET.select] = 1;
			setSelected(sl);
		}


		/**
		 * THEN: Fetch available menu from server
		 */

		if (holding != 0)
		{ /* value known by client */
			setActualHolding(holding);
			fetchHoldingMenu(holding);
			return;
		}

		/* or, most-likely stationary terminal */
		fetch('http://bandurama.ddns.net:2023/api/worker/holding', {
			method: 'POST',
			body: JSON.stringify({}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				setActualHolding(resp.data.uuid);
				fetchHoldingMenu(resp.data.uuid);
			});

	}, []);


	const fetchHoldingMenu = function (uuid)
	{
		fetch('http://bandurama.ddns.net:2023/api/menu', {
			method: 'POST',
			body: JSON.stringify(holding == 'all' ? {} : { uuid: uuid }),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					console.log(resp.data);
					setMealList(resp.data);
					/* repr prices as hash table */
					const pc = {};
					for (let meal of resp.data)
						pc[meal.uuid] = parseFloat(meal.price);
					setPrices(pc);
				}
				else
				{
					throw new Error(resp.msg);
				}
			});
	}

	const evtDisableTakeoutBox = function ()
	{
		setDisplayTakeoutBox(false);
	}

	const countCost = function ()
	{
		return Object
			.keys(selected)
			.reduce((a, key) => a + prices[key] * selected[key], 0)
			.toFixed(2);
	}

	return (
		<>
			<TopNav useAccountButton={false} />
			<div className="wrapper">
				<div className="OrderWrapper">
					<div className="meals">
						{mealList.map((meal) => <OrderItem meal={meal} key={meal.uuid} selected={selected} setSelected={setSelected} />)}
					</div>
					<div className="summary">
						<button onClick={(e) => parseInt(countCost()) > 0 && setDisplayTakeoutBox(true)}>ZAPŁAĆ {countCost()} ZŁ</button>
					</div>
				</div>
			</div>
			<Footer/>
			{displayTakoutBox && <OrderTakeoutBox performOrder={performOrder} disabler={(e) => setDisplayTakeoutBox(false)} />}
		</>
	)
}