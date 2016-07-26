package net.anotheria.portalkit.engines.mailhunter;

import net.anotheria.portalkit.engines.mailhunter.transformer.LocalizedTransformer;

import java.util.ArrayList;
import java.util.List;


public class Transformation {

	private ArrayList<Transformer> transformers;

	private String locale;
	
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

	public String transform(String s) {
		return transform(s, "");
	}

	public String transform(String s, String locale) {

		for (int i = 0; i < transformers.size(); i++) {

			Transformer transformer = transformers.get(i);

			if (transformer instanceof LocalizedTransformer) {
				s = transformer.transform(s, locale);
			} else {
				s = transformer.transform(s);
			}
		}

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
