import React from "react";
import { Badge } from "react-bootstrap";

// https://github.com/tp02ga/AssignmentSubmissionApp/blob/master/front-end/src/StatusBadge/index.js
// https://youtu.be/MGtkDvpD6rs?si=8fiI7fI_uO9vqr3b&t=729
// https://youtu.be/d_YH4TnLuhk?si=T5FeTjUEDWkkJkKl&t=431
const StatusBadge = (props) => {
  const { text } = props;
  function getColorOfBadge() {
    if (text === "Completed") return "success";
    else if (text === "Needs Update") return "danger";
    else if (text === "Pending Submission") return "warning";
    else if (text === "Resubmitted") return "primary";
    else if (text === "In Review") return "primary";
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