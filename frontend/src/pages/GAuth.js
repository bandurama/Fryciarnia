import {useEffect} from "react";

export default function  GAuth () {

	useEffect(() => {
		/* assuming google hasn't failed us */
		const toks = window.location.href.split('?')
			.at(-1)
			.split('&')
			.map((n) => n.split('='));

		const token = toks
			.filter((n) => n.at(0) === 'access_token')
			.at(0)
			.at(1);

		/* if token isn't presented in uri, bail out */
		if (token == undefined)
			window.location.href = 'http://bandurama.ddns.net/';

		/* if is, tell backend */
		console.log('My token is', token);

		/* tell server to proceed */
		fetch(
			'http://bandurama.ddns.net:2023/api/websession/google/login', {
				method: 'POST',
				body: JSON.stringify({token: token}),
				credentials: 'include'
			})
			.then((response) => response.json())
			.then(resp => {
				// window.location.href = 'http://bandurama.ddns.net/';
				console.log(resp);
			})
	}, []);

	return (
		<>
			<p>Please wait...</p>
		</>
	)
}
