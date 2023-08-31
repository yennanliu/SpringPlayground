import React from "react";

// https://youtu.be/YYDpGYOjfqM?si=KndWtVvDf0dtxBBS&t=161

const Login = () => {
  return (
    // <></> : React fragment
    <>
      <div>
        <label htmlFor="username">Username</label>
        <input type="email" id="username"></input>
      </div>
      <div>
        <label htmlFor="password">Password</label>
        <input type="password" id="password"></input>
      </div>
    </>
  );
};

export default Login;
