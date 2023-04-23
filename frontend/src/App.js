import {Route, Routes} from "react-router-dom";
import Main from "./pages/Main";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import GAuth from "./pages/GAuth";
import Login from "./pages/Login";
import Profile from "./pages/Profile/Profile";
import History from "./pages/Profile/History";
import Tickets from "./pages/Tickets";
import Menu from "./pages/Menu";
import Meal from "./pages/Meal";
import Admin from "./pages/Admin/Admin";

function App() {
	return (
		<div className="App">
			<Routes>
				<Route exact path="/" Component={Main} />
				<Route  path="/register" Component={Register} />
				<Route path="/login" Component={Login} />
				<Route path="/dashboard" Component={Dashboard} />
				<Route path="/gauth" Component={GAuth} />
				<Route path="/profile" Component={Profile} />
				<Route path="/tickets" Component={Tickets} />
				<Route path="/menu" Component={Menu} />
				<Route path="/menu/:meal" Component={Meal} />
				<Route path="/admin/:option" Component={Admin} />
				<Route path="/profile/history" Component={History} />
			</Routes>
		</div>
	);
}

export default App;
