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

const Dashboard = () => {
  const navigate = useNavigate();
  const [getJwt, setJwt] = useLocalState("", "jwt"); // getter, setter
  const [assignments, setAssignment] = useState(null);

  useEffect(() => {
    ajax("/api/assignments/", "GET", getJwt).then((assignmentsData) => {
      setAssignment(assignmentsData);
    });
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
      <h1>Dashboard !!</h1>
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
      {assignments &&
        assignments.map((assignment) => (
          // <Col>
          <Card key={assignment.id} style={{ width: "18rem", height: "20rem" }}>
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
                    navigate(`/assignments/${assignment.id}`);
                  }}
                >
                  Edit
                </Button>
              )}
            </Card.Body>
          </Card>
          // </Col>
        ))}
      <Card style={{ width: "18rem", height: "20rem" }}>
        <Card.Body className="d-flex flex-column justify-content-around">
        </Card.Body>
      </Card>
    </Container>
  );
};

// https://youtu.be/SbzKc3lwVQw?si=VRm29BYVejRSMQgL&t=740
// when export "default", we don't need {} when import (check App.js)
export default Dashboard;
