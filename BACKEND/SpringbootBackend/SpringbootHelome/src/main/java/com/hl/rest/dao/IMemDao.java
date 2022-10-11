package com.hl.rest.dao;

import java.util.List;

import com.hl.rest.vo.Member;

public interface IMemDao {
	/** member CRUD */
	public void registerMem(String email, String password, String username, String school, String isteacher, String grade, String classnum);
	public Member getMem(String email);
	public Member getMem(int memberIdx);
	public int getMemListSize();
	public List<Member> getMemStudentList(int startlist, int listsize, String grade, String classnum);
	public void updateMember(Member member);
	public void deleteMember(String memberIdx);
}
