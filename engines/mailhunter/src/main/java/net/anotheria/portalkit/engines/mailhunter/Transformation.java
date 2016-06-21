package net.anotheria.portalkit.engines.mailhunter;

import java.util.ArrayList;
import java.util.List;


public class Transformation {
	private ArrayList<Transformer> transformers;
	
	public Transformation(){
		transformers = new ArrayList<Transformer>();
	}
	
	public Transformation(Transformer aTransformer){
		this();
		addTransformer(aTransformer);
	}
	
	public Transformation(List<Transformer> someTransformers){
		this();
		transformers.addAll(someTransformers);
	}
	
	public String transform(String s){
		for (int i=0; i<transformers.size(); i++)
			s = transformers.get(i).transform(s);
		return s;
	}
	
	public void addTransformer(Transformer t){
		transformers.add(t);
	}
	
	public String describe(){
		String ret = "";
		for (int i=0; i<transformers.size(); i++){
			ret += transformers.get(i).describe();
			if (i<transformers.size()-1)
				ret += " -> ";
		}
		return ret;
	}
	
	@Override public String toString(){
		return describe();
	}
}
