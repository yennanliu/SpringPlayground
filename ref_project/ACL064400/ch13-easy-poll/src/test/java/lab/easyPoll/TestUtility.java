package lab.easyPoll;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lab.easyPoll.domain.Poll;

public class TestUtility {

	private TestUtility() {
	}

	public static CollectionModel<EntityModel<Poll>> createCollectionModel(Iterable<Poll> polls) {
		List<EntityModel<Poll>> ems = new ArrayList<>();
		for (Poll p : polls) {
			EntityModel<Poll> em = EntityModel.of(p);
			ems.add(em);
		}
		CollectionModel<EntityModel<Poll>> cem = CollectionModel.of(ems);
		return cem;
	}

	public static PagedModel<EntityModel<Poll>> createPagedModel(Iterable<Poll> polls, PageMetadata pm) {
		List<EntityModel<Poll>> ems = new ArrayList<>();
		for (Poll p : polls) {
			EntityModel<Poll> em = EntityModel.of(p);
			ems.add(em);
		}
		PagedModel<EntityModel<Poll>> pem = PagedModel.of(ems, pm);
		return pem;
	}
	
	public static String asJsonString(final Object obj) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
