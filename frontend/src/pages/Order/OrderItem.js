
export default function OrderItem ({ meal, selected, setSelected })
{

	const updateSelectedState = function (delta)
	{
		const sl = {};
		for (let key of Object.keys(selected))
			sl[key] = selected[key];
		if (!Object.keys(sl).includes(meal.uuid))
			sl[meal.uuid] = 0;
		sl[meal.uuid] += delta;
		setSelected(sl);
	}

	const isRenderableOverZero = function ()
	{
		return Object.keys(selected).includes(meal.uuid)
				&& selected[meal.uuid] > 0;
	}

	return (
		<div>
			<div className="icon">
				<img src={meal.image} />
			</div>
			<div className="name">
				{meal.name}
			</div>
			<div className="ctl">
				<div>
					{isRenderableOverZero() && <button className="sub" onClick={(e) => updateSelectedState(-1)}>-</button>}
					{!isRenderableOverZero() && `${meal.price}zł`}
				</div>
				<div>
					{isRenderableOverZero() ? `x${selected[meal.uuid]}` : ""}
				</div>
				<div>
					<button className="add" onClick={(e) => updateSelectedState(+1)}>+</button>
				</div>
			</div>
		</div>
	)
}