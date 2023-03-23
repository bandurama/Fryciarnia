import './App.css';
import { Routes, Route } from 'react-router-dom';


function Main () {

  function requester (e) {
    fetch('http://bandurama.ddns.net/test')
      .then((response) => response.json())
      .then(resp => { console.log(resp)})
  }

  return (
    <div>
      Main Page
      <a href="/other">Other?</a>
      <a href="#" onClick={requester}>Fetch</a>
    </div>
  )
}

function Other () {
  return (
    <>
      Other Page
      <a href="/">Main?</a>
    </>
  )
}

function App() {
  return (
    <div className="App">
      <Routes>
        <Route exact path="/" Component={Main} />
        <Route path="/other" Component={Other} />
      </Routes>
    </div>
  );
}

export default App;
