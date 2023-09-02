//import logo from './logo.svg';
import { useEffect, useState } from "react";
import "./App.css";
import { useLocalState } from "./util/useLocalStorage";
import { Route, Routes } from "react-router-dom";

// use-defined view
import Dashboard from "./Dashboard";
import Homepage from "./Homepage";
import Login from "./Login";
import PrivateRoute from "./PrivateRoute";

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
  //const [getJwt, setJwt] = useState(""); // getter, setter
  // https://youtu.be/aIr288-3AFE?si=utEgteuvcG6m0Aan&t=792
  const [getJwt, setJwt] = useLocalState("", "jwt"); // getter, setter

  /**
   *  https://youtu.be/pupnAIRpbKo?si=ne6sMKKJR_cR3gds&t=391
   *
   *  dependency is an array
   *
   *  useEffect( () => function}, dependency)
   *
   */
  // useEffect(() => {
  //   if (!getJwt) {
  //     const reqBody = {
  //       username: "admin",
  //       password: "123",
  //     };

  //     /**
  //      * Fetch : call backend/other api, then go to next line of code, will not freeze and only operate once receive result
  //      * https://youtu.be/N1QStjH1rVI?si=ozUe3M3tx6z3i-T1&t=1256
  //      */
  //     fetch("api/auth/login", {
  //       headers: {
  //         "Content-Type": "application/json",
  //       },
  //       method: "post",
  //       body: JSON.stringify(reqBody),
  //     })
  //       // .then((response) => response.json)
  //       // .then((data) => console.log(data))
  //       .then((response) => Promise.all([response.json(), response.headers]))
  //       .then(([body, headers]) => {
  //         // headers.forEach((element) => {
  //         //   console.log(element);
  //         // });

  //         // jwt : json web token
  //         //const jwt = headers.get("authorization")
  //         setJwt(headers.get("authorization"));
  //         //console.log(jwt)
  //         console.log(getJwt);
  //         console.log(body);
  //       });
  //   }
  // }, [getJwt]);

  // useEffect(() => {
  //   console.log(`>>> JWT is ${getJwt}`);
  // }, [getJwt]);

  // return view
  return (
    /** Manage all App paths */
    <Routes>
      
      <Route path="/dashboard" element={<Dashboard />}></Route>
      {/* redirect to login page if access dashboard without login */}
      {/* <Route path="/dashboard" element={
        <PrivateRoute>
          <Dashboard />
        </PrivateRoute>
      }></Route> */}

      <Route path="/" element={<Homepage />}></Route>
      <Route path="/login" element={<Login />}></Route>
    </Routes>
  );
}

export default App;
