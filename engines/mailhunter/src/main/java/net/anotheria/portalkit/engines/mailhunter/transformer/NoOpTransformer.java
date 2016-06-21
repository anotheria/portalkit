package net.anotheria.portalkit.engines.mailhunter.transformer;

public class NoOpTransformer extends AbstractTransformer{

	@Override public String transform(String s) {
		return s;
	}

}
