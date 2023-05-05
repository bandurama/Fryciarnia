import PayPal from "../components/PayPal";
import TopNav from "../components/TopNav";
import Footer from "../components/Footer";
import FieldBox from "../components/FieldBox";

export default function Payment ()
{
	return (
		<>
			<TopNav useAccountButton={false} />
				<FieldBox width={1200} IBody={(
					<div style={{width: 500}}>
						<PayPal/>
					</div>

				)}/>
			<Footer />
		</>
	)
}