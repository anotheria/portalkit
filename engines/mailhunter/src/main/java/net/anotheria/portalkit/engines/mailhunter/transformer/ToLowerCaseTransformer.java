package net.anotheria.portalkit.engines.mailhunter.transformer;

/**
 * TODO please remined another to comment this class
 * @author another
 */
public class ToLowerCaseTransformer extends AbstractTransformer{

	@Override public String transform(String s) {
		return s.toLowerCase();
	}

}
