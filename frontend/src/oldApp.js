import './App.css';
import { Routes, Route } from 'react-router-dom';
import {useEffect} from "react";


function Main () {

  function requester (e) {
    fetch(
    'http://bandurama.ddns.net:2023/api/webuser/register', {
      method: 'POST',
      body: JSON.stringify({mail: 'maciek-aciek.pl.32323', password: "Kupa", type: "Web", isGoogleAccount: false}),
      credentials: 'include'
    })
      .then((response) => response.json())
      .then(resp => { console.log(resp)})
  }

  return (
    <div>
      <a href="https://accounts.google.com/o/oauth2/v2/auth?client_id=268913103926-nvg6lersjlvopnouhesjcvts6ttrj14v.apps.googleusercontent.com&redirect_uri=http://bandurama.ddns.net/gauth&response_type=token&scope=https://www.googleapis.com/auth/userinfo.profile&include_granted_scopes=true&state=pass-through%20value">Goówno OaAuth 2.0</a>
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

function  GAuth () {

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
      .then(resp => { console.log(resp)})

  }, []);

  return (
    <>
    </>
  )
}

function App() {
  return (
    <div className="App">
      <Routes>
        <Route exact path="/" Component={Main} />
        <Route path="/other" Component={Other} />
        <Route path="/gauth" Component={GAuth} />
      </Routes>
    </div>
  );
}

export default App;
