

export default function KBtn ({ data })
{
	return (
		<div className="k-btn">
			<div>
				<img src={data.dbIngridient.icon} title={data.dbIngridient.name} />
			</div>
			<div style={{fontWeight: "bold"}}>
				x{data.dbStock.quantity}
			</div>
		</div>
	)
}