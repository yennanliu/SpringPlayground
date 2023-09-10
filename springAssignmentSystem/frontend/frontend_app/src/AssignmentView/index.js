import React, { useEffect, useState } from "react";
import { useLocalState } from "../util/useLocalStorage";
import ajax from "../Services/fetchService";
import { Link } from "react-router-dom";
import { Button, Container } from "react-bootstrap";

import Col from "react-bootstrap/Col";
import Form from "react-bootstrap/Form";
import Row from "react-bootstrap/Row";

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

    // V2 : use custom method
    // https://youtu.be/w6YUDqKiT8I?si=pXIQhoWmGDLjQgtI&t=803
    ajax(`/api/assignments/${assignmentId}`, "PUT", getJwt, assignment)
      // V1 : use raw fetch
      // fetch(`/api/assignments/${assignmentId}`, {
      //   headers: {
      //     "Content-type": "application/json",
      //     Authentication: `Bearer ${getJwt}`,
      //   },
      //   method: "PUT",
      //   body: JSON.stringify(assignment),
      // })
      //   .then((response) => {
      //     if (response.status === 200) {
      //       return response.json();
      //     }
      //   })
      // if http code == 200, save response data (assignmentData) to FE var as well
      .then((assignmentData) => {
        setAssignment(assignmentData);
      });
  }

  useEffect(() => {
    // V2
    // https://youtu.be/w6YUDqKiT8I?si=pXIQhoWmGDLjQgtI&t=803
    ajax(`/api/assignments/${assignmentId}`, "GET", getJwt)
      // V1
      // fetch(`/api/assignments/${assignmentId}`, {
      //   headers: {
      //     "Content-type": "application/json",
      //     Authentication: `Bearer ${getJwt}`,
      //   },
      //   method: "GET",
      // })
      //   .then((response) => {
      //     if (response.status === 200) {
      //       return response.json();
      //     }
      //   })
      .then((assignmentsData) => {
        console.log("BE response = " + assignmentsData);
        if (assignmentsData.branch === null) {
          assignmentsData.branch = "";
        }
        if (assignmentsData.githubUrl === null) {
          assignmentsData.githubUrl = "";
        }
        setAssignment(assignmentsData);
      });
  }, []);

  return (
    <Container>
      <h1>Assignment ID : {assignmentId}</h1>
      <Link to="http://localhost:3000/dashboard">Back to Dashboard</Link>
      {/** only show below when assignment is not null */}
      {assignment ? (
        <>
          <h4>Status : {assignment.status}</h4>

          <Form.Group as={Row} className="mb-3">
            <Form.Label column sm="2">
              GitHub URL
            </Form.Label>
            <Col sm="10">
              <Form.Control
                id="githubUrl"
                onChange={(event) =>
                  updateAssignment("githubUrl", event.target.value)
                }
                type="url"
                value={assignment.githubUrl}
                placeholder="http//github.com"
              />
            </Col>
          </Form.Group>

          <Form.Group as={Row} className="mb-3">
            <Form.Label column sm="2">
              Branch
            </Form.Label>
            <Col sm="10">
              <Form.Control
                id="branch"
                onChange={(event) =>
                  updateAssignment("branch", event.target.value)
                }
                type="text"
                value={assignment.branch}
                placeholder="master"
              />
            </Col>
          </Form.Group>

          <Button onClick={() => saveAssignment()}>Submit Assignment</Button>
        </>
      ) : (
        <></>
      )}
    </Container>
  );
};

export default AssignmentView;
