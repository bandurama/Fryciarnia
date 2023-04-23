
export function acquireGetParams ()
{
	const href = window.location.href;
	const literal = href.split('?');
	if (literal.length <= 1)
		return null;

	const pairs = literal
		.at(-1)
		.split('&')
		.map((n) => n.split('='));

	const obj = {};
	for (let pair of pairs)
		obj[pair[0]] = pair[1];

	return obj;
}