import React, { useState } from "react";
import { useLocalState } from "../util/useLocalStorage";
import { Container, Row, Col, Button, Form } from "react-bootstrap";

// https://youtu.be/YYDpGYOjfqM?si=KndWtVvDf0dtxBBS&t=161
// https://youtu.be/6qJ8JXWC9IA?si=Dv8hRVq4jVtHc34j&t=118

const Login = () => {
  // binding value with useState (init with null), so once val is updated, React will automatically update element as well
  // https://youtu.be/6qJ8JXWC9IA?si=Az2koaES1S5aCnVN&t=460
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const [getJwt, setJwt] = useLocalState("", "jwt"); // getter, setter

  console.log(">>> username = " + username);
  console.log(">>> password = " + password);

  function sendLoginRequest() {
    console.log(">>> sendLoginRequest start");

    const reqBody = {
      username: username,
      password: password,
    };

    fetch("api/auth/login", {
      //fetch("test_post", {
      headers: {
        "Content-Type": "application/json",
      },
      method: "POST",
      body: JSON.stringify(reqBody),
    })
      // .then((response) => response.json)
      // .then((data) => console.log(data))
      .then((response) => {
        if (response.status === 200) {
          return Promise.all([response.json(), response.headers]);
        } else {
          return Promise.reject("Invalid login attempt");
        }
      })
      .then(([body, headers]) => {
        // headers.forEach((element) => {
        //   console.log(element);
        // });

        // jwt : json web token
        //const jwt = headers.get("authorization")
        setJwt(headers.get("authorization"));
        window.location.href = "dashboard"; // redirect to dashboard
        //console.log(jwt)
        console.log(getJwt);
        console.log(body);
      })
      .catch((message) => {
        alert(">>> login faile : " + message);
      });
  }

  return (
    // <></> : React fragment
    <>
      {/** Bootstrap
       * https://youtu.be/a1sIrTLdYns?si=4GVyycznLkIsMhdT&t=597
       *
       *  use Container, Form group, Form lebel for bootstrap feature
       *  className="fs-1" : fontsize style
       */}
      <Container className="mt-5">
        <Form.Group className="mb-3">
          <Form.Label htmlFor="username" className="fs-1">
            Username
          </Form.Label>
          <Form.Control
            type="email"
            id="username"
            placeholder="test@google.com"
            value={username}
            size="lg"
            onChange={(event) => setUsername(event.target.value)}
          ></Form.Control>{" "}
          {/** set username equals as input */}
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label htmlFor="password" className="fs-1">
            Password
          </Form.Label>
          <Form.Control
            type="password"
            id="password"
            placeholder="Type your password"
            value={password}
            size="lg"
            onChange={(event) => setPassword(event.target.value)}
          ></Form.Control>{" "}
          {/** set password equals as input */}
        </Form.Group>
        <Row>
          <Col className="mt-2">
              <Button
                id="submit"
                type="button"
                size="lg"
                onClick={() => sendLoginRequest()}
              >
                Login
              </Button>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default Login;
