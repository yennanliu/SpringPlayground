import React from "react";
import { Container } from "react-bootstrap";
import { Link } from "react-router-dom";

// https://youtu.be/SbzKc3lwVQw?si=HkKR_zDUUK0SU9-s&t=970

const Homepage = () => {
  return (
    <Container>
      <div>
        <h1>THIS IS HOMEPAGE!!</h1>
        <p></p>
      </div>
      <Link to="http://localhost:3000/login">Login</Link>
      <p></p>
      <Link to="http://localhost:3000/dashboard">Go to Dashboard</Link>
      <p></p>
      <Link to="http://localhost:3000/code_review_dashboard">
        Go to Code Reviewer Dashboard
      </Link>
      <p></p>
      <Link to="http://localhost:3000/code_review">Go to Code Review</Link>
    </Container>
  );
};

export default Homepage;
