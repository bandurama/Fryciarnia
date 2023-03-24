import {useEffect} from "react";

export default function Dashboard() {
	useEffect(() => {
		document.title = 'Strona Główna - Fryciarnia'
	}, []);
	return (
		<>
			<p>Dashboard</p>
		</>
	)
}