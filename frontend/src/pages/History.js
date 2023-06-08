import {useParams} from "react-router-dom";
import TopNav from "../components/TopNav";
import Footer from "../components/Footer";
import '../styles/History.css'
import {useEffect} from "react";

export default function History ()
{
	useEffect(() => {
		document.title = 'Historia | Fryciarnia';
	})
	return (
		<>
			<TopNav useAccountButton={true} />
			<img src="/baner.jpg" style={{width: '100%', marginTop: '100px'}} />
			<div className="wrapper">
				<div className="card" id="card-history">
					<div className="title">O Fryciarni</div>
					<div className="descr">
						Witaj w naszej fryciarni, gdzie serwujemy najsmaczniejsze frytki!
						Nasze frytki są przygotowywane z najwyższej jakości ziemniaków, starannie krojone i smażone na złocisty kolor.
						Od klasycznych frytek po odmiany jak krzywoliniowe, słodkie z batatów czy z dyni, mamy coś dla każdego podniebienia.
						Nasza fryciarnia oferuje przyjazną atmosferę i niezapomniane doznania kulinarne dla miłośników tego klasycznego dania.
					</div>
				</div>
				<div className="card" style={{backgroundImage: 'url(history0.avif)'}}>
				</div>
				<div className="card" id="card-search" style={{backgroundImage: 'url(history1.avif)'}}>
				</div>
				<div className="card" id="card-history">
					<div className="title">Historia</div>
					<div className="descr">
						Nasza fryciarnia wywodzi się z małej, rodzinnej straganu,
						gdzie pan Fryciarz serwował swoje wyjątkowe frytki. Zainteresowanie i popularność
						przyciągnęły klientów z odległych miejscowości, co zainspirowało pana Fryciarza
						do otwarcia pierwszej fryciarni. Sukces tej fryciarni sprawił, że pan
						Fryciarz postanowił stworzyć sieć franczyz, dzieląc się swoim smakiem na całym świecie.
						Dzięki unikalnemu przepisowi i pasji pan Fryciarz, nasza sieć fryciarni rozrosła się na
						międzynarodową skalę, serwując najsmaczniejsze frytki na całym świecie.
						Nasza historia jest historią pasji, smaku i podróży w fascynujący świat frytek.
					</div>
				</div>
				<div className="card" id="card-history">
					<div className="title">Nasi Kucharze</div>
					<div className="descr">
						W naszej fryciarni nasi kucharze są nie tylko wykwalifikowani, ale także pełni pasji do przygotowywania wyjątkowych frytek.
						To profesjonaliści, którzy doskonale znają tajniki przyrządzania idealnych frytek, od ich cięcia po
						odpowiednią technikę smażenia. Naszym kucharzom zależy na jakości i świeżości składników, dlatego starannie
						wybierają najlepsze ziemniaki i używają naturalnych przypraw. Kucharze w naszej fryciarni są kreatywni i otwarci
						na eksperymenty, tworząc nowe, innowacyjne smaki frytek i dodatków. To właśnie dzięki umiejętnościom i zaangażowaniu
						naszych kucharzy możemy dostarczać naszym klientom niezapomniane doznania kulinarne.
					</div>
				</div>
				<div className="card" style={{backgroundImage: 'url(history3.avif)'}}>
				</div>
				<div className="card" id="card-search" style={{backgroundImage: 'url(history4.avif)'}}>
				</div>
				<div className="card" id="card-history">
					<div className="title">Przyjazna atmosfera</div>
					<div className="descr">
						Atmosfera w naszych restauracjach jest przyjazna, ciepła i pełna energii.
						Tworzymy miejsce, w którym nasi goście czują się komfortowo i swobodnie. Niezależnie od tego,
						czy odwiedzasz nas na szybką przekąskę czy na dłuższy pobyt, nasza atmosfera jest zawsze przyjemna
						i relaksująca. Nasi pracownicy są uśmiechnięci, pomocni i gotowi zapewnić Ci wyjątkowe doświadczenie
						gastronomiczne. Nasze restauracje są zaprojektowane w nowoczesnym stylu, tworząc przytulne wnętrza,
						które sprzyjają relaksowi i rozmowom. Przyjemna muzyka i aromatyczne zapachy naszych frytek dodatkowo
						podkreślają atmosferę, tworząc niezapomniany nastrój dla naszych gości.
					</div>
				</div>
			</div>
			<Footer/>
		</>
	)
}