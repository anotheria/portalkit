package net.anotheria.mailhunter.transformer;

public class CapitalsOnlyTransformer extends AbstractTransformer{
	
	public static final int A = 'A'-1;
	public static final int Z = 'Z'+1;
	
	@Override public String transform(String s) {
		StringBuilder ret = new StringBuilder();
		for (int i=0; i<s.length(); i++){
			char c = s.charAt(i);
			if (c>A && c<Z)
				ret.append(c);
		}
		return ret.toString();
	}
	

}
