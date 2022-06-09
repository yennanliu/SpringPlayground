package lab.mockito.basic.stub;

public enum SortOrder {
	
   ASC, DESC;
   
   public static SortOrder convert(String order) {
	   for(SortOrder o: values()) {
		   if(o.name().equalsIgnoreCase(order)) {
			   return o;
		   }
	   }
	   return null;
   }
}
