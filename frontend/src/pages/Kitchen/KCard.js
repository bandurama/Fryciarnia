import KItem from "./KItem";

export default function KCard ({ data, dl, sdl, ua })
{
	const parseName = function (data)
	{
		if (data.dbUser.isGoogleAccount)
			return `${data.dbUser.name ?? ''} ${data.dbUser.surname ?? data.dbUser.mail}`;

		return data.dbUser.mail;
	}

	const evtChangeState = function (data, state)
	{
		fetch(`http://bandurama.ddns.net:2023/api/kitchen/${state}/${data.adpOrdersAdpOrderMealDbHolding.dbOrders.uuid}`, {
			method: 'POST',
			body: JSON.stringify({}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				ua();
				if (!resp.ok)
					throw new Error(resp.msg);
			});
	}

	const RenderButton = function ({data})
	{
		if (data.adpOrdersAdpOrderMealDbHolding.dbOrders.orderStatus == 'PAID')
			return (<button onClick={(e) => evtChangeState(data, 'ready') }> &gt; GOTOWE</button>)

		return <button onClick={(e) => evtChangeState(data, 'done') }> &gt; ODEBRANE</button>
	}

	return (
		<div className="k-card">
			<div className="k-title" style={{backgroundColor: data.adpOrdersAdpOrderMealDbHolding.dbOrders.orderStatus == 'PAID' ? '' : '#b85932' }}>
				<div className="k-ticket">
					{data.adpOrdersAdpOrderMealDbHolding.dbOrders.ticket}
				</div>
				<div className="k-info">
					<div className="k-hour">
						{data.adpOrdersAdpOrderMealDbHolding.dbOrders.ctime.split(' ').at(1)}
					</div>
					<div className="k-person">
						{parseName(data)}
					</div>
				</div>
			</div>
			<div className="k-items">
				{data.adpOrdersAdpOrderMealDbHolding.adpOrderMeals.map((n) => <KItem orderId={data.adpOrdersAdpOrderMealDbHolding.dbOrders.uuid} dl={dl} sdl={sdl} data={n} key={n.dbOrder.uuid} />)}
			</div>
			<div className="k-ctl" style={{color: "white"}}>
				<RenderButton data={data} /> { data.adpOrdersAdpOrderMealDbHolding.dbOrders.isTakeout && <img src="/icons/takeout.png" title="Na wynos (uwaga, to nie jest chińska restauracja)" /> }
			</div>
		</div>
	)
}