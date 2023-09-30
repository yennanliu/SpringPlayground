import React, { useEffect, useInsertionEffect, useState } from "react";
import { useLocalState } from "../util/useLocalStorage";
import ajax from "../Services/fetchService";
import { Link } from "react-router-dom";
import { Badge, Button, Container } from "react-bootstrap";

import Col from "react-bootstrap/Col";
import Form from "react-bootstrap/Form";
import Row from "react-bootstrap/Row";

import ButtonGroup from "react-bootstrap/ButtonGroup";
import Dropdown from "react-bootstrap/Dropdown";
import DropdownButton from "react-bootstrap/DropdownButton";
import userEvent from "@testing-library/user-event";

const AssignmentView = () => {
  const assignmentId = window.location.href.split("/assignments/")[1];
  // https://youtu.be/zQiKOu8iGco?si=zRyGka60wy07xUrV&t=1934
  // init a new assignment
  const [assignment, setAssignment] = useState({
    branch: "",
    githubUrl: "",
    assignmentNum: null,
  });
  const [getJwt, setJwt] = useLocalState("", "jwt"); // getter, setter
  const [assignmentEnums, setAssignmentEnums] = useState([]);
  const [selectedAssignment, setSelectedAssignment] = useState(null);

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

      // https://youtu.be/K-ywr1I1mr0?si=nFWaN1mbJ8cKub_r&t=845
      .then((assignmentsResponse) => {
        let assignmentsData = assignmentsResponse.assignment;

        console.log("BE response assignmentsData = " + assignmentsData);
        console.log("BE response branch = " + assignmentsData.branch);
        console.log("BE response githubUrl = " + assignmentsData.githubUrl);

        if (assignmentsData.branch === null) {
          assignmentsData.branch = "";
        }
        if (assignmentsData.githubUrl === null) {
          assignmentsData.githubUrl = "";
        }

        setAssignment(assignmentsData);
        setAssignmentEnums(assignmentsResponse.assignmentEnums);
      });
  }, []);

  useEffect(() => {
    console.log(">>> assignmentEnums " + assignmentEnums);
  }, [assignmentEnums]);

  return (
    <Container className="mt-5">
      <Row className="d-flex">
        <Col className="d-flex align-items-center">
          {selectedAssignment ? (
            <h1>Assignment ID : {selectedAssignment}</h1>
          ) : (<></> )}
        </Col>
        <Col>
          <Badge pill bg="info" style={{ fontSize: "1.3em" }}>
            {assignment.status}
          </Badge>
        </Col>
      </Row>
      <p></p>
      <Link to="http://localhost:3000/dashboard">Back to Dashboard</Link>
      {/** only show below when assignment is not null */}
      {assignment ? (
        <>
          {/** Dropdown:
           *  https://youtu.be/MGtkDvpD6rs?si=DV0FkrAYixoeN9fd&t=1569
           *  https://react-bootstrap.netlify.app/docs/components/dropdowns/
           */}
          <Form.Group as={Row} className="my-4" controlId="assignmentName">
            <Form.Label column sm="2">
              Assignment Number
            </Form.Label>
            <Col sm="10">
              <DropdownButton
                as={ButtonGroup}
                variant="info"
                title={selectedAssignment ?  `Assignment ${selectedAssignment}` : "Select an Assignment"}
                onSelect={(selectedAssignment) => {
                  setSelectedAssignment(selectedAssignment);
                  updateAssignment("assignmentNum", selectedAssignment);
                }}
              >
                {/* {["1", "2", "3", "4", "5"].map((assignmentNum) => (
                  <Dropdown.Item eventKey={assignmentNum}>
                    {assignmentNum}
                  </Dropdown.Item>
                ))} */}
                {/* https://youtu.be/K-ywr1I1mr0?si=vELe7cwexA5ME29P&t=1023 */}
                {assignmentEnums.map((assignmentEnum) => (
                  <Dropdown.Item
                    key={assignmentEnum.assignmentNum}
                    eventKey={assignmentEnum.assignmentNum}
                  >
                    {assignmentEnum.assignmentNum}
                  </Dropdown.Item>
                ))}

                {/* <Dropdown.Item eventKey="1">Action</Dropdown.Item>
                <Dropdown.Item eventKey="2">Another action</Dropdown.Item>
                <Dropdown.Item eventKey="3" active>
                  Active Item
                </Dropdown.Item> */}
                {/* <Dropdown.Divider />
                <Dropdown.Item eventKey="4">Separated link</Dropdown.Item> */}
              </DropdownButton>
            </Col>
          </Form.Group>

          <Form.Group as={Row} className="my-4" controlId="githubUrl">
            <Form.Label column sm="2">
              GitHub URL
            </Form.Label>
            <Col sm="10">
              <Form.Control
                onChange={(event) =>
                  updateAssignment("githubUrl", event.target.value)
                }
                type="url"
                value={assignment.githubUrl}
                placeholder="http//github.com"
              />
            </Col>
          </Form.Group>

          <Form.Group as={Row} className="mb-3" controlId="branch">
            <Form.Label column sm="2">
              Branch
            </Form.Label>
            <Col sm="10">
              <Form.Control
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
