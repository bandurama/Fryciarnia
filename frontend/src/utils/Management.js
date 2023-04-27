import {usingBootstrap} from "./Bootstrap";
import {useParams} from "react-router-dom";
import {useEffect} from "react";

export default function Management ({ nav, views, onLogout, title })
{

	const { option } = useParams();

	const displayView = function ()
	{
		return Object.keys(views).includes(option)
			? views[option]
			: (<>error!</>)
	}

	useEffect(() => {
		usingBootstrap();
	}, [])

	console.log(nav);

	/**
	 * What a usefull and joyfull framewor
	 * it really saves developer time
	 * -- says nobody
	 */
	return (
		<>
			<header className="bg-light">
				<div className="container-fluid ">
					<nav className="navbar navbar-light">
						<a className="navbar-brand" href="#">{title}</a>
						<div className="navbar-nav">
							<button className="btn btn-info btn-sm mx-4" id="wyloginator" onClick={onLogout}>Wyloguj</button>
						</div>
					</nav>
				</div>
			</header>
			<div className="container-fluid">
				<div className="row">
					<nav className="col bg-light">
							{
								nav.map((section) => (
									<div key={section.title}>
										<h3 key={`H3${section.title}`}>{section.title}</h3>
										<ul className="nav flex-column" key={`UL${section.title}`}>
										{
											section.routes.map((route) => (
												<div key={`DIV${route.href}`}>
													<li className="nav-item px-1" key={`LI${route.href}`}>
														<a className="nav-link" href={route.href} key={`A${route.href}`}>{route.name}</a>
													</li>
												</div>
											))
										}
										</ul>
									</div>
								))
							}
					</nav>
					<main className="col-10 h-100 px-1 py-5 mb-5">
						{displayView()}
					</main>
				</div>
			</div>
			<footer>
			</footer>
		</>
	)

}