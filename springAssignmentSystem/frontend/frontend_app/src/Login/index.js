import React, { useState } from "react";
import { useLocalState } from "../util/useLocalStorage";
import { Container, Row, Col, Button } from "react-bootstrap";

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
       */}
      <Container>
        <Row>
          <Col>
            <div>
              <label htmlFor="username">Username</label>
              <input
                type="email"
                id="username"
                value={username}
                onChange={(event) => setUsername(event.target.value)}
              ></input>{" "}
              {/** set username equals as input */}
            </div>
          </Col>
        </Row>
        <Row>
          <Col>
            <div>
              <label htmlFor="password">Password</label>
              <input
                type="password"
                id="password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
              ></input>{" "}
              {/** set password equals as input */}
            </div>
          </Col>
        </Row>
        <Row>
          <Col>
            <div>
              <Button
                id="submit"
                type="button"
                onClick={() => sendLoginRequest()}
              >
                Login
              </Button>
            </div>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default Login;
