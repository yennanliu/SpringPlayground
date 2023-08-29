//import logo from './logo.svg';
import './App.css';

/** MAIN APP (entry point) */

function App() {

  console.log("APP start!")

  const reqBody = {
    "username": "admin",
    "password": "123"
}

  /** 
   * Fetch : call backend/other api, then go to next line of code, will not freeze and only operate once receive result
   * https://youtu.be/N1QStjH1rVI?si=ozUe3M3tx6z3i-T1&t=1256 
   */
  fetch("api/auth/login", {
    headers: {
      "Content-Type": "application/json",
    },
    method: "post",
    body: JSON.stringify(reqBody)
  })
    // .then((response) => response.json)
    // .then((data) => console.log(data))
    .then((response) => Promise.all([response.json(), response.headers]))
    .then(([data, headers]) => {

      headers.forEach((element) => {
        console.log(element)
      })

    })

  return (
    <div className="App">
      <h1>HELLO WORLD</h1>
    </div>
  );
}

export default App;
