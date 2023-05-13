
import '../styles/Tickets.css'
import {useEffect, useState} from "react";

export default function Tickets ()
{

	const [pending, setPending] = useState([]);
	const [finished, setFinished] = useState([]);

	const updateData = function ()
	{
		fetch(`http://bandurama.ddns.net:2023/api/tickets`, {
			method: 'POST',
			body: JSON.stringify({}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (!resp.ok)
					return;

				/* convert to fake timestamp */
				for (let d of resp.data)
				{
					const _coprimes = [3600, 60, 1];
					d.ctime = d.ctime.split(' ').at(3).split(':').map((n, i) => n * _coprimes[i]).join('');
				}

				const data = resp.data.sort((a, b) => a.ctime - b.ctime);

				const pend = [];
				const fins = [];

				for (let order of data)
				{
					if (order.orderStatus == 'PAID')
						pend.push(order);
					else if (order.orderStatus == 'READY')
						fins.push(order);

					setFinished(fins);
					setPending(pend);
				}

			});
	}

	useEffect(() => {
		setInterval(() => {
			updateData();
		}, 500);
	}, []);

	return (
		<>
			<div className="ticket-wrapper">
				<div className="ticket-left-pane">
					<div className="ticket-pending-pane ticket-banner">
						OCZEKUJĄCE
					</div>
					<div className="ticket-area">
						{pending.map((p) =>
							<div className="ticket" key={p.uuid}>
								{p.ticket}
							</div>
						)}
					</div>
				</div>
				<div className="ticket-right-pane">
					<div className="ticket-ready-pane ticket-banner">
						GOTOWE
					</div>
					<div className="ticket-area">
						{finished.map((p) =>
							<div className="ticket ticket-ready" key={p.uuid}>
								{p.ticket}
							</div>
						)}
					</div>
				</div>
			</div>
		</>
	)
}