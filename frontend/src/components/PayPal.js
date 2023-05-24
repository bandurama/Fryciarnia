import {PayPalButtons, PayPalScriptProvider} from "@paypal/react-paypal-js";
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";

export default function PayPal ()
{
	const { order } = useParams();
	const [payAmount, setPayAmount] = useState(null);

	useEffect(() => {
		fetch(`http://bandurama.ddns.net:2023/api/orders/info/${order}`, {
			method: 'POST',
			body: JSON.stringify({}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp => {
				console.log(resp);
				if (resp.ok)
				{
					if (resp.data.dbOrders.orderStatus !== "PAYING")
						window.location.href = '/'; // TODO: Add fallback
					setPayAmount(resp.data.adpOrderMeals.reduce((a, b) => a + b.dbMeal.price * b.dbOrder.quantity, 0).toFixed(2));
				}
			})
	}, []);

	return (
		<>
			{
				payAmount && (
					<>
						<div style={{textAlign: 'center', marginBottom: 50}}>
							<h2 style={{fontFamily: 'Arial'}}>Dokonaj płatności na kwotę {payAmount}zł</h2>
						</div>
						<PayPalScriptProvider options={{ currency: "PLN", "client-id": "ASowtrUmfhsw3PMyd--LgnwrHR6GChuRQxH9OJZQuQ9iVxTy7h2efY6LWGZAn2SBOxKw-j2KLXg3rMJk" }}>
							<PayPalButtons
								createOrder={(data, actions) => {
									return actions.order.create({
										purchase_units: [
											{
												amount: {
													value: payAmount,
												},
											},
										],
									});
								}}
								onCancel = {() =>
									{
										fetch(`http://bandurama.ddns.net:2023/api/orders/fail/${order}`, {
											method: 'POST',
											body: JSON.stringify({}),
											credentials: 'include'
										})
											.then((response) => response.json())
											.then(resp => {
												console.log(resp);
												if (resp.ok)
												{
													window.location.href = "/error/NO_PAY"
												}
											})
									}
								}
								onApprove={(data, actions) => {
									return actions.order.capture().then((details) => {
										const name = details.payer.name.given_name;
										console.log(details)

										fetch(`http://bandurama.ddns.net:2023/api/paypal/${details.id}/${order}`, {
											method: 'POST',
											body: JSON.stringify({}),
											credentials: 'include'
										})
											.then((response) => response.json())
											.then(resp => {
												console.log(resp);
												if (resp.ok)
												{
													window.location.href = `/ticket/${order}`;
												}
											})
									});
								}}
							/>
						</PayPalScriptProvider>
					</>
				)
			}
		</>
	)
}