package net.anotheria.mailhunter;

/**
 * TODO please remined another to comment this class
 * @author another
 */
public class TransformationStats {
	private long duration;
	private long requests;
	private long hits;
	private String transformationName;
	
	public TransformationStats(){
		duration = requests = hits = 0;
	}
	
	public TransformationStats(String aTransformationName){
		this();
		transformationName = aTransformationName;
	}
	
	/**
	 * @return
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @return
	 */
	public long getHits() {
		return hits;
	}

	/**
	 * @return
	 */
	public long getRequests() {
		return requests;
	}

	/**
	 * @param l
	 */
	public void setDuration(long l) {
		duration = l;
	}

	/**
	 * @param l
	 */
	public void setHits(long l) {
		hits = l;
	}

	/**
	 * @param l
	 */
	public void setRequests(long l) {
		requests = l;
	}
	
	public double getMidExecution(){
		return (double)getDuration() / getRequests();
	}
	
	public double getHitPerRequestRation(){
		return (double)getHits()/getRequests();
	}
	
	public String toString(){
		String ret = transformationName;
		ret += " R: "+getRequests();
		ret += " H: "+getHits();
		ret += " H/R: "+getHitPerRequestRation();
		ret += " Mid: "+getMidExecution();
		ret += " Total: "+getDuration();
		return ret;		
	}
	
	public void notifyRequest(){
		requests++;
	}
	
	public void notifyDuration(long duration){
		this.duration += duration;
	}
	
	public void notifyHit(){
		hits++;
	}

}
