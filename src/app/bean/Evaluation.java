package app.bean;

public class Evaluation {
	int seId;
	int sId;
	int eeId;
	String evalRank;
	public Evaluation(int seId,int sId,int eeId,String evalRank){
		this.setEeId(eeId);
		this.setSeId(seId);
		this.setsId(sId);
		this.setEvalRank(evalRank);
		
	}
	public int getSeId() {
		return seId;
	}
	public void setSeId(int seId) {
		this.seId = seId;
	}
	public int getsId() {
		return sId;
	}
	public void setsId(int sId) {
		this.sId = sId;
	}
	public int getEeId() {
		return eeId;
	}
	public void setEeId(int eeId) {
		this.eeId = eeId;
	}
	public String getEvalRank() {
		return evalRank;
	}
	public void setEvalRank(String evalRank) {
		this.evalRank = evalRank;
	}

	
	

}
