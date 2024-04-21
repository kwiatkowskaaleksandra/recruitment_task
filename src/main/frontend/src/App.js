import './App.css';
import {Route, BrowserRouter as Router, Routes} from "react-router-dom";
import Flights from "./components/Flights";
import 'bootstrap/dist/css/bootstrap.min.css';
import Passengers from "./components/Passengers";


function App() {
  return (
    <Router>
      <div>
        <Routes>
          <Route path={"/"} element={<Flights/>}/>
            <Route path="/passengers/:flightNumber/:idFlight" element={<Passengers />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
