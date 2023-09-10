import React from "react";
import { Badge } from "react-bootstrap";

// https://github.com/tp02ga/AssignmentSubmissionApp/blob/master/front-end/src/StatusBadge/index.js

const StatusBadge = (props) => {
  const { text } = props;
  function getColorOfBadge() {
    if (text === "Completed") return "success";
    else if (text === "Needs Update") return "danger";
    else if (text === "Pending Submission") return "warning";
    else if (text === "Resubmitted") return "primary";
    else return "info";
  }
  return (
    <Badge
      pill
      bg={getColorOfBadge()}
      style={{
        fontSize: "1em",
      }}
    >
      {text}
    </Badge>
  );
};

export default StatusBadge;