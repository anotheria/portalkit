package net.anotheria.mailhunter.transformer;

import net.anotheria.util.StringUtils;

public class ReverseStringTransformer extends AbstractTransformer{
	
	@Override public String transform(String s) {
		return StringUtils.reverseString(s);
	}

}
