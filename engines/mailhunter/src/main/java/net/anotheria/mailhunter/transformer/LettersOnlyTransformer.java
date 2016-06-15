package net.anotheria.mailhunter.transformer;

public class LettersOnlyTransformer extends AbstractTransformer{
	
	@Override public String transform(String s) {
		StringBuilder ret = new StringBuilder();
		for (int i=0; i<s.length(); i++){
			char c = s.charAt(i);
			if (Character.isLetterOrDigit(c) || c==' ')
				ret.append(c);
		}
		return ret.toString();
	}
}
	
