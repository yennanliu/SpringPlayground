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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
        Set<Assignment> assignmentsByUser = assignmentService.findByUser(user);
        return ResponseEntity.ok(assignmentsByUser);
    }

    @GetMapping("{assignmentId}")
    public ResponseEntity<?> getAssignment(@PathVariable Long assignmentId, @AuthenticationPrincipal User user) {
        Optional<Assignment> assignmentOpt = assignmentService.findById(assignmentId);

        Set<Offer> offers = orderService.findStudentOrdersByUserId(assignmentOpt.get().getUser().getId());
        boolean isBootcampStudent = offers.stream()
                .anyMatch(offer -> BOOTCAMP_OFFER_IDS.contains(offer.getId()));
        boolean isJavaFoundationsStudent = offers.stream()
                .anyMatch(offer -> offer.getId().equals(JAVA_FOUNDATIONS_OFFER_ID));

        if (isBootcampStudent) {

            AssignmentResponseDto response = new BootcampAssignmentResponseDto(assignmentOpt.orElse(new Assignment()));
            return ResponseEntity.ok(response);
        } else if (isJavaFoundationsStudent) {
            AssignmentResponseDto response = new JavaFoundationsAssignmentResponseDto(assignmentOpt.orElse(new Assignment()));
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(new BootcampAssignmentResponseDto());
    }

    @PutMapping("{assignmentId}")
    public ResponseEntity<?> updateAssignment(@PathVariable Long assignmentId,
                                              @RequestBody Assignment assignment,
                                              @AuthenticationPrincipal User user) {
        // add the code reviewer to this assignment if it was claimed
        if (assignment.getCodeReviewer() != null) {
            User codeReviewer = assignment.getCodeReviewer();
            codeReviewer = userService.findUserByUsername(codeReviewer.getUsername()).orElseThrow();

//            if (AuthorityUtil.hasRole(AuthorityEnum.ROLE_CODE_REVIEWER.name(), codeReviewer)) {
//                assignment.setCodeReviewer(codeReviewer);
//            }
        }
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
