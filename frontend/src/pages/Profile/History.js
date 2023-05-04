import TopNav from "../../components/TopNav";

import '../../styles/History.css';
import OrdersTable from "../../components/OrdersTable";

export default function History()
{
	return (
		<>
			<TopNav useAccountButton={true}/>
			<div className="formwrapper">
				<div className="bounding-box history-container">
					<div className="container-lg ">
						<div className="table-responsive history-table">
							<div className="table-wrapper">
								<div className="table-title">
									<div className="row">
										<div className="col-sm-12 mb-3">
											<h2>
												Historia zamówień
											</h2>
										</div>
									</div>
								</div>
								<OrdersTable/>
							</div>
						</div>
					</div>
				</div>
			</div>
		</>
	)
}