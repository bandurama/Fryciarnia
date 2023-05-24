import '../styles/Main.css'; /* TODO: MOVE TO SEPARATE FILE */

export default function Footer ()
{
	return (
		<>
			<div className="bottom-nav">
				<a href="/menu/all">NASZE MENU</a>
				<a href="http://bandurama.ddns.net:2023/red/locations" target="_blank">WSZYSTKIE LOKALE</a>
				<a href="#">ALERGENY</a>
				<a href="/history">HISTORIA FRYCIARNI</a>
				<a href="https://weaii.tu.kielce.pl/">PŚK</a>
				<a href="#">FRANCZYZA</a>
			</div>
			<div className="footer">
				<div>
					&copy; 2023 Projekt ISI+PAI - Ślusarczyk, Bandura - Koordynator mgr inż. Mateusz Pawełkiewicz.
				</div>
				<div>
					<a href="https://github.com/bandurama/Fryciarnia">GitHub</a>
					<a href="https://achilles.tu.kielce.pl/portal/Members/7637837b75044f12a9338bd380002931/projektowanie-aplikacji-internetowych-2/projekt">Achilles</a>
					<a href="http://marty.cba.pl/">Ślusarczyk</a>
					<a href="http://maciej-bandura.j.pl/">Bandura</a>
					<a href="#">WEAiI</a>
				</div>
			</div>
		</>
	)
}