package com.member;
 
public class SessionInfo {
	private String mem_id, mem_name;
	private int memRoll;
	
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	public String getMem_name() {
		return mem_name;
	} 
	public void setMem_name(String mem_name) {
		this.mem_name = mem_name;
	}
	public int getMemRoll() {
		return memRoll;
	}
	public void setMemRoll(int memRoll) {
		this.memRoll = memRoll;
	}
}
