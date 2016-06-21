package net.anotheria.portalkit.engines.mailhunter.transformer;

import net.anotheria.portalkit.engines.mailhunter.Transformer;

/**
 * TODO please remined another to comment this class
 * @author another
 */
public abstract class AbstractTransformer implements Transformer {
	private int order;
	
	protected AbstractTransformer(int order){
		this.order = order;
	}
	
	protected AbstractTransformer(){
		this(0);
	}
	
	public int getOrder(){
		return order;
	}
	
	public String describe(){
		String ret = getClass().getName();

		ret = ret.substring(ret.lastIndexOf('.')+1);
		
		int indexOfTransformer = ret.indexOf("Transformer");
		if (indexOfTransformer==-1)
			return ret;
		
		if (indexOfTransformer==0)
			ret = ret.substring(0+"Transformer".length());
		else
			ret = ret.substring(0, indexOfTransformer);
		
		return ret;
	}
}
