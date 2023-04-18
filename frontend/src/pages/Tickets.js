
import '../styles/Tickets.css'

export default function Tickets ()
{
	return (
		<>
			<div className="ticket-wrapper">
				<div className="ticket-left-pane">
					<div className="ticket-pending-pane ticket-banner">
						OCZEKUJĄCE
					</div>
					<div className="ticket-area">
						<div className="ticket">
							2
						</div>
						<div className="ticket">
							29
						</div>
						<div className="ticket">
							2
						</div>
						<div className="ticket">
							29
						</div>
					</div>
				</div>
				<div className="ticket-right-pane">
					<div className="ticket-ready-pane ticket-banner">
						GOTOWE
					</div>
					<div className="ticket-area">
						<div className="ticket ticket-ready">
							2
						</div>
						<div className="ticket ticket-ready">
							29
						</div>
					</div>
				</div>
			</div>
		</>
	)
}