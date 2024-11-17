package com.yen.SpringAssignmentSystem.service;

import com.yen.SpringAssignmentSystem.proffesso.domain.Offer;
import com.yen.SpringAssignmentSystem.proffesso.domain.Order;
import com.yen.SpringAssignmentSystem.proffesso.domain.ProffessoUser;
import com.yen.SpringAssignmentSystem.proffesso.repository.OrderRepository;
import com.yen.SpringAssignmentSystem.proffesso.repository.ProffessoUserRepo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    // TODO : optimize below
    public static final List<Long> BOOTCAMP_OFFER_IDS = List.of(225L, 221L, 218L, 212L);
    public static final Long JAVA_FOUNDATIONS_OFFER_ID = 227L;

    // attr
    private OrderRepository orderRepo;
    private ProffessoUserRepo proffessoUserRepo;

    // constructor
    public OrderService(OrderRepository orderRepo, ProffessoUserRepo proffessoUserRepo){
        this.orderRepo = orderRepo;
        this.proffessoUserRepo = proffessoUserRepo;
    }

    public Set<Offer> findStudentOrdersByUserId(Long userId){

        Optional<ProffessoUser> proffessoUserOpt = proffessoUserRepo.findById(userId);

        if (proffessoUserOpt.isPresent()){

            Set<Order> orders = orderRepo.findByUser(proffessoUserOpt.get());

            /** NOTE : stream flatmap op below */
            Set<Offer> offers = orders.stream()
                    .map(order -> order.getOffers())
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());

            return offers;
        }else{
            return null;
        }
    }

}
