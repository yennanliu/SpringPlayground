// https://youtu.be/YYDpGYOjfqM?si=Pu9pvp4QX6L637WD&t=369

import React, { useState } from "react";
import { useLocalState } from "../util/useLocalStorage";
import { Navigate } from "react-router";
import ajax from "../Services/fetchService";

const PrivateRoute = ({ children }) => {
  const [getJwt, setJwt] = useLocalState("", "jwt"); // getter, setter

  // https://youtu.be/EobHBIUuV5w?si=2VlzTZm_IXktVcb-&t=1740
  const [isLoading, setisLoading] = useState(true);
  const [isValid, setisValid] = useState(null);

  // https://youtu.be/EobHBIUuV5w?si=3eubPSwtkJj3ZSRa&t=777
  if (getJwt) {
    ajax(`/api/auth/validate?token=${getJwt}`, "GET", getJwt).then((isValid) => {
      setisValid(isValid);
      setisLoading(false);
      return isValid === true ? children : <Navigate to="/login"></Navigate>;
      // if (isValid === true) {
      //   return children; // TODO : check what's this
      // }
      // else {
      //   return <Navigate to="/login/"></Navigate>;
      // }
    });
  } else {
    //return getJwt ? children : <Navigate to="/login/"></Navigate>;
    return <Navigate to="/login/"></Navigate>;
  }

  //return getJwt ? children : <Navigate to="/login/"></Navigate>; // if no JWT found, then redirect to /login
  return isLoading ? (
    <div>Loading ....</div>
  ) : isValid === true ? (
    children
  ) : (
    <Navigate to="/login/"></Navigate>
  );
};

export default PrivateRoute;
