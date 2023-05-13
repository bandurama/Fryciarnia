import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import TopNav from "../components/TopNav";
import FieldBox from "../components/FieldBox";
import Footer from "../components/Footer";
import '../styles/Ticket.css'

export default function Ticket ()
{
	const { order } = useParams();
	const [ticket, setTicket] = useState(null);

	useEffect(() => {

		fetch(`http://bandurama.ddns.net:2023/api/orders/info/${order}`, {
			method: 'POST',
			body: JSON.stringify({}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					setTicket(resp.data.dbOrders.ticket);
				}
			});

		console.log(order);

	}, []);

	return (
		<>
			<TopNav useAccountButton={false} />
			<FieldBox width={1200} IBody={(
				<div style={{width: 500, display: 'flex', justifyContent: 'center', alignItems: 'center', flexDirection: 'column'}}>
					<div className="ntitle">
						Zamówienie w trakcie realizacji
					</div>
					<div className="numbox">
						{ticket}
					</div>
					<div className="stitle">
						Niebawem otrzymasz swoje zamówienie.
						Numerek będzie wyświetlany na ekranie w restauracji.
						W razie pytań zapytaj obsługę podając numer Twojego zamówienia.
					</div>
					<div>
						<button className="vButton" onClick={(e) => window.location.href = '/'}>WYJDŹ</button>
					</div>
					<div style={{fontFamily: 'arial', fontSize: '16px', color: '#969696', textAlign: 'center', marginTop: '75px'}}>
						Numer twojego zamówienia: <br/>
						{order}
					</div>
				</div>
			)}/>
			<Footer />
		</>
	)
}