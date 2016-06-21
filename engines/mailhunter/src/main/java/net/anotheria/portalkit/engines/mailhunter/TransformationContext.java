package net.anotheria.portalkit.engines.mailhunter;

public class TransformationContext {
	
	/**
	 * Stores the transformation has fired in current request.
	 */
	private Transformation firedTransformation;
	
	private Matcher firedMatcher;
	
	private String toMatch;
	
	public String getToMatch() {
		return toMatch;
	}

	public void setToMatch(String toMatch) {
		this.toMatch = toMatch;
	}

	public Matcher getFiredMatcher() {
		return firedMatcher;
	}

	public void setFiredMatcher(Matcher firedMatcher) {
		this.firedMatcher = firedMatcher;
	}

	public Transformation getFiredTransformation() {
		return firedTransformation;
	}

	public void setFiredTransformation(Transformation firedTransformation) {
		this.firedTransformation = firedTransformation;
	}

	private static InheritableThreadLocal<TransformationContext> context = new InheritableThreadLocal<TransformationContext>(){

		@Override
		protected TransformationContext initialValue() {
			return new TransformationContext();
		}
			
	};
	
	public static TransformationContext getContext(){
		return context.get();
	}
	
	public static void main(String a[]){
		System.out.println(TransformationContext.getContext());
		System.out.println(TransformationContext.getContext());
	}
}
