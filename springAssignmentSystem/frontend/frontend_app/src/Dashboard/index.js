// https://youtu.be/SbzKc3lwVQw?si=jKjSbEO7OH2UshFi&t=495
// type "rsc", and sould auto generate basic code

import React from "react";
import { useLocalState } from "../util/useLocalStorage";

const Dashboard = () => {
  const [getJwt, setJwt] = useLocalState("", "jwt"); // getter, setter

  // https://youtu.be/4l1X3PQ_NWw?si=l9tAhN7PA4i-9vBA&t=126

  function createAssignment() {
    console.log("createAssignment ...");
    fetch("/assignments/", {
      headers: {
        "Content-type": "application/json",
        Authentication: `Bearer ${getJwt}`,
      },
      method: "POST",
    })
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        }
      })
      .then((data) => {
        console.log("BE response = " + data);
      });
  }

  return (
    <div style={{ margin: "2em" }}>
      <h1>Dashboard !!</h1>
      {/* <div>JWT value is {getJwt}</div> */}
      <button onClick={() => createAssignment()}>Submit New Assignment</button>
    </div>
  );
};

// https://youtu.be/SbzKc3lwVQw?si=VRm29BYVejRSMQgL&t=740
// when export "default", we don't need {} when import (check App.js)
export default Dashboard;
