
import '../styles/TopNav.css'

export default function ({useAccountButton = false})
{

	const btnClick = function ( e )
	{
		console.log(e);
		window.location.href = './login'
	}

	const buttonify = function ()
	{
		return !useAccountButton
			? <></>
			: <button onClick={btnClick}>DOŁĄCZ DO NAS</button>
	}

	return (
		<>
			<div className="top-nav">
				<div className="soc">
					<a href="#"><img src="./icons/fb-icon.png"/></a>
					<a href="#"><img src="./icons/in-icon.png"/></a>
					<a href="#"><img src="./icons/yt-icon.png"/></a>
					<a href="#"><img src="./icons/tok-icon.png"/></a>
				</div>
				<div className="logo">
					<img src="./logo.png" />
				</div>
				<div className="acc">
					{buttonify()}
				</div>
			</div>
		</>
	)

}