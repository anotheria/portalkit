package net.anotheria.portalkit.engines.mailhunter.transformer;

import net.anotheria.util.StringUtils;

/**
 * TODO please remined another to comment this class
 * @author another
 */
public class StartingLettersTransformer extends AbstractTransformer{
	
	@Override public String transform(String s) {
		s = StringUtils.replace(s, '\t', ' ');
		s = StringUtils.replace(s, '\r', ' ');
		s = StringUtils.replace(s, '\n', ' ');
		String t[] = StringUtils.tokenize(s, ' ');
		StringBuilder ret = new StringBuilder(t.length);
		for (int i=0; i<t.length; i++)
			if (t[i]!=null && t[i].length()>0)
				ret.append(t[i].charAt(0));
			
		return ret.toString();
	}

}
 