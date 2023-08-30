//import logo from './logo.svg';
import { useEffect, useState } from "react";
import "./App.css";

/**
 *
 * MAIN APP (entry point)
 *
 *  https://github.com/tp02ga/AssignmentSubmissionApp/blob/master/front-end/src/App.js
 */

function App() {
  console.log("APP start!");

  /**
   *  https://youtu.be/pupnAIRpbKo?si=O-NSykY6ChvYenb7&t=629
   *
   *  useState can save state (val) in the scope of function
   *
   */
  const [getJwt, setJwt] = useState(""); // getter, setter

  /**
   *  https://youtu.be/pupnAIRpbKo?si=ne6sMKKJR_cR3gds&t=391
   *
   *  dependency is an array
   *
   *  useEffect( () => function}, dependency)
   *
   */
  useEffect(() => {
    const reqBody = {
      username: "admin",
      password: "123",
    };

    /**
     * Fetch : call backend/other api, then go to next line of code, will not freeze and only operate once receive result
     * https://youtu.be/N1QStjH1rVI?si=ozUe3M3tx6z3i-T1&t=1256
     */
    fetch("api/auth/login", {
      headers: {
        "Content-Type": "application/json",
      },
      method: "post",
      body: JSON.stringify(reqBody),
    })
      // .then((response) => response.json)
      // .then((data) => console.log(data))
      .then((response) => Promise.all([response.json(), response.headers]))
      .then(([body, headers]) => {
        // headers.forEach((element) => {
        //   console.log(element);
        // });

        // jwt : json web token
        //const jwt = headers.get("authorization")
        setJwt(headers.get("authorization"));
        //console.log(jwt)
        console.log(getJwt);
        console.log(body);
      });
  }, [getJwt]);

  useEffect(() => {
    console.log(`>>> JWT is ${getJwt}`);
  }, [getJwt]);

  return (
    <div className="App">
      <h1>HELLO WORLD</h1>
      <div>JWT value is {getJwt}</div>
    </div>
  );
}

export default App;
