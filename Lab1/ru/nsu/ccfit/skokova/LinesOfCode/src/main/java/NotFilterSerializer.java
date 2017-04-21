package ru.nsu.ccfit.skokova.LinesOfCode;

public class NotFilterSerializer implements FilterSerializer {
	static {
		FilterFactory.creators.put("!", NotFilterSerializer.class);
	}

	public NotFilter parseFilter(String line) throws FilterCreateException {
		String forParse = line.trim().substring(2, line.length() - 1);
		NotFilter result = null;
		try {
			result = new NotFilter(FilterFactory.getInstance().createFilterSerializer(forParse).parseFilter(forParse), line);
		} catch (InstantiationException | IllegalAccessException e) {
		    System.out.println("Oops"); //TODO
	    } catch (IncorrectFilterTypeException e) {
            System.err.println(e.getLocalizedMessage());
        }
		return result;
	}
}
