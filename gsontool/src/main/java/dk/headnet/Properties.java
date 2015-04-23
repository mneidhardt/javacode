package dk.headnet;

public class Properties {

	String om;
	String time;
	String tz;
	String st;
	String stf;
	String stn;
	String mag;
	String inj;
	String fat;
	String loss;
	String closs;
	String slat;
	String slon;
	String elat;
	String elon;
	String len;
	String wid;
	String created_at;
	String updated_at;
	String date_fix;
	int cartodb_id;
	public String getOm() {
		return om;
	}
	public void setOm(String om) {
		this.om = om;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTz() {
		return tz;
	}
	public void setTz(String tz) {
		this.tz = tz;
	}
	public String getSt() {
		return st;
	}
	public void setSt(String st) {
		this.st = st;
	}
	public String getStf() {
		return stf;
	}
	public void setStf(String stf) {
		this.stf = stf;
	}
	public String getStn() {
		return stn;
	}
	public void setStn(String stn) {
		this.stn = stn;
	}
	public String getMag() {
		return mag;
	}
	public void setMag(String mag) {
		this.mag = mag;
	}
	public String getInj() {
		return inj;
	}
	public void setInj(String inj) {
		this.inj = inj;
	}
	public String getFat() {
		return fat;
	}
	public void setFat(String fat) {
		this.fat = fat;
	}
	public String getLoss() {
		return loss;
	}
	public void setLoss(String loss) {
		this.loss = loss;
	}
	public String getCloss() {
		return closs;
	}
	public void setCloss(String closs) {
		this.closs = closs;
	}
	public String getSlat() {
		return slat;
	}
	public void setSlat(String slat) {
		this.slat = slat;
	}
	public String getSlon() {
		return slon;
	}
	public void setSlon(String slon) {
		this.slon = slon;
	}
	public String getElat() {
		return elat;
	}
	public void setElat(String elat) {
		this.elat = elat;
	}
	public String getElon() {
		return elon;
	}
	public void setElon(String elon) {
		this.elon = elon;
	}
	public String getLen() {
		return len;
	}
	public void setLen(String len) {
		this.len = len;
	}
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public String getDate_fix() {
		return date_fix;
	}
	public void setDate_fix(String date_fix) {
		this.date_fix = date_fix;
	}
	public int getCartodb_id() {
		return cartodb_id;
	}
	public void setCartodb_id(int cartodb_id) {
		this.cartodb_id = cartodb_id;
	}

	@Override
	public String toString() {
		return "Properties [om=" + om + ", time=" + time + ", tz=" + tz
				+ ", st=" + st + ", stf=" + stf + ", stn=" + stn + ", mag="
				+ mag + ", inj=" + inj + ", fat=" + fat + ", loss=" + loss
				+ ", closs=" + closs + ", slat=" + slat + ", slon=" + slon
				+ ", elat=" + elat + ", elon=" + elon + ", len=" + len
				+ ", wid=" + wid + ", created_at=" + created_at
				+ ", updated_at=" + updated_at + ", date_fix=" + date_fix
				+ ", cartodb_id=" + cartodb_id + "]";
	}

}
