import React, { useEffect, useInsertionEffect, useState, useRef } from "react";
import { useLocalState } from "../util/useLocalStorage";
import ajax from "../Services/fetchService";
import { Link, json } from "react-router-dom";
import { Badge, Button, Container } from "react-bootstrap";
import Col from "react-bootstrap/Col";
import Form from "react-bootstrap/Form";
import Row from "react-bootstrap/Row";
import { useNavigate } from "react-router-dom";
import StatusBadge from "../StatusBadge";

const CodeReviewerAssignmentView = () => {
  const navigate = useNavigate();

  console.log(
    ">>> (CodeReviewerAssignmentView) window.location.href = " +
      window.location.href
  );
  const assignmentId = window.location.href.split("/code_review/")[1];
  // https://youtu.be/zQiKOu8iGco?si=zRyGka60wy07xUrV&t=1934
  // init a new assignment
  const [assignment, setAssignment] = useState({
    branch: "",
    githubUrl: "",
    assignmentNum: null,
    status: null,
  });
  const [getJwt, setJwt] = useLocalState("", "jwt"); // getter, setter
  const [assignmentEnums, setAssignmentEnums] = useState([]);
  const [selectedAssignment, setSelectedAssignment] = useState(null);
  // https://youtu.be/5BQfPkykC5Q?si=BtHXvuPAqqkq1eZR&t=1330
  const [assignmentStatuses, setAssignmentStatuses] = useState([]);

  // https://youtu.be/2XRQzR4y2yM?si=p8QcytO5aBC6ufBj&t=459
  // setup current status
  const previousAssignmentValue = useRef(assignment);

  // https://youtu.be/zQiKOu8iGco?si=w4oK-Ap9YBPTTEWl&t=2007
  function updateAssignment(prop, value) {
    //assignment[prop] = value;
    const newAssignment = { ...assignment };
    newAssignment[prop] = value;
    setAssignment(newAssignment);
    console.log(assignment);
  }

  function saveAssignment(status) {
    // call BE API save assignment

    // https://youtu.be/2XRQzR4y2yM?si=aiNeLhS3SXsU18HE&t=833
    // means when submit an assignment at the first time
    // https://youtu.be/SOyfQCsOvO4?si=Eu1z9CWZO0c-e3Zq&t=816

    console.log(
      "(saveAssignment) status =  " +
        status +
        ",  assignment = " +
        JSON.stringify(assignment)
    );

    if (status && assignment.status !== status) {
      updateAssignment("status", status);
    } else {
      // https://youtu.be/2XRQzR4y2yM?si=RDbtHpdUnVs8j7JE&t=1109
      persist();
    }
  }

  function persist() {
    ajax(`/api/assignments/${assignmentId}`, "PUT", getJwt, assignment).then(
      (assignmentData) => {
        setAssignment(assignmentData);
      }
    );
  }

  // https://youtu.be/2XRQzR4y2yM?si=zMMSmNUnlpz-Czg3&t=601
  // https://github.com/tp02ga/AssignmentSubmissionApp/blob/master/front-end/src/AssignmentView/index.js#L65C3-L65C46
  // will update below function, based on assignment value
  useEffect(() => {
    // / https://youtu.be/2XRQzR4y2yM?si=aiNeLhS3SXsU18HE&t=833
    // if previous status != new status, we need to save it to BE (DB) right away
    if (previousAssignmentValue.current.status !== assignment.status) {
      console.log("save new status to DB");
      persist();
    }
    console.log(
      "previous value of assignment = " +
        JSON.stringify(previousAssignmentValue)
    );
    console.log("new value of assignment = " + JSON.stringify(assignment));
    previousAssignmentValue.current = assignment;
  }, [assignment]);

  useEffect(() => {
    // V2
    // https://youtu.be/w6YUDqKiT8I?si=pXIQhoWmGDLjQgtI&t=803
    ajax(`/api/assignments/${assignmentId}`, "GET", getJwt)
      // https://youtu.be/K-ywr1I1mr0?si=nFWaN1mbJ8cKub_r&t=845
      .then((assignmentsResponse) => {
        let assignmentsData = assignmentsResponse.assignment;

        // console.log("BE response assignmentsData = " + assignmentsData);
        // console.log("BE response branch = " + assignmentsData.branch);
        // console.log("BE response githubUrl = " + assignmentsData.githubUrl);

        if (assignmentsData.branch === null) {
          assignmentsData.branch = "";
        }
        if (assignmentsData.githubUrl === null) {
          assignmentsData.githubUrl = "";
        }

        setAssignment(assignmentsData);
        setAssignmentEnums(assignmentsResponse.assignmentEnums);
        setAssignmentStatuses(assignmentsResponse.statusEnums);
        console.log(
          ">>> assignmentsResponse.statusEnums = " +
            JSON.stringify(assignmentsResponse.statusEnums)
        );
      });
  }, []);

  useEffect(() => {
    console.log(">>> assignmentEnums " + JSON.stringify(assignmentEnums));
  }, [assignmentEnums]);

  return (
    <Container className="mt-5">
      <Row className="d-flex">
        <Col className="d-flex align-items-center">
          {assignment.number ? (
            <h1>Assignment ID : {assignment.number}</h1>
          ) : (
            <></>
          )}
        </Col>
        <Col>
          {/* <Badge pill bg="info" style={{ fontSize: "1.3em" }}>
            {assignment.status}
          </Badge> */}
          <div className="d-flex align-items-start">
            <StatusBadge text={assignment.status} />
          </div>
        </Col>
      </Row>
      <p></p>
      <Link to="http://localhost:3000/dashboard">Back to Dashboard</Link>
      {/** only show below when assignment is not null */}
      {assignment ? (
        <>
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
                readOnly
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
                readOnly
                value={assignment.branch}
                placeholder="master"
              />
            </Col>
          </Form.Group>

          {/**https://youtu.be/SOyfQCsOvO4?si=S6KBoGBnYrkOLi8G&t=560 */}
          <Form.Group as={Row} className="mb-3" controlId="branch">
            <Form.Label column sm="2">
              Video Review URL
            </Form.Label>
            <Col sm="10">
              <Form.Control
                onChange={(event) =>
                  updateAssignment("codeReviewVideoUrl", event.target.value)
                }
                type="text"
                value={assignment.codeReviewVideoUrl}
                placeholder="https/my-review-video-url"
              />
            </Col>
          </Form.Group>

          <Button onClick={() => saveAssignment("completed")}>
            Complete Review
          </Button>

          <Button
            variant="secondary"
            onClick={() =>
              //window.location.href = "/dashboard"
              navigate("/dashboard")
            }
          >
            Back
          </Button>

          <Button
            variant="danger"
            onClick={() => saveAssignment("Needs Update")}
          >
            Reject Assignment
          </Button>
        </>
      ) : (
        <></>
      )}
    </Container>
  );
};

export default CodeReviewerAssignmentView;
