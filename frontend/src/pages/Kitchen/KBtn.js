

export default function KBtn ({ data })
{
	return (
		<div className="k-btn">
			<div>
				<img src={data.dbIngridient.icon} />
			</div>
			<div style={{fontWeight: "bold"}}>
				x{data.dbStock.quantity}
			</div>
		</div>
	)
}