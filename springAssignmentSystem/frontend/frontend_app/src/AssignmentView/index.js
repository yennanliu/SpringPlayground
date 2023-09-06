import React, { useEffect, useState } from "react";
import { useLocalState } from "../util/useLocalStorage";

const AssignmentView = () => {
  const assignmentId = window.location.href.split("/assignments/")[1];
  const [assignment, setAssignment] = useState(null);
  const [getJwt, setJwt] = useLocalState("", "jwt"); // getter, setter

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
        </>
      ) : (
        <></>
      )}
    </div>
  );
};

export default AssignmentView;
