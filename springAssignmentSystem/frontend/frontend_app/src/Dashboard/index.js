// https://youtu.be/SbzKc3lwVQw?si=jKjSbEO7OH2UshFi&t=495
// type "rsc", and should auto generate basic code

import React, { useEffect, useState } from "react";
import { useLocalState } from "../util/useLocalStorage";

const Dashboard = () => {
  const [getJwt, setJwt] = useLocalState("", "jwt"); // getter, setter
  const [assignments, setAssignment] = useState(null);

  useEffect(() => {
    fetch("/api/assignments/", {
      headers: {
        "Content-type": "application/json",
        Authentication: `Bearer ${getJwt}`,
      },
      method: "GET",
    })
      .then((response) => {
        if (response.status === 200) return response.json();
      })
      .then((assignmentsData) => {
        setAssignment(assignmentsData);
      });
  }, []);

  // https://youtu.be/4l1X3PQ_NWw?si=l9tAhN7PA4i-9vBA&t=126

  function createAssignment() {
    console.log("createAssignment ...");
    fetch("/api/assignments/", {
      headers: {
        "Content-type": "application/json",
        Authentication: `Bearer ${getJwt}`,
      },
      method: "POST",
      body: JSON.stringify({ user: "admin-user" }),
    })
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        }
      })
      .then((assignment) => {
        console.log("BE response = " + assignment);
        // redirect to new assignment URL
        window.location.href = `/assignments/${assignment.id}`;
      });
  }

  return (
    <div style={{ margin: "2em" }}>
      <h1>Dashboard !!</h1>
      {/* <div>JWT value is {getJwt}</div> */}
      {assignments ? assignments.map((assignment) => 
      <div> 
          Assignment ID : {assignment.id}, user name : {assignment.user.username}
      </div>) : <></>}
      <button onClick={() => createAssignment()}>Submit New Assignment</button>
    </div>
  );
};

// https://youtu.be/SbzKc3lwVQw?si=VRm29BYVejRSMQgL&t=740
// when export "default", we don't need {} when import (check App.js)
export default Dashboard;
