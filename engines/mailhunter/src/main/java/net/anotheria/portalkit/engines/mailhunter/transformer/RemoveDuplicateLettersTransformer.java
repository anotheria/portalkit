package net.anotheria.portalkit.engines.mailhunter.transformer;

/**
 * TODO please remined another to comment this class
 * @author another
 */
public class RemoveDuplicateLettersTransformer extends AbstractTransformer{
	public String transform(String s) {
		if (s.length()<2)
			return s;
		StringBuilder ret = new StringBuilder();
		
		int i=1;
		int l = s.length();
		char c1,c2;
		ret.append(s.charAt(0));
		while(i<l){
			c1 = s.charAt(i-1);
			c2 = s.charAt(i);
			if (c2!=c1)
				ret.append(c2);
			i++;
		}
		
		return ret.toString();
	}
}
