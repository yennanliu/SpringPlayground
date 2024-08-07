package com.yen.SpringAssignmentSystem.controller;

import com.yen.SpringAssignmentSystem.domain.Assignment;
import com.yen.SpringAssignmentSystem.domain.User;
import com.yen.SpringAssignmentSystem.dto.AssignmentResponseDto;
import com.yen.SpringAssignmentSystem.dto.BootcampAssignmentResponseDto;
import com.yen.SpringAssignmentSystem.dto.JavaFoundationsAssignmentResponseDto;
import com.yen.SpringAssignmentSystem.dto.UserKeyDto;
import com.yen.SpringAssignmentSystem.enums.AuthorityEnum;
import com.yen.SpringAssignmentSystem.proffesso.domain.Offer;
import com.yen.SpringAssignmentSystem.service.AssignmentService;
import com.yen.SpringAssignmentSystem.service.OrderService;
import com.yen.SpringAssignmentSystem.service.UserService;
import com.yen.SpringAssignmentSystem.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.yen.SpringAssignmentSystem.service.OrderService.BOOTCAMP_OFFER_IDS;
import static com.yen.SpringAssignmentSystem.service.OrderService.JAVA_FOUNDATIONS_OFFER_ID;

// https://youtu.be/4l1X3PQ_NWw?si=6Q4Pk7_TT3fYVhkv&t=509
// https://github.com/tp02ga/AssignmentSubmissionApp/blob/master/back-end/src/main/java/com/coderscampus/AssignmentSubmissionApp/web/AssignmentController.java


@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"}, allowCredentials = "true")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @PostMapping("")
    public ResponseEntity<?> createAssignment(User user) {

        System.out.println(">>> user = " + user.toString());

        User u1 = new User();
        u1.setUsername("admin");
        u1.setPassword("123");
        u1.setId(1L);

        System.out.println(">>> createAssignment, User = " + u1.toString());
        //Assignment newAssignment = assignmentService.save(u1);
        Assignment newAssignment = assignmentService.save(u1);
        return ResponseEntity.ok(newAssignment);
    }

//    @PostMapping("")
//    public ResponseEntity<?> createAssignment(@AuthenticationPrincipal User user) {
//        Assignment newAssignment = assignmentService.save(user);
//
//        return ResponseEntity.ok(newAssignment);
//    }

    @GetMapping("")
    public ResponseEntity<?> getAssignments(@AuthenticationPrincipal User user) {

        // TODO : fix this, read User from request, instead of hardcode
        //System.out.println(">>> User = " + user.toString());
        User u1 = new User();
        u1.setId(1L);
        Set<Assignment> assignmentsByUser = assignmentService.findByUser(u1);
        return ResponseEntity.ok(assignmentsByUser);
    }

    // NOTE : instead of query assignment with role from JWT, I use the other endpoint instead
    // https://youtu.be/mEdWzE4-sLg?si=OunD8llEQwwNgKid&t=928
    @GetMapping("/to_review")
    public ResponseEntity<?> getToReviewAssignments(@AuthenticationPrincipal User user) {

        ListUtil list_util = new ListUtil();
        List<Assignment> submittedAssignment = assignmentService.findByStatus("Submitted");
        List<Assignment> inReviewAssignment = assignmentService.findByStatus("In Review");
        List<Assignment> needsUpdateAssignment = assignmentService.findByStatus("Needs Update");
        List<Assignment> _merge = list_util.mergeList(submittedAssignment, inReviewAssignment);
        List<Assignment> merge = list_util.mergeList(_merge, needsUpdateAssignment);
        return ResponseEntity.ok(merge);
    }

    @GetMapping("{assignmentId}")
    //public ResponseEntity<?> getAssignment(@PathVariable Long assignmentId, @AuthenticationPrincipal User user)
    public ResponseEntity<?> getAssignment(@PathVariable Long assignmentId) {

        System.out.println(">>> getAssignment assignmentId start ..., assignmentId = " + assignmentId);
        Optional<Assignment> assignmentOpt = assignmentService.findById(assignmentId);
        System.out.println(">>> assignmentOpt = " + assignmentOpt.get());

//        Set<Offer> offers = orderService.findStudentOrdersByUserId(assignmentOpt.get().getUser().getId());
//        boolean isBootcampStudent = offers.stream()
//                .anyMatch(offer -> BOOTCAMP_OFFER_IDS.contains(offer.getId()));
//        boolean isJavaFoundationsStudent = offers.stream()
//                .anyMatch(offer -> offer.getId().equals(JAVA_FOUNDATIONS_OFFER_ID));
//
//        if (isBootcampStudent) {
//            AssignmentResponseDto response = new BootcampAssignmentResponseDto(assignmentOpt.orElse(new Assignment()));
//            return ResponseEntity.ok(response);
//        } else if (isJavaFoundationsStudent) {
//            AssignmentResponseDto response = new JavaFoundationsAssignmentResponseDto(assignmentOpt.orElse(new Assignment()));
//            return ResponseEntity.ok(response);
//        }

        //return ResponseEntity.ok(assignmentOpt.orElse(new Assignment()));
        AssignmentResponseDto response = new BootcampAssignmentResponseDto(assignmentOpt.orElse(new Assignment()));
        // System.out.println(">>> (AssignmentResponseDto) response number = " + Arrays.stream(response.getAssignmentEnums()).count());
        return ResponseEntity.ok(response);
    }

    @PutMapping("{assignmentId}")
//    public ResponseEntity<?> updateAssignment(@PathVariable Long assignmentId,
//                                              @RequestBody Assignment assignment,
//                                              @AuthenticationPrincipal User user) {
        public ResponseEntity<?> updateAssignment(@PathVariable Long assignmentId,
                @RequestBody Assignment assignment) {

        // add the code reviewer to this assignment if it was claimed
//        if (assignment.getCodeReviewer() != null) {
//            User codeReviewer = assignment.getCodeReviewer();
//            codeReviewer = userService.findUserByUsername(codeReviewer.getUsername()).orElseThrow();
//
////            if (AuthorityUtil.hasRole(AuthorityEnum.ROLE_CODE_REVIEWER.name(), codeReviewer)) {
////                assignment.setCodeReviewer(codeReviewer);
////            }
//        }
        assignment.setStatus(assignment.getStatus());
        System.out.println(">>> (PutMapping) update Assignment as = " + assignment);
        Assignment updatedAssignment = assignmentService.save(assignment);
        return ResponseEntity.ok(updatedAssignment);
    }

    @GetMapping("/all")
    public ResponseEntity<?> allAssignments() {
        Map<UserKeyDto, Set<Assignment>> allAssignments = assignmentService.findAll();
        return ResponseEntity.ok(allAssignments);
    }

    @GetMapping("/all2")
    public String allAssignments2() {
        List<Assignment> allAssignments = assignmentService.getAllAssignment();
        System.out.println("count = " + allAssignments.size());
        System.out.println("1st  = " + allAssignments.get(0));
        return "OK";
        //return ResponseEntity.ok(allAssignments);
    }

}
