// https://youtu.be/YYDpGYOjfqM?si=Pu9pvp4QX6L637WD&t=369

import React from "react";
import { useLocalState } from "../util/useLocalStorage";
import { Navigate } from "react-router";

const PrivateRoute = ({ children }) => {
  const [getJwt, setJwt] = useLocalState("", "jwt"); // getter, setter
  return getJwt ? children : <Navigate to="/login/"></Navigate>; // if no JWT found, then redirect to /login
};

export default PrivateRoute;
