package com.member;
 
public class MemberDTO {
	private String cal_code, cal_name, cal_add;
		// [대학] 학교코드, 학교명, 학교주소
	private String mem_id, mem_name, mem_pw, mem_gt;
		// [회원] 아이디, 이름, 패스워드, 권한
	private String mem_add, mem_tel, mem_gen, mem_email;
		// [회원] 주소, 전화번호, 성별, 이메일
	private String mem_tel1, mem_tel2, mem_tel3;
		// [회원] 전화번호 분리
	private String mem_email1, mem_email2;
		// [회원] 이메일 분리
	
	 
	
	public String getCal_code() {
		return cal_code;
	}
	public String getMem_tel1() {
		return mem_tel1;
	}
	public void setMem_tel1(String mem_tel1) {
		this.mem_tel1 = mem_tel1;
	}
	public String getMem_tel2() {
		return mem_tel2;
	}
	public void setMem_tel2(String mem_tel2) {
		this.mem_tel2 = mem_tel2;
	}
	public String getMem_tel3() {
		return mem_tel3;
	}
	public void setMem_tel3(String mem_tel3) {
		this.mem_tel3 = mem_tel3;
	}
	public String getMem_email1() {
		return mem_email1;
	}
	public void setMem_email1(String mem_email1) {
		this.mem_email1 = mem_email1;
	}
	public String getMem_email2() {
		return mem_email2;
	}
	public void setMem_email2(String mem_email2) {
		this.mem_email2 = mem_email2;
	}
	public void setCal_code(String cal_code) {
		this.cal_code = cal_code;
	}
	public String getCal_name() {
		return cal_name;
	}
	public void setCal_name(String cal_name) {
		this.cal_name = cal_name;
	}
	public String getCal_add() {
		return cal_add;
	}
	public void setCal_add(String cal_add) {
		this.cal_add = cal_add;
	}
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
	public String getMem_pw() {
		return mem_pw;
	}
	public void setMem_pw(String mem_pw) {
		this.mem_pw = mem_pw;
	}
	public String getMem_gt() {
		return mem_gt;
	}
	public void setMem_gt(String mem_gt) {
		this.mem_gt = mem_gt;
	}
	public String getMem_add() {
		return mem_add;
	}
	public void setMem_add(String mem_add) {
		this.mem_add = mem_add;
	}
	public String getMem_tel() {
		return mem_tel;
	}
	public void setMem_tel(String mem_tel) {
		this.mem_tel = mem_tel;
	}
	public String getMem_gen() {
		return mem_gen;
	}
	public void setMem_gen(String mem_gen) {
		this.mem_gen = mem_gen;
	}
	public String getMem_email() {
		return mem_email;
	}
	public void setMem_email(String mem_email) {
		this.mem_email = mem_email;
	}
}
