package com.yen.SpringAssignmentSystem.service;

import com.yen.SpringAssignmentSystem.domain.Assignment;
import com.yen.SpringAssignmentSystem.domain.User;
import com.yen.SpringAssignmentSystem.dto.UserKeyDto;
import com.yen.SpringAssignmentSystem.enums.*;
import com.yen.SpringAssignmentSystem.proffesso.domain.Offer;
import com.yen.SpringAssignmentSystem.proffesso.repository.ProffessoUserRepo;
import com.yen.SpringAssignmentSystem.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.yen.SpringAssignmentSystem.service.OrderService.BOOTCAMP_OFFER_IDS;
import static com.yen.SpringAssignmentSystem.service.OrderService.JAVA_FOUNDATIONS_OFFER_ID;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepo;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProffessoUserRepo proffessoUserRepo;

    public Assignment save(User user){

        System.out.println(">>> Assignment save..., user = " + user.toString());

        Assignment assignment = new Assignment();
        assignment.setStatus(AssignmentStatusEnum.PENDING_SUBMISSION.getStatus());
        Integer nextAssignmentToSubmit = findNextAssignmentToSubmit(user);
        assignment.setNumber(nextAssignmentToSubmit);
        assignment.setCreatedDate(LocalDateTime.now());
        assignment.setUser(user);

        System.out.println(">>> Assignment save..., assignment = " + assignment.toString());

        //Set<Offer> offers = orderService.findStudentOrdersByUserId(user.getId());

        // TODO : optimize below
//        boolean isBootcampStudent = offers.stream()
//                .anyMatch(offer -> BOOTCAMP_OFFER_IDS.contains(offer.getId()));
//
//        boolean isJavaFoundationsStudent = offers.stream()
//                .anyMatch(offer -> offer.getId().equals(JAVA_FOUNDATIONS_OFFER_ID));
//
//        if (isBootcampStudent) {
//            AssignmentEnum bootcampAssignmentEnum = BootcampAssignmentEnum.values()[nextAssignmentToSubmit - 1];
//            assignment.setName(bootcampAssignmentEnum.getAssignmentName());
//        } else if (isJavaFoundationsStudent) {
//            AssignmentEnum bootcampAssignmentEnum = JavaFoundationsAssignmentEnum.values()[nextAssignmentToSubmit - 1];
//            assignment.setName(bootcampAssignmentEnum.getAssignmentName());
//        }
        return assignmentRepo.save(assignment);
    }

    private Integer findNextAssignmentToSubmit(User user) {

        Set<Assignment> assignmentsByUser = assignmentRepo.findByUser(user);
        if (assignmentsByUser == null) {
            return 1;
        }

        // TODO : optimize below
        Optional<Integer> nextAssignmentNumOpt = assignmentsByUser.stream().sorted((a1, a2) -> {
            if (a1.getNumber() == null)
                return 1;
            if (a2.getNumber() == null)
                return 1;
            return a2.getNumber().compareTo(a1.getNumber());
        }).map(assignment -> {
            if (assignment.getNumber() == null)
                return 1;
            return assignment.getNumber() + 1;
        }).findFirst();
        return nextAssignmentNumOpt.orElse(1);
    }

    // TODO : check purpose of this
    //@Secured({"ROLE_INSTRUCTOR"})
    public Map<UserKeyDto, Set<Assignment>> findAll(){

        List<Assignment> assignments = assignmentRepo.findAllActiveBootcampStudents();

        var droppedStudents = proffessoUserRepo.findDroppedBootcampStudents();

        // TODO : optimize below
        Map<UserKeyDto, Set<Assignment>> allStudentAssignments = assignments.stream()
                .filter(assignment -> !droppedStudents.stream()
                        .filter(droppedStudent -> droppedStudent.getId().equals(assignment.getUser().getId()))
                        .findAny()
                        .isPresent())
                .filter(a -> (a.getName() == null) || !a.getName().equals("Compounding Interest Calculator") && !a.getName().equals("Job Data Parsing & Filtering"))
                .collect(Collectors.groupingBy(a -> new UserKeyDto(a.getUser().getUsername(),
                                a.getUser().getName(),
                                a.getUser().getCohortStartDate(),
                                null),
                                //a.getUser().getBootcampDurationInWeeks()), // TODO : fix this
                        Collectors.mapping(a -> a, Collectors.toSet())));

        TreeMap<UserKeyDto, Set<Assignment>> sortedMap = new TreeMap<>(allStudentAssignments);
        return sortedMap;
    }

    // TODO : fix this
    public Set<Assignment> findByUser(User user){

//        boolean hasCodeReviewerRole = user.getAuthorities().stream()
//                .filter(auth -> AuthorityEnum.ROLE_CODE_REVIEWER.name().equals(auth.getAuthority())).count() > 0;

//        if (hasCodeReviewerRole) {
//            // load assignments if you're a code reviewer role
//            return assignmentRepo.findByCodeReviewer(user);
//        } else {
//            // load assignments if you're a student role
//            return assignmentRepo.findByUser(user);
//        }

        return assignmentRepo.findByUser(user);
    }

    public Optional<Assignment> findById(Long assignmentId) {

        return assignmentRepo.findById(assignmentId);
    }

    public Assignment save(Assignment assignment) {

        // load the assignment from DB using assignment.getId() in order to get current
        // status
        Assignment oldAssignment = assignmentRepo.findById(assignment.getId()).get();
        String oldStatus = oldAssignment.getStatus();

        assignment.setLastModified(LocalDateTime.now());
        Assignment newAssignment = assignmentRepo.save(assignment);
        String newStatus = newAssignment.getStatus();

        // TODO : optimize below
        if (!oldStatus.equals(newStatus)) {
            if ((oldStatus.contentEquals("Pending Submission") && newStatus.contentEquals("Submitted"))) {
                newAssignment.setLastModified(LocalDateTime.now());
                if (newAssignment.getSubmittedDate() == null)
                    newAssignment.setSubmittedDate(LocalDateTime.now());
                newAssignment = assignmentRepo.save(newAssignment);
            }
            if ((oldStatus.contentEquals("Pending Submission") && newStatus.contentEquals("Submitted"))
                    || (oldStatus.contentEquals("Needs Update") && newStatus.contentEquals("Resubmitted"))) {
                notificationService.sendAssignmentStatusUpdateCodeReviewer(oldStatus, assignment);
                if (AssignmentStatusEnum.RESUBMITTED.getStatus().equalsIgnoreCase(newStatus)) {
                    newAssignment.setCodeReviewer(null);
                    newAssignment = assignmentRepo.save(newAssignment);
                }
            }

            if ((oldStatus.contentEquals("In Review") && newStatus.contentEquals("Completed"))
                    || (oldStatus.equals("In Review") && newStatus.equals("Needs Update"))) {
                notificationService.sendAssignmentStatusUpdateStudent(oldStatus, assignment);
            }
        }

        return newAssignment;
    }

}
