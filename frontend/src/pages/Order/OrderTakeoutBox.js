
export default function OrderTakeoutBox ({ performOrder, disabler })
{
	return (
		<>
			<div className="takeout-shade" onClick={(e) => disabler()}></div>
			<div className="takeout-box">
				<div className="takeout-item" onClick={(e) => performOrder(false)}>
					<img src="/icons/inside.png" />
					Na Miejscu
				</div>
				<div className="takeout-item" onClick={(e) => performOrder(true)}>
					<img src="/icons/takeout_black.png" />
					Na Wynos
				</div>
			</div>
		</>
	)
}