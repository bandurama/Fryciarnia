import TopNav from "../components/TopNav";
import Footer from "../components/Footer";

import '../styles/Menu.css';

export default function Menu ()
{
	return (
		<>
			<TopNav useAccountButton={true}/>
			<div className="slides">
				<img src="/menu/strip.jpg"/>
			</div>
			<div className="sub-menu" onClick={(e) => window.location = "/" }>
				<img src="/icons/back.png"/>
				Wróć do <b>&nbsp;Strony Głównej</b>
			</div>
			<div className="wrapper">


				<div className="menu-item">
					<div className="menu-item-image" style={{backgroundImage: 'url("/menu/frytki.jpg")'}}></div>
					<div className="menu-item-title">
						Frytki
					</div>
				</div>


				<div className="menu-item">
					<div className="menu-item-image" style={{backgroundImage: 'url("/menu/frytki.jpg")'}}></div>
					<div className="menu-item-title">
						Frytki
					</div>
				</div>


				<div className="menu-item">
					<div className="menu-item-image" style={{backgroundImage: 'url("/menu/frytki.jpg")'}}></div>
					<div className="menu-item-title">
						Frytki
					</div>
				</div>


				<div className="menu-item">
					<div className="menu-item-image" style={{backgroundImage: 'url("/menu/frytki.jpg")'}}></div>
					<div className="menu-item-title">
						Frytki
					</div>
				</div>


				<div className="menu-item">
					<div className="menu-item-image" style={{backgroundImage: 'url("/menu/frytki.jpg")'}}></div>
					<div className="menu-item-title">
						Frytki
					</div>
				</div>


				<div className="menu-item">
					<div className="menu-item-image" style={{backgroundImage: 'url("/menu/frytki.jpg")'}}></div>
					<div className="menu-item-title">
						Frytki
					</div>
				</div>


				<div className="menu-item">
					<div className="menu-item-image" style={{backgroundImage: 'url("/menu/frytki.jpg")'}}></div>
					<div className="menu-item-title">
						Frytki
					</div>
				</div>


				<div className="menu-item">
					<div className="menu-item-image" style={{backgroundImage: 'url("/menu/frytki.jpg")'}}></div>
					<div className="menu-item-title">
						Frytki
					</div>
				</div>


				<div className="menu-item">
					<div className="menu-item-image" style={{backgroundImage: 'url("/menu/frytki.jpg")'}}></div>
					<div className="menu-item-title">
						Frytki
					</div>
				</div>


				<div className="menu-item">
					<div className="menu-item-image" style={{backgroundImage: 'url("/menu/frytki.jpg")'}}></div>
					<div className="menu-item-title">
						Frytki
					</div>
				</div>

				<div className="menu-item">
					<div className="menu-item-image" style={{backgroundImage: 'url("/menu/frytki.jpg")'}}></div>
					<div className="menu-item-title">
						Frytki
					</div>
				</div>

			</div>
			<Footer/>
		</>
	)
}