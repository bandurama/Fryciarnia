/**
 * Paczka z npm oczywiście psuje cały projekt,
 * dlatego też używamy starmodnej metody
 */

const __cdn_href = 'https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css';
const __cdn_material = 'https://fonts.googleapis.com/icon?family=Material+Icons';
const appendLink = function (rel, type, href)
{
	const hw = document.createElement('link');
	hw.rel = rel;
	hw.type = type;
	hw.href = href;
	document.head.appendChild(hw);
}

export function usingBootstrap ()
{
	appendLink('Stylesheet', 'text/css', __cdn_href);
	appendLink('Stylesheet', 'text/css', __cdn_material);
}