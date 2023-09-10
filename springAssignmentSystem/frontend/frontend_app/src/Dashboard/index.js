// https://youtu.be/SbzKc3lwVQw?si=jKjSbEO7OH2UshFi&t=495
// type "rsc", and should auto generate basic code

import React, { useEffect, useState } from "react";
import { useLocalState } from "../util/useLocalStorage";
import { Link } from "react-router-dom";
import ajax from "../Services/fetchService";
// import Card from "react-bootstrap/Card";
import { Button, Card, Col, Row } from "react-bootstrap";

const Dashboard = () => {
  const [getJwt, setJwt] = useLocalState("", "jwt"); // getter, setter
  const [assignments, setAssignment] = useState(null);

  useEffect(() => {
    // V2
    ajax("/api/assignments/", "GET", getJwt)
      // V1
      // fetch("/api/assignments/", {
      //   headers: {
      //     "Content-type": "application/json",
      //     Authentication: `Bearer ${getJwt}`,
      //   },
      //   method: "GET",
      // })
      //   .then((response) => {
      //     if (response.status === 200) return response.json();
      //   })
      .then((assignmentsData) => {
        setAssignment(assignmentsData);
      });
  }, []);

  // https://youtu.be/4l1X3PQ_NWw?si=l9tAhN7PA4i-9vBA&t=126

  function createAssignment() {
    console.log("createAssignment ...");
    // V2
    ajax("/api/assignments/", "POST", getJwt)
      // V1
      // fetch("/api/assignments/", {
      //   headers: {
      //     "Content-type": "application/json",
      //     Authentication: `Bearer ${getJwt}`,
      //   },
      //   method: "POST",
      //   body: JSON.stringify({ user: "admin-user" }),
      // })
      // .then((response) => {
      //   if (response.status === 200) {
      //     return response.json();
      //   }
      // })
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
      {assignments ? (
        assignments.map((assignment) => (
          <div key={assignment.id}>
            {/* <Link></Link>
            <Link to={`/assignments/${assignment.id}`}>
              Assignment ID : {assignment.id}
            </Link> */}
            {/** React bootstrap cards :
             * https://youtu.be/3MqTKoM8_EQ?si=vf-cx_KrsSwbA56p&t=425
             * https://react-bootstrap.netlify.app/docs/components/cards
             */}

            <Card style={{ width: "18rem" }}>
              <Card.Body>
                <Card.Title>Card Title</Card.Title>
                <Card.Subtitle className="mb-2 text-muted">
                  Card Subtitle
                </Card.Subtitle>
                <Card.Text>
                  Some quick example text to build on the card title and make up
                  the bulk of the card's content.
                </Card.Text>
                <Card.Text>User name : {assignment.user.username}</Card.Text>
                <Card.Link href="#">Card Link</Card.Link>
                <Card.Link href="#">Another Link</Card.Link>
              </Card.Body>
            </Card>
            {/* , User name : {assignment.user.username} */}
          </div>
        ))
      ) : (
        <></>
      )}
      <button onClick={() => createAssignment()}>Submit New Assignment</button>
    </div>
  );
};

// https://youtu.be/SbzKc3lwVQw?si=VRm29BYVejRSMQgL&t=740
// when export "default", we don't need {} when import (check App.js)
export default Dashboard;
