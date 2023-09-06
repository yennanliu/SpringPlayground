import React from "react";

const AssignmentView = () => {
  const assignmentId = window.location.href.split("/assignments/")[1];
  return (
    <div>
      <h1>Assignment ID : {assignmentId}</h1>
    </div>
  );
};

export default AssignmentView;
