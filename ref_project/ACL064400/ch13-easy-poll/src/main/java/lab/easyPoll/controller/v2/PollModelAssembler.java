package lab.easyPoll.controller.v2;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import lab.easyPoll.domain.Poll;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;

@Component("pollModelAssemblerV2")
public class PollModelAssembler implements RepresentationModelAssembler<Poll, EntityModel<Poll>> {

	@Override
	public EntityModel<Poll> toModel(Poll poll) {

		Link l1 = linkTo(methodOn(PollController.class).getPoll(poll.getId())).withSelfRel();
		Link l2 = linkTo(methodOn(VoteController.class).getAllVotes(poll.getId())).withRel("votes");
		Link l3 = linkTo(methodOn(ComputeResultController.class).computeResult(poll.getId())).withRel("compute-result");

		return EntityModel.of(poll, l1, l2, l3);
	}

	@Override
	public CollectionModel<EntityModel<Poll>> toCollectionModel(Iterable<? extends Poll> entities) {
		CollectionModel<EntityModel<Poll>> cep = RepresentationModelAssembler.super.toCollectionModel(entities);
		cep.add(linkTo(methodOn(PollController.class).getAllPolls(null)).withSelfRel());
		return cep;
	}
}
