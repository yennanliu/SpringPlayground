// https://youtu.be/SbzKc3lwVQw?si=jKjSbEO7OH2UshFi&t=495
// type "rsc", and sould auto generate basic code

import React from "react";
import { useLocalState } from "../util/useLocalStorage";

const Dashboard = () => {
  const [getJwt, setJwt] = useLocalState("", "jwt"); // getter, setter

  return (
    <div>
      <h1>Dashboard !!</h1>
      <div>JWT value is {getJwt}</div>
    </div>
  );
};

// https://youtu.be/SbzKc3lwVQw?si=VRm29BYVejRSMQgL&t=740
// when export "default", we don't need {} when import (check App.js)
export default Dashboard;
