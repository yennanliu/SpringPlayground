package lab.mockito.basic.wildcardMatcher;

public class ServiceFacade {

	Service service;

	public ServiceFacade(Service service) {
		this.service = service;
	}

	public Object call(Object input) {
		Request req = new Request(input);
		Response resp = service.call(req);
		return resp.getOutput();
	}
}
