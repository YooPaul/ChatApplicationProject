package dbManager;

import java.io.Serializable;
import java.util.Date;

public class ChatMsgDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String friendid;
	private String msg;
	private java.util.Date nalja;
	private String date;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setChatMsgDTO(String id, String friendid, String msg, String nalja) {
		this.id = id;
		this.friendid = friendid;
		this.msg = msg;
		this.date = nalja;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFriendid() {
		return friendid;
	}
	public void setFriendid(String friendid) {
		this.friendid = friendid;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public java.util.Date getNalja() {
		return nalja;
	}
	public void setNalja(java.util.Date nalja) {
		this.nalja = nalja;
	}
}
