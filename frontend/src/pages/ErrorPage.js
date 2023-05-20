import {useParams} from "react-router-dom";
import TopNav from "../components/TopNav";
import FieldBox from "../components/FieldBox";
import Footer from "../components/Footer";

export default function ErrorPage ()
{
	const { code } = useParams();

	const __error_codes =
	{
		NO_PAY: {title: 'Płatność nie powiodła się!', subtitle: 'Spróbuj ponownie później.', button: ['WYJDŹ', '/']},
		NO_ORDER: {title: 'Błędne zamówienie', subtitle: 'Nie udało się zrealizować zamówienia.', button: ['WYJDŹ', '/']},
		NO_LOGIN: {title: 'Brak sesji', subtitle: 'Przejdź do panelu logowania.', button: ['ZALOGUJ', '/login']},
	};

	return(
		<>
			<TopNav useAccountButton={false} />
			<FieldBox width={1200} IBody={(
				<div style={{width: 500, display: 'flex', justifyContent: 'center', alignItems: 'center', flexDirection: 'column'}}>
					<div className="ntitle">
						{__error_codes[code].title}
					</div>
					<img src='/icons/error.png'/>
					<div className="stitle">
						{__error_codes[code].subtitle}
					</div>
					<div>
						<button className="vButton" onClick={(e) => window.location.href = __error_codes[code].button[1]}>{__error_codes[code].button[0]}</button>
					</div>
				</div>
			)}/>
			<Footer />
		</>
	)
}