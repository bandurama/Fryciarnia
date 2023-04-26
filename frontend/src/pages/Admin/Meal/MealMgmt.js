import {useEffect, useState} from "react";
import {acquireGetParams} from "../../../utils/Gets";
import {fillInForm, serializeForm} from "../../../utils/Forms";

export default function MealMgmt ()
{
	const [editting, setEditting] = useState(null);

	const [tbxQuantity, setTbxQuantity] = useState("");
	const [tbxInstruction, setTbxInstruction] = useState("");
	const [tbxStep, setTbxStep] = useState(null);

	const [recipeList, setRecipeList] = useState([]);

	const [ingridientList, setIngridientList] = useState([]);

	const reloadIngridientList = function ()
	{
		fetch('http://bandurama.ddns.net:2023/api/ingridient/list', {
			method: 'POST',
			body: JSON.stringify({}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					console.log(resp.data);
					setIngridientList(resp.data);
				}
				else
				{
					throw new Error(resp.msg);
				}
			});
	}

	const reloadRecipeList = function (uuid)
	{
		/**
		 * First clear out bottom textboxes
		 */
		setTbxQuantity("");
		setTbxInstruction("");
		setTbxStep(null);
		/**
		 * then fetch recipe data
		 */
		fetch('http://bandurama.ddns.net:2023/api/recipe/list', {
			method: 'POST',
			body: JSON.stringify({uuid: uuid}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					console.log(resp.data);
					setRecipeList(resp.data);
					setTbxStep(resp.data.sort((a, b) => a.dbRecipe.step - b.dbRecipe.step).at(-1).dbRecipe.step + 10);
					// setTbxStep(resp.data.)
				}
				else
				{
					throw new Error(resp.msg);
				}
			});
	}

	useEffect(() => {
		const _get = acquireGetParams();
		if (_get != null)
			if (Object.keys(_get).includes('uuid'))
			{ /* also fetch */
				setEditting(_get.uuid);
				fillInUserInfo(_get.uuid);
				reloadRecipeList(_get.uuid);
				reloadIngridientList();
			}
	}, []);

	const fillInUserInfo = function (uuid)
	{
		fetch('http://bandurama.ddns.net:2023/api/meal/info', {
			method: 'POST',
			body: JSON.stringify({uuid: uuid}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					console.log(resp.data);
					fillInForm('meal', resp.data);
				}
				else
				{
					throw new Error(resp.msg);
				}
			});

	}


	const eventEdit = function (e)
	{
		const datagram = serializeForm("meal");
		datagram['uuid'] = editting;
		console.log(datagram);

		fetch('http://bandurama.ddns.net:2023/api/meal/edit', {
			method: 'POST',
			body: JSON.stringify(datagram),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					console.log(resp.data);
					window.location.reload();
				}
				else
				{
					alert("Nie udało się edytować posiłku: " + resp.msg);
					throw new Error(resp.msg);
				}
			});
	}

	const eventInsert = function (e)
	{
		const datagram = serializeForm("meal");
		datagram['uuid'] = "";
		console.log(datagram);

		fetch('http://bandurama.ddns.net:2023/api/meal/insert', {
			method: 'POST',
			body: JSON.stringify(datagram),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					console.log(resp.data);
					window.location.href = `/admin/mealsmgmt?uuid=${resp.data.uuid}`;
				}
				else
				{
					alert("Nie udało się dodać posiłku: " + resp.msg);
					throw new Error(resp.msg);
				}
			});
	}

	const onRemoveStep = function (stepId)
	{
		fetch('http://bandurama.ddns.net:2023/api/recipe/remove', {
			method: 'POST',
			body: JSON.stringify({uuid: stepId}),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					reloadRecipeList(editting);
				}
				else
				{
					alert("Nie udało się usunąć kroku: " + resp.msg);
					throw new Error(resp.msg);
				}
			});
	}

	const onInsertNewStep = function (e)
	{
		/* here we are adding new step to recipe of this meal */
		const datagram = serializeForm("newStepForm");
		datagram['meal'] = editting;
		datagram['uuid'] = "";
		console.log(datagram);

		fetch('http://bandurama.ddns.net:2023/api/recipe/insert', {
			method: 'POST',
			body: JSON.stringify(datagram),
			credentials: 'include'
		})
			.then((response) => response.json())
			.then(resp =>
			{
				if (resp.ok)
				{
					reloadRecipeList(editting);
				}
				else
				{
					alert("Nie udało się dodać kroku: " + resp.msg);
					throw new Error(resp.msg);
				}
			});
	}


	const renderListOfIngridients = function ( )
	{ /* ONLY IF editing */
		return (
			<div className="table-responsive">
				<div className="table-wrapper">
					<div className="table-title">
						<div className="row">
							<div className="col-sm-8"><h2>Marszruta</h2></div>
						</div>
					</div>
					<table className="table table-striped">
						<thead>
						<tr>
							<th>Numer kroku</th>
							<th>Składnik</th>
							<th>Ilość</th>
							<th>Instrukcja</th>
							<th>Operacje</th>
						</tr>
						</thead>
						<tbody id="lista_render">
						{
							recipeList.sort((a, b) => a.dbRecipe.step - b.dbRecipe.step).map((item) => (
								<tr key={item.dbRecipe.uuid}>
									<td>{item.dbRecipe.step}</td>
									<td>{item.dbIngridient.name}</td>
									<td>{item.dbRecipe.quantity}</td>
									<td>{item.dbRecipe.instruction}</td>
									<td>
										<a className="edit" title="Usuń krok" data-toggle="tooltip" onClick={(e) => onRemoveStep(item.dbRecipe.uuid)} >
											<i className="material-icons">
												&#xE872;
											</i>
										</a>
									</td>
								</tr>
							))
						}
						<tr id="newStepForm">
							<td><input type="number" className="form-control" placeholder="Krok" name="step" value={tbxStep} onChange={(e) => setTbxStep(e.target.value) } /></td>
							<td>
								<select className="form-select" name="ingridient" required>
									<option value="" disabled selected>Wybierz</option>
									{
										ingridientList.map((ingridient) =>
											<option key={ingridient.uuid} value={ingridient.uuid}>{ingridient.name}</option>)
									}
								</select>
							</td>
							<td><input type="number" className="form-control" placeholder="Ilość" name="quantity" value={tbxQuantity} onChange={(e) => setTbxQuantity(e.target.value) }/></td>
							<td><input type="text" className="form-control" placeholder="Instrukcja" name="instruction" value={tbxInstruction} onChange={(e) => setTbxInstruction(e.target.value) } /></td>
							<td>
								<a className="edit" title="Dodaj krok" data-toggle="tooltip" onClick={onInsertNewStep}>
									<i className="material-icons">
										add
									</i>
								</a>
							</td>
						</tr>

						</tbody>
					</table>
				</div>
			</div>
		)
	}


	return (
		<>
			<div className="container">
				<div className="py-4 text-center">
					<h2>Zarządzanie listą posiłków</h2>
				</div>
				<div className="row g-4">
					<div className="col-md-7 col-lg-8">
						<h4 className="mb-3">Dodaj do listy posiłków</h4>
						<div className="row g-3">
							<form id="meal">
								<div className="col-sm-6">
									<label htmlFor="name" className="form-label">Nazwa</label>
									<input type="text" className="form-control" name="name" placeholder="" required/>
								</div>

								<div className="col-sm-6">
									<label htmlFor="price" className="form-label">Cena</label>
									<input type="number" className="form-control" name="price" placeholder="" required/>
								</div>

								<div className="col-sm-6">
									<label htmlFor="image" className="form-label">Zdjęcie</label>
									<input type="text" className="form-control" name="image" placeholder="" required/>
								</div>

								<div className="form-check">
									<input type="checkbox" className="form-check-input" name="isListed"/>
									<label className="form-check-label" htmlFor="isListed">Dostępność</label>
								</div>

							</form>
							<hr className="mt-5" />

							<div className="row mt-5">
								<div className="col text-center">
									{
										editting != null
											? <button className="w-20 btn btn-warning btn-lg mr-3" onClick={eventEdit}>Edycja</button>
											: <button className="w-20 btn btn-success btn-lg mr-3" onClick={eventInsert}>Dodaj</button>
									}
									<button className="w-20 btn btn-danger btn-lg">Anuluj</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				{ editting != null && renderListOfIngridients() }
			</div>
		</>
	)
}
