
import '../styles/TopNav.css'
import AccountButton from "./AccountButton";
import LogOutButton from "./LogOutButton";

export default function ({useAccountButton = false})
{

	const __profile_alt = `http://bandurama.ddns.net/profile`;

	const buttonify = function ()
	{
		if (window.location.href == __profile_alt)
			return <LogOutButton/>

		return !useAccountButton
			? <></>
			: <AccountButton/>
	}

	return (
		<>
			<div className="top-nav">
				<div className="soc">
					<a href="#"><img src="/icons/fb-icon.png"/></a>
					<a href="#"><img src="/icons/in-icon.png"/></a>
					<a href="#"><img src="/icons/yt-icon.png"/></a>
					<a href="#"><img src="/icons/tok-icon.png"/></a>
				</div>
				<div className="logo">
					<img onClick={(e) => window.location = "/" } src="/logo.png" />
				</div>
				<div className="acc">
					{buttonify()}
				</div>
			</div>
		</>
	)

}