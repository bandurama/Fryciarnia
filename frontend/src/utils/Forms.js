

const __serializeFormTraverse = function (dataset, root)
{
	const children = root.children;
	for (let child of children)
	{ /* if element of specific type, append to dataset,
			 else traverse further */
		__serializeFormTraverse(dataset, child);
	}
	if (['INPUT', 'SELECT'].includes(root.tagName))
	{
		const value = root.tagName == 'INPUT' && root.type == 'checkbox'
			? root.checked
			: root.value;

		dataset.push({
			name: root.name,
			value: value
		})
	}
}

export function serializeForm (formId)
{
	const form = document.getElementById(formId);
	if (form == undefined)
		throw new Error(`Form #${formId} does not exists!`);

	const dataset = [];
	const obj = {};
	__serializeFormTraverse(dataset, form);
	for (let data of dataset)
		obj[data.name] = data.value;
	return obj;
}


const __fillInFormTraverse = function (dataset, root)
{
	const children = root.children;
	for (let child of children)
		__fillInFormTraverse(dataset, child);

	if (['INPUT', 'SELECT'].includes(root.tagName))
	{
		if (Object.keys(dataset).includes(root.name))
		{
			const value = dataset[root.name];
			if (root.type == 'checkbox')
				root.checked = value;
			else
				root.value = value;
		}
	}
	//
	// if (root.tagName == 'SELECT')
	// {
	// 	console.log('setting', root, ' to ', dataset[root.name])
	// 	root.selected = dataset[root.name];
	// }
}

export function fillInForm (formId, datagram)
{
	const form = document.getElementById(formId);
	if (form == undefined)
		throw new Error(`Form #${formId} does not exists!`);
	__fillInFormTraverse(datagram, form);
}