//package aosproject;

public class HostStatus {
	private int hostId;
	private long hostClock;
	private boolean isHostCompetingForCS;
	private boolean isHostInCS;
	private long clockOfRecentCSRequestMade;
	private int responseCount;
	private int totalNumOfHosts;
//	private boolean hostMListenerBlocked;
	boolean needToAsk[] = new boolean[9];

	public HostStatus(int id, int hostCount) {
		// TODO Auto-generated constructor stub
		this.hostId = id;
		this.totalNumOfHosts = hostCount;
		
		this.hostClock = 0;
		this.isHostCompetingForCS = false;
		this.isHostInCS = false;
		this.responseCount = 0;
		//this.hostMListenerBlocked = false;
	}
	
	public synchronized long incrementAccordingToLamport(long msgClock)
	{
		long localHostClock=this.hostClock;
		localHostClock++;
		msgClock++;
		this.hostClock=(localHostClock >= msgClock ? localHostClock : msgClock);
		long toReturnClock = this.hostClock;
		return toReturnClock;
	}
	
/*	public synchronized boolean isHostMListenerBlocked() {
		return hostMListenerBlocked;
	}

	public synchronized void setHostMListenerBlocked(boolean hostMListenerBlocked) {
		this.hostMListenerBlocked = hostMListenerBlocked;
	}*/

	public synchronized int getTotalNumOfHosts() {
		return totalNumOfHosts;
	}

	public synchronized int getResponseCount() {
		return responseCount;
	}
/*	this should never be called in optimized algorithm
	public synchronized void setResponseCountToZero() {
		this.responseCount = 0;
	}
*/
	public synchronized void incrementResponseCount()
	{
		++this.responseCount;
	}
	public synchronized void decreaseResponseCount()
	{
		--this.responseCount;
	}
	
	public synchronized long getClockOfRecentCSRequestMade() {
		return clockOfRecentCSRequestMade;
	}

/*	public synchronized void setClockOfRecentCSRequestMade(long clockOfRecentRequestMade) {
		this.clockOfRecentCSRequestMade = clockOfRecentRequestMade;
	}*/

	public void setHostId(int hostId) {
		this.hostId = hostId;
	}

	public int getHostId() {
		return hostId;
	}
	
	public synchronized boolean isHostCompetingForCS() {
		return isHostCompetingForCS;
	}

/*	public synchronized void setHostCompetingForCS(boolean isHostCompeting) {
		this.isHostCompetingForCS = isHostCompeting;
	}*/

/*	public synchronized long getHostClock() {
		return hostClock;
	}

	public synchronized void setHostClock(long hostClock) {
		this.hostClock = hostClock;
	}*/
	public synchronized long incrementHostClock()
	{
		++this.hostClock;
		long tmp=hostClock;
		return tmp;
	}
	public synchronized long prepareForCCS()
	{
		this.isHostCompetingForCS = true;
		++this.hostClock;
		this.clockOfRecentCSRequestMade = this.hostClock;
	//	this.responseCount = 0;
		long tmp=hostClock;
		return tmp;
	}
	public synchronized void prepareForCSEnd()
	{
		this.isHostCompetingForCS = false;
		this.isHostInCS = false;
	}
	
	public String printHostStatus()
	{
		String x = "  |hostId:" + hostId + "| ";
		x+=" |hostClock:" + hostClock +"| ";
		x+=" |isHostCompetingForCS:" + isHostCompetingForCS +"| ";
		x+=" |clockOfRecentCSRequestMade:" + clockOfRecentCSRequestMade +"| ";
		x+=" |responseCount:" + responseCount +"| ";
		x+=" |totalNumOfHosts:" + totalNumOfHosts +"| ";
		return x;
	}

	public synchronized boolean isHostInCS() {
		return isHostInCS;
	}

	public synchronized void setHostInCS(boolean isHostInCS) {
		this.isHostInCS = isHostInCS;
	}
}
