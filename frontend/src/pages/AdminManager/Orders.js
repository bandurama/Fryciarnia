import {useEffect, useState} from "react";
import TopNav from "../../components/TopNav";

import '../../styles/History.css';
import {usingBootstrap} from "../../utils/Bootstrap";
import OrdersTable from "../../components/OrdersTable";

export default function Orders()
{
	useEffect(() => {
		document.title = 'Historia zamówień - Fryciarnia'
		usingBootstrap();
	}, []);

	return (
		<>
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
		</>
	)
}