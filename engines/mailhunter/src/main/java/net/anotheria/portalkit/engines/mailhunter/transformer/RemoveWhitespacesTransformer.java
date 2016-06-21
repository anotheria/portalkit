package net.anotheria.portalkit.engines.mailhunter.transformer;

public class RemoveWhitespacesTransformer extends AbstractTransformer{
	
	@Override public String transform(String s) {
		StringBuilder ret = new StringBuilder(s.length());
		for (int i=0; i<s.length(); i++){
			char c = s.charAt(i);
			if (c!=' ' && c!='\n' && c!='\r' && c!='\t' && c!='_')
				ret.append(c);
		}
		return ret.toString();
	}

}
