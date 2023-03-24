import {Route, Routes} from "react-router-dom";
import Main from "./components/Main";
import Register from "./components/Register";
import Dashboard from "./components/Dashboard";
import GAuth from "./components/GAuth";
import Login from "./components/Login";




function App() {
	return (
		<div className="App">
			<Routes>
				<Route exact path="/" Component={Main} />
				<Route  path="/register" Component={Register} />
				<Route path="/login" Component={Login} />
				<Route path="/dashboard" Component={Dashboard} />
				{/*<Route path="/menu" Component={Menu} />*/}
				<Route path="/gauth" Component={GAuth} />
			</Routes>
		</div>
	);
}

export default App;
