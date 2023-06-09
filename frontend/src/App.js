import {Route, Routes} from "react-router-dom";
import Main from "./pages/Main";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import GAuth from "./pages/GAuth";
import Login from "./pages/Login";
import Profile from "./pages/Profile/Profile";
import Tickets from "./pages/Tickets";
import Menu from "./pages/Menu";
import Meal from "./pages/Meal";
import Admin from "./pages/Admin/Admin";
import Manager from "./pages/Manager/Manager";
import Order from "./pages/Order/Order";
import Ticket from "./pages/Ticket";
import Payment from "./pages/Payment/Payment";
import Kitchen from "./pages/Kitchen/Kitchen";
import ErrorPage from "./pages/ErrorPage";
import History from "./pages/History";
import ProfileHistory from "./pages/Profile/ProfileHistory";

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
				<Route path="/menu/:holding" Component={Menu} />
				<Route path="/meal/:holding/:meal" Component={Meal} />
				<Route path="/admin/:option" Component={Admin} />
				<Route path="/manager/:option" Component={Manager} />
				<Route path="/profile/history" Component={ProfileHistory} />
				<Route path="/order/:holding" Component={Order} />
				<Route path="/payment/:order" Component={Payment} />
				<Route path="/error/:code" Component={ErrorPage} />
				<Route path="/ticket/:order" Component={Ticket} />
				<Route path="/kitchen" Component={Kitchen} />
				<Route path="/history" Component={History} />
			</Routes>
		</div>
	);
}

export default App;

/**
 * PayPal Debugier: >Y6)E$ac
 */