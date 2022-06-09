package lab.mockito.basic.wildcardMatcher;

public class Service {
	public Response call(Request req) {
		String s = req.getInput() + " EOL";
		return new Response(s);
	}
}
