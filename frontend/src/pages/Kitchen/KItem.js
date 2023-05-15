

export default function KItem ({ orderId, data, dl, sdl })
{
	const magic = orderId + data.dbMeal.uuid + data.dbOrder.uuid;

	const evtOnClick = function (dl, sdl)
	{
		if (dl.includes(magic))
			sdl(dl.filter((n) => n != magic));
		else
			sdl([...dl, magic]);

	}

	return (
		<div className="k-item" onClick={(e) => evtOnClick(dl, sdl)} style={{textDecoration: dl.includes(magic) ? 'line-through' : 'none'}}>
			{data.dbOrder.quantity}x {data.dbMeal.name}
		</div>
	)
}