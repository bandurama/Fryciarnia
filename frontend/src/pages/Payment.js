import {PayPalButtons, PayPalScriptProvider} from "@paypal/react-paypal-js";

export default function Payment ()
{
	return (
		<PayPalScriptProvider options={{ "client-id": "ASowtrUmfhsw3PMyd--LgnwrHR6GChuRQxH9OJZQuQ9iVxTy7h2efY6LWGZAn2SBOxKw-j2KLXg3rMJk" }}>
			<PayPalButtons
				createOrder={(data, actions) => {
					return actions.order.create({
						purchase_units: [
							{
								amount: {
									value: "1.99",
								},
							},
						],
					});
				}}
				onApprove={(data, actions) => {
					return actions.order.capture().then((details) => {
						const name = details.payer.name.given_name;
						console.log(details)

						fetch(`http://bandurama.ddns.net:2023/api/paypal/${details.id}`, {
							method: 'POST',
							body: JSON.stringify({}),
							credentials: 'include'
						})
							.then((response) => response.json())
							.then(resp => {
								console.log(resp);
							})
					});
				}}
			/>
		</PayPalScriptProvider>
	)
}