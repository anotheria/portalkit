package net.anotheria.portalkit.engines.mailhunter.transformer;

import net.anotheria.util.StringUtils;

/**
 * TODO please remined another to comment this class
 * @author another
 */
public class ToMailAttributesTransformer extends AbstractTransformer{
	
	private static final String[] crypted = {
		"arroba",
		"ätt",
		"ät",
		"ädd",
		"klammeraffe",
		//"bei",
		"pkt",
		"punkt",
		"point",
		"inde",
		"ausde",
		"minus",
		"mehl",
//		"kom",
		"icks",
		"ehm",
		"weh",
		"wewewe",
		"ddee",
		"dot",
		"warm",
		"post"
	};

	private static final String[] uncrypted = {
		"@",
		"@",
		"@",
		"@",
		"@",
		//"@",
		".",
		"."	,
		"."	,
		"de",
		"de",
		"-",
		"mail",
//		"com",
		"x",
		"m",
		"w",
		"www",
		"de",
		".",
		"hot",
		"mail"
	};

	@Override public String transform(String s) {
		//System.out.print("From "+s);
		for (int i=0; i<crypted.length; i++)
			if (s.indexOf(crypted[i])!=-1)
				s = StringUtils.replace(s, crypted[i], uncrypted[i]);
		//System.out.println("to "+s);	
		return s;
	}

}
