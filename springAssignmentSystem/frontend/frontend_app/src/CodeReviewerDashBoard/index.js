// https://youtu.be/SbzKc3lwVQw?si=jKjSbEO7OH2UshFi&t=495
// type "rsc", and should auto generate basic code

import React, { useEffect, useState } from "react";
import { useLocalState } from "../util/useLocalStorage";
import { Link } from "react-router-dom";
import ajax from "../Services/fetchService";
// import Card from "react-bootstrap/Card";
import { Button, Card, Col, Row, Badge, Container } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import StatusBadge from "../StatusBadge";

const CodeReviewerDashboard = () => {
  const navigate = useNavigate();
  const [getJwt, setJwt] = useLocalState("", "jwt"); // getter, setter
  const [assignments, setAssignment] = useState(null);

  // https://youtu.be/utXVb3R1yuE?si=4Hv8qBS_gQ8mJVfF&t=408
  function claimAssignment(assignment) {
    //const decodedJwt = jwt_decode(getJwt);
    const user = {
      id: null,
      username: "my_code_reivewer",
      authorities: ["ROLE_CODE_REVIEWER"],
    };
    assignment.codeReviewer = user;
    // TODO : fix this hardcode status
    assignment.status = "In Review";
    console.log("claim assignment : " + JSON.stringify(assignment));
    ajax(`/api/assignments/${assignment.id}`, "PUT", getJwt, assignment).then(
      (updatedAssignment) => {
        // TODO : update view for assignment that changed
        // copy an array syntax [...oldArray]
        // https://youtu.be/utXVb3R1yuE?si=MkVbNb5R8imKjtkw&t=1947
        const assignmentsCopied = [...assignments];
        const i = assignmentsCopied.findIndex((x) => x.id === assignment.id);
        assignmentsCopied[i] = updatedAssignment;
        setAssignment(assignmentsCopied);
      }
    );
  }

  useEffect(() => {
    ajax("/api/assignments/to_review", "GET", getJwt).then(
      (assignmentsData) => {
        setAssignment(assignmentsData);
      }
    );
  }, []);

  function createAssignment() {
    console.log("createAssignment ...");
    ajax("/api/assignments/", "POST", getJwt).then((assignment) => {
      console.log("BE response = " + assignment);
      // redirect to new assignment URL
      window.location.href = `/assignments/${assignment.id}`;
    });
  }

  return (
    <Container>
      <h1>CodeReviewerDashboard !!</h1>
      <Link to="http://localhost:3000/dashboard">Go to Dashboard</Link>
      <Row>
        <Col>
          <div
            className="d-flex justify-content-end"
            style={{ cursor: "pointer" }}
            onClick={() => {
              setJwt(null);
              window.location.href = "/login";
            }}
          >
            Logout
          </div>
        </Col>
      </Row>

      {/**
       *
       *  In Review
       */}
      {/** check .assignment-wrapper setting in App.css */}
      <div className="assignment-wrapper in-review">
        <div className="assignment-wrapper-title h3 mt-5">In Review</div>
        {assignments &&
          assignments
            .filter((assignment) => assignment.status === "In Review")
            .map((assignment) => (
              // <Col>
              <Card
                key={assignment.id}
                style={{ width: "18rem", height: "20rem" }}
              >
                <Card.Body className="d-flex flex-column justify-content-around">
                  <Card.Title>Assignment #{assignment.number}</Card.Title>

                  <div className="d-flex align-items-start">
                    <StatusBadge text={assignment.status} />
                  </div>

                  <Card.Text style={{ marginTop: "1em" }}>
                    <b>GitHub URL</b>: {assignment.githubUrl}
                    <br />
                    <b>Branch</b>: {assignment.branch}
                  </Card.Text>

                  {assignment && assignment.status === "Completed" ? (
                    <>
                      <Button
                        onClick={() => {
                          window.open(assignment.codeReviewVideoUrl);
                        }}
                        className="mb-4"
                      >
                        Watch Review
                      </Button>
                      <Button
                        variant="secondary"
                        onClick={() => {
                          navigate(`/assignments/${assignment.id}`);
                        }}
                      >
                        View
                      </Button>
                    </>
                  ) : (
                    <Button
                      variant="secondary"
                      onClick={() => {
                        //navigate(`/assignments/${assignment.id}`);
                        claimAssignment(assignment);
                      }}
                    >
                      Claim
                    </Button>
                  )}
                </Card.Body>
              </Card>
              // </Col>
            ))}
        <Card style={{ width: "18rem", height: "20rem" }}>
          <Card.Body className="d-flex flex-column justify-content-around"></Card.Body>
        </Card>
      </div>

      {/**
       *
       *  Awaiting Review
       */}

      {/** css style from App.css */}
      <div className="assignment-wrapper submitted">
        <div className="assignment-wrapper-title h3 mt-5">Awaiting Review</div>
        {assignments &&
          assignments
            .filter((assignment) => assignment.status === "Submitted")
            .map((assignment) => (
              // <Col>
              <Card
                key={assignment.id}
                style={{ width: "18rem", height: "20rem" }}
              >
                <Card.Body className="d-flex flex-column justify-content-around">
                  <Card.Title>Assignment #{assignment.number}</Card.Title>

                  <div className="d-flex align-items-start">
                    <StatusBadge text={assignment.status} />
                  </div>

                  <Card.Text style={{ marginTop: "1em" }}>
                    <b>GitHub URL</b>: {assignment.githubUrl}
                    <br />
                    <b>Branch</b>: {assignment.branch}
                  </Card.Text>

                  {assignment && assignment.status === "Completed" ? (
                    <>
                      <Button
                        onClick={() => {
                          window.open(assignment.codeReviewVideoUrl);
                        }}
                        className="mb-4"
                      >
                        Watch Review
                      </Button>
                      <Button
                        variant="secondary"
                        onClick={() => {
                          navigate(`/assignments/${assignment.id}`);
                        }}
                      >
                        View
                      </Button>
                    </>
                  ) : (
                    <Button
                      variant="secondary"
                      onClick={() => {
                        //navigate(`/assignments/${assignment.id}`);
                        claimAssignment(assignment);
                      }}
                    >
                      Claim
                    </Button>
                  )}
                </Card.Body>
              </Card>
              // </Col>
            ))}
        <Card style={{ width: "18rem", height: "20rem" }}>
          <Card.Body className="d-flex flex-column justify-content-around"></Card.Body>
        </Card>
      </div>

      {/**
       *
       *  Needs Update
       */}

      <div className="assignment-wrapper needs-update">
        <div className="assignment-wrapper-title h3 mt-5">Needs Update</div>
        {assignments &&
          assignments
            .filter((assignment) => assignment.status === "Needs Update")
            .map((assignment) => (
              // <Col>
              <Card
                key={assignment.id}
                style={{ width: "18rem", height: "20rem" }}
              >
                <Card.Body className="d-flex flex-column justify-content-around">
                  <Card.Title>Assignment #{assignment.number}</Card.Title>

                  <div className="d-flex align-items-start">
                    <StatusBadge text={assignment.status} />
                  </div>

                  <Card.Text style={{ marginTop: "1em" }}>
                    <b>GitHub URL</b>: {assignment.githubUrl}
                    <br />
                    <b>Branch</b>: {assignment.branch}
                  </Card.Text>

                  {assignment && assignment.status === "Completed" ? (
                    <>
                      <Button
                        onClick={() => {
                          window.open(assignment.codeReviewVideoUrl);
                        }}
                        className="mb-4"
                      >
                        Watch Review
                      </Button>
                      <Button
                        variant="secondary"
                        onClick={() => {
                          navigate(`/assignments/${assignment.id}`);
                        }}
                      >
                        View
                      </Button>
                    </>
                  ) : (
                    <Button
                      variant="secondary"
                      onClick={() => {
                        //navigate(`/assignments/${assignment.id}`);
                        claimAssignment(assignment);
                      }}
                    >
                      Claim
                    </Button>
                  )}
                </Card.Body>
              </Card>
              // </Col>
            ))}
        <Card style={{ width: "18rem", height: "20rem" }}>
          <Card.Body className="d-flex flex-column justify-content-around"></Card.Body>
        </Card>
      </div>
    </Container>
  );
};

// https://youtu.be/SbzKc3lwVQw?si=VRm29BYVejRSMQgL&t=740
// when export "default", we don't need {} when import (check App.js)
export default CodeReviewerDashboard;
