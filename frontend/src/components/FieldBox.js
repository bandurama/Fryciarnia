import './../styles/FieldBox.css'
export default function FieldBox ({IBody, width})
{
	return (
		<div className="fieldBox">
			<div style={{width: width}}>
				{IBody}
			</div>
		</div>
	)
}