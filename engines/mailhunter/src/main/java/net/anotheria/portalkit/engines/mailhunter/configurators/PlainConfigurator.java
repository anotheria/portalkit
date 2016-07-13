package net.anotheria.portalkit.engines.mailhunter.configurators;

import net.anotheria.portalkit.engines.mailhunter.EngineConfigurator;
import net.anotheria.portalkit.engines.mailhunter.TransformationEngine;
import net.anotheria.portalkit.engines.mailhunter.Transformation;
import net.anotheria.portalkit.engines.mailhunter.Transformer;
import net.anotheria.portalkit.engines.mailhunter.matcher.GenericAndMatcher;
import net.anotheria.portalkit.engines.mailhunter.matcher.GenericContainsMatcher;
import net.anotheria.portalkit.engines.mailhunter.transformer.CapitalsOnlyTransformer;
import net.anotheria.portalkit.engines.mailhunter.transformer.LettersOnlyTransformer;
import net.anotheria.portalkit.engines.mailhunter.transformer.NoOpTransformer;
import net.anotheria.portalkit.engines.mailhunter.transformer.RemoveDuplicateLettersTransformer;
import net.anotheria.portalkit.engines.mailhunter.transformer.RemoveWhitespacesTransformer;
import net.anotheria.portalkit.engines.mailhunter.transformer.ReverseStringTransformer;
import net.anotheria.portalkit.engines.mailhunter.transformer.StartingLettersTransformer;
import net.anotheria.portalkit.engines.mailhunter.transformer.ToLowerCaseTransformer;
import net.anotheria.portalkit.engines.mailhunter.transformer.DecryptionTransformer;

public class PlainConfigurator implements EngineConfigurator{
	
	public void configure(TransformationEngine engine) {
		configureTransformations(engine);
		configureMatchers(engine);		
	}
	
	private void configureTransformations(TransformationEngine engine){
		engine.addTransformation(new Transformation(new NoOpTransformer()));
		engine.addTransformation(new Transformation(new RemoveWhitespacesTransformer()));
		engine.addTransformation(createTransformation(new LettersOnlyTransformer(), new StartingLettersTransformer(), new ToLowerCaseTransformer()));
		engine.addTransformation(createTransformation(new CapitalsOnlyTransformer(), new ToLowerCaseTransformer()));
		engine.addTransformation(new Transformation(new StartingLettersTransformer()));
		engine.addTransformation(createTransformation(new ToLowerCaseTransformer(), new RemoveWhitespacesTransformer(), new DecryptionTransformer()));
		engine.addTransformation(createTransformation(new ToLowerCaseTransformer(), new LettersOnlyTransformer(), new RemoveWhitespacesTransformer(), new DecryptionTransformer()));
		engine.addTransformation(new Transformation(new RemoveDuplicateLettersTransformer()));	
		engine.addTransformation(new Transformation(new ReverseStringTransformer()));
	}
	
	private Transformation createTransformation(Transformer a, Transformer ... b){
		Transformation t = new Transformation();
		if (a!=null)
			t.addTransformer(a);
		if (b!=null){
			for (Transformer tt : b)
				t.addTransformer(tt);
		}
		return t;  
	}
	
	
	private static final String [] DOMAINS = {
		//"icq",
		"finya","single.de","austria.at",
		"bluemail.","netcologne",
		"gmx","ge em ix", "ge äm ix",
		"yahoo","yohaa","jahu","jahoo",
		"lycos",
 		"freenet","freeenet",
		"compuserve",
		"t-online","online.","onlein.","onlain.","onlinecom","onlinede","t-offline", 
		"hotmail","hotmehl", "heißepost", "heissepost", "heissemail", "heiße mail", "heiße post",
		"bluewin", "nexgo", "freesurf", "arcor", "imail",
		"tiscali",
		"wäb","wäp","web.","webde",
		"aol",
		".com",/*".ch",*/"www.","mail.com","mail.",
		"genion",
		"arroba",
		
		"lokis", "lokalisten","loka",
		"skype", "msn", "aim", "sms", "www", "url", 
		"gmail", "googlemail" 
		
		/**
		
		  "icq"," i c q",
		  
		  "0","1","2","3","4","5","6","7","8","9",
		"eins", "zwei", "drei", "vier", "fünf", "fuenf", "sechs", "sieben", "acht", "neun",
		 "nu", "ei","zw", "dr", "vi", "fu", "se", "si", "ac", "ne",
		        **/
	};
	
	/*
	private static final String[] ICQREGEX = {
	    //"(\\w*\\s*)*(ei|i|ey|ai|ay)\\s*(see|seek|c|zi)\\s*(you|q|qu|que|kuh|u|ju)",
	    "(ei|i|ey|ai|ay)(\\s+\\w*\\s*|\\s)*(see|seek|c|zi)(\\s+\\w*\\s*|\\s)*(you|q|qu|que|kuh|u|ju)",
	    //"(\\d\\s*\\w*\\s*){6,}",
	    //"(\\w*\\s*)*(\\d{6,})",
	    "((\\d|eins|zwei|drei|vier|fünf|fuenf|sechs|sieben|acht|neun|nu|ei|zw|dr|vi|fu|se|si|ac|ne)\\s*\\w*\\s*){6,}",
	    //"(\\w*\\s*)*((eins|zwei|drei|vier|fünf|fuenf|sechs|sieben|acht|neun|nu|ei|zw|dr|vi|fu|se|si|ac|ne)\\s*){6,}",
	    };
	*/
	
	private void configureMatchers(TransformationEngine engine){
		for (int i=0; i<DOMAINS.length; i++){
			//engine.addMatcher(new GenericExactMatcher(DOMAINS[i]));
			engine.addMatcher(new GenericContainsMatcher(DOMAINS[i]));
		}
		
		GenericAndMatcher addMatcher1 = new GenericAndMatcher(null, null, 0.90);
		addMatcher1.addFirstValue("@");
		addMatcher1.addSecondValue(".com");
		addMatcher1.addSecondValue(".de");
		addMatcher1.addSecondValue(".info");
		addMatcher1.addSecondValue(".net");
		addMatcher1.addSecondValue(".es");
		addMatcher1.addSecondValue(".it");
		addMatcher1.addSecondValue(".ch");
		addMatcher1.addSecondValue(".at");
		engine.addMatcher(addMatcher1);
		
		GenericAndMatcher addMatcher2 = new GenericAndMatcher(null, null, 0.90);
		addMatcher2.addFirstValue("www.");
		addMatcher2.addSecondValue(".com");
		addMatcher2.addSecondValue(".de");
		addMatcher2.addSecondValue(".es");
        addMatcher2.addSecondValue(".it");
        addMatcher2.addSecondValue(".ch");
        addMatcher2.addSecondValue(".at");
		engine.addMatcher(addMatcher2);

		//RegExMatcher addMatcher3 = new RegExMatcher(ICQREGEX);
		//engine.addMatcher(addMatcher3);
		 
	}

}
