import React, { useEffect, useState } from "react";
import { useLocalState } from "../util/useLocalStorage";

const AssignmentView = () => {
  const assignmentId = window.location.href.split("/assignments/")[1];
  // https://youtu.be/zQiKOu8iGco?si=zRyGka60wy07xUrV&t=1934
  // init a new assignment
  const [assignment, setAssignment] = useState({
    branch: "",
    githubUrl: "",
  });
  const [getJwt, setJwt] = useLocalState("", "jwt"); // getter, setter

  // https://youtu.be/zQiKOu8iGco?si=w4oK-Ap9YBPTTEWl&t=2007
  function updateAssignment(prop, value) {
    //assignment[prop] = value;
    const newAssignment = { ...assignment };
    newAssignment[prop] = value;
    setAssignment(newAssignment);
    console.log(assignment);
  }

  function saveAssignment() {
    // call BE API save assignment
    fetch(`/api/assignments/${assignmentId}`, {
      headers: {
        "Content-type": "application/json",
        Authentication: `Bearer ${getJwt}`,
      },
      method: "PUT",
      body: JSON.stringify(assignment),
    })
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        }
      })
      // if http code == 200, save response data (assignmentData) to FE var as well
      .then((assignmentData) => {
        setAssignment(assignmentData);
      });
  }

  useEffect(() => {
    fetch(`/api/assignments/${assignmentId}`, {
      headers: {
        "Content-type": "application/json",
        Authentication: `Bearer ${getJwt}`,
      },
      method: "GET",
    })
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        }
      })
      .then((assignmentsData) => {
        console.log("BE response = " + assignmentsData);
        setAssignment(assignmentsData);
      });
  }, []);

  return (
    <div>
      <h1>Assignment ID : {assignmentId}</h1>
      {/** only show below when assignment is not null */}
      {assignment ? (
        <>
          <h2>Status : {assignment.status}</h2>
          <h3>
            GitHub URL :{" "}
            <input
              type="url"
              id="githubUrl"
              // onChange={(event) => setGitHubUrl(event.target.value)}
              onChange={(event) =>
                updateAssignment("githubUrl", event.target.value)
              }
              value={assignment.githubUrl}
            ></input>
          </h3>
          <h3>
            Branch :{" "}
            <input
              type="text"
              id="branch"
              onChange={(event) =>
                updateAssignment("branch", event.target.value)
              }
              value={assignment.branch}
            ></input>
          </h3>
          <button onClick={() => saveAssignment()}>Submit Assignment</button>
        </>
      ) : (
        <></>
      )}
    </div>
  );
};

export default AssignmentView;
