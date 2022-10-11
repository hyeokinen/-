package com.hl.rest.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hl.rest.service.IHomeworkService;
import com.hl.rest.service.IMemService;
import com.hl.rest.vo.Homework;
import com.hl.rest.vo.HomeworkNotice;
import com.hl.rest.vo.Member;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class HomeworkController {
	
	@Autowired
	private IHomeworkService ser;
	
	@Autowired
	private IMemService memser;
	
	@ExceptionHandler(Exception.class)
	public void ExceptionMethod(Exception e) {

	}
	
	/** 숙제 공지생성 */
	@PostMapping("/board/homework")
	@ApiOperation(value = "숙제 공지생성")
	public ResponseEntity<Map<String, Object>> CreateHomeworkNotice(
			@RequestHeader(value = "Authorization") String token,
			@RequestBody HomeworkNotice homework_notice) {
		ResponseEntity<Map<String, Object>> res = null;
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			Claims de = AuthController.verification(token);
			String email = (String) de.get("email");
			Member member = memser.getMem(email);
			homework_notice.setHomeworkNotice_memberIdx(member.getMemberIdx()+"");
			
			if(member.getIsteacher().equals("1")) {
				ser.CreateHomeworkNotice(homework_notice);
				msg.put("homework_notice", homework_notice);
				res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.OK);
			} else {
				msg.put("error", "학생은 권한이 없습니다.");
				res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.UNAUTHORIZED);
			}
		} catch(Exception e) {
			Object[] input = {token, homework_notice};
			msg.put("Input Data", input);
			msg.put("SAY", "Error msg를 참고하여 Input Data을 다시 한 번 확인해보세요.");
			msg.put("Error msg", e.getMessage());
			res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.BAD_REQUEST);
		}
		return res;
	}
	
	/** 숙제공지 수정 */
	@PutMapping("/board/homework/{homeworkNoticeIdx}")
	@ApiOperation(value = "숙제공지 수정")
	public ResponseEntity<Map<String, Object>> UpdateHomeworkNotice(
			@RequestHeader(value = "Authorization") String token,
			@PathVariable("homeworkNoticeIdx") String homeworkNoticeIdx,
			@RequestBody HomeworkNotice homework_notice) {
		ResponseEntity<Map<String, Object>> res = null;
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			Claims de = AuthController.verification(token);
			String email = (String) de.get("email");
			Member member = memser.getMem(email);
			
			if(member.getMemberIdx().equals(ser.getWhoseHomeworkNotice(homeworkNoticeIdx)+"")) {
				homework_notice.setHomeworkNotice_idx(homeworkNoticeIdx);
				ser.updateHomeworkNotice(homework_notice);
				msg.put("HomeworkNotice", homework_notice);
				res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.OK);
			} else {
				msg.put("error", "숙제공지에 대한 담당자가 아니거나 학생은 수정에 대한 권한이 없습니다.");
				res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.FORBIDDEN);
			}	
		}catch(Exception e) {
			Object[] input = {token, homework_notice, homeworkNoticeIdx};
			msg.put("Input Data", input);
			msg.put("SAY", "Error msg를 참고하여 Input Data을 다시 한 번 확인해보세요.");
			msg.put("Error msg", e.getMessage());
			res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.BAD_REQUEST);
		}
		return res;
	}
	
	/** 숙제공지 삭제 */
	@DeleteMapping("/board/homework/{homeworkNoticeIdx}")
	@ApiOperation(value = "숙제공지 삭제")
	public ResponseEntity<Map<String, Object>> DeleteHomeworkNotice(
			@RequestHeader(value = "Authorization") String token,
			@PathVariable("homeworkNoticeIdx") String homeworkNoticeIdx) {
		ResponseEntity<Map<String, Object>> res = null;
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			Claims de = AuthController.verification(token);
			String email = (String) de.get("email");
			Member member = memser.getMem(email);
			
			if(member.getMemberIdx().equals(ser.getWhoseHomeworkNotice(homeworkNoticeIdx)+"")) {
				ser.deleteHomeworkNotice(homeworkNoticeIdx);
				msg.put("msg", "해당 숙제공지가 삭제됐습니다.");
				res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.OK);
			} else {
				msg.put("error", "숙제공지에 대한 담당자가 아니거나 학생은 수정에 대한 권한이 없습니다.");
				res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.FORBIDDEN);
			}	
		}catch(Exception e) {
			Object[] input = {token, homeworkNoticeIdx};
			msg.put("Input Data", input);
			msg.put("SAY", "Error msg를 참고하여 Input Data을 다시 한 번 확인해보세요.");
			msg.put("Error msg", e.getMessage());
			res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.BAD_REQUEST);
		}
		return res;
	}
	
	/** 숙제 제출 */
	@PostMapping("/homework")
	@ApiOperation(value = "숙제 제출")
	public ResponseEntity<Map<String, Object>> CreateHomework(
			@RequestHeader(value = "Authorization") String token,
			@RequestBody Homework homework) {
		ResponseEntity<Map<String, Object>> res = null;
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			Claims de = AuthController.verification(token);
			String email = (String) de.get("email");
			Member member = memser.getMem(email);
			
			if(member.getIsteacher().equals("0")) {
				homework.setHomework_memberIdx(member.getMemberIdx());
				homework.setHomework_url("/home/homeworkImg/"+homework.getHomework_noticeIdx()+"/"+member.getMemberIdx()+".jpg");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); //HH:mm:ss
				TimeZone time = TimeZone.getTimeZone("Asia/Seoul");
				df.setTimeZone(time);
				String today = df.format(new Date());
				homework.setHomework_submitDate(today);
				
				ser.CreateHomework(homework);
				msg.put("Homework", homework);
				res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.OK);
			} else {
				msg.put("error", "선생님은 제출할 수 없습니다.");
				res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.FORBIDDEN);
			}
		}catch(Exception e) {
			Object[] input = {token, homework};
			msg.put("Input Data", input);
			msg.put("SAY", "Error msg를 참고하여 Input Data을 다시 한 번 확인해보세요.");
			msg.put("Error msg", e.getMessage());
			res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.BAD_REQUEST);
		}
		return res;
	}
	
	/** 숙제제출 삭제 */
	@DeleteMapping("/homework/{homeworkIdx}")
	@ApiOperation(value = "숙제 제출 삭제")
	public ResponseEntity<Map<String, Object>> DeleteHomework(
			@RequestHeader(value = "Authorization") String token,
			@PathVariable("homeworkIdx") String homeworkIdx) {
		ResponseEntity<Map<String, Object>> res = null;
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			Claims de = AuthController.verification(token);
			String email = (String) de.get("email");
			Member member = memser.getMem(email);
			
			//내 숙제만 삭제할 수 있음
			if(member.getMemberIdx().equals(ser.getWhoseHomework(homeworkIdx)+"")) {
				ser.deleteHomework(homeworkIdx);
				msg.put("msg", "과제가 삭제됐습니다.");
				res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.OK);
			} else {
				msg.put("error", "자신의 과제가 아니거나 권한이 없습니다.");
				res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.FORBIDDEN);
			}
		} catch(Exception e) {
			Object[] input = {token, homeworkIdx};
			msg.put("Input Data", input);
			msg.put("SAY", "Error msg를 참고하여 Input Data을 다시 한 번 확인해보세요.");
			msg.put("Error msg", e.getMessage());
			res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.BAD_REQUEST);
		}
		return res;
	}
	
	/** 숙제공지 전체목록 조회 */
	@GetMapping("/board/homeworks")
	@ApiOperation(value = "숙제공지 전체목록 조회")
	public ResponseEntity<Map<String, Object>> GetHomeworkNoticeList(
			@RequestHeader(value = "Authorization") String token) {
		ResponseEntity<Map<String, Object>> res = null;
		Map<String, Object> msg = new HashMap<String, Object>();
		
		try {
			Claims de = AuthController.verification(token);
			String email = (String) de.get("email");
			Member member = memser.getMem(email);
			
			List<HomeworkNotice> homeworknotice = ser.getHomeworkNoticeList();
			msg.put("HomeworkNoticeList", homeworknotice);
			res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.OK);
			
		} catch (Exception e) {
			msg.put("Input Data", token);
			msg.put("SAY", "Error msg를 참고하여 Input Data을 다시 한 번 확인해보세요.");
			msg.put("Error msg", e.getMessage());
			res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.BAD_REQUEST);
		}
		return res;
	}
	
	/** 숙제제출 전제목록 조회 */
	@GetMapping("/homeworks")
	@ApiOperation(value = "숙제제출 전체목록 조회")
	public ResponseEntity<Map<String, Object>> GetHomeworkList(
			@RequestHeader(value = "Authorization") String token) {
		ResponseEntity<Map<String, Object>> res = null;
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			Claims de = AuthController.verification(token);
			String email = (String) de.get("email");
			Member member = memser.getMem(email);
			List<Homework> homeworklist = null;
			
			if(member.getIsteacher().equals("1")) { 
				homeworklist = ser.getHomeworkList_teacher(member.getMemberIdx());
			} else { 
				homeworklist = ser.getHomeworkList_student(member.getMemberIdx());
			}
			
			System.out.println(homeworklist);
			if(homeworklist.size()==0) {
				msg.put("msg", "요청에 성공하였으나 응답할 콘텐츠가 없습니다.");
				res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.NO_CONTENT);
			} else {
				msg.put("HomeworkList", homeworklist);
				res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.OK);
			}
		} catch(Exception e) {
			msg.put("Input Data", token);
			msg.put("SAY", "Error msg를 참고하여 Input Data을 다시 한 번 확인해보세요.");
			msg.put("Error msg", e.getMessage());
			res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.BAD_REQUEST);
			System.out.println(e.getMessage());
		}
		return res;
	}
	
	
	/** 숙제제출 현황 조회 */
	@GetMapping("/homework/{homeworkNoticeIdx}")
	@ApiOperation(value = "숙제 제출현황 조회")
	public ResponseEntity<Map<String, Object>> GetHomework(
			@RequestHeader(value = "Authorization") String token,
			@PathVariable("homeworkNoticeIdx") String homeworkNoticeIdx) {
		ResponseEntity<Map<String, Object>> res = null;
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			Claims de = AuthController.verification(token);
			String email = (String) de.get("email");
			Member member = memser.getMem(email);
			
			List<Homework> homeworkList=null;
			if(member.getIsteacher().equals("1")) {
				String charge = ser.getWhoseHomeworkNotice(homeworkNoticeIdx)+"";
				if(member.getMemberIdx().equals(charge)) {
					homeworkList = ser.getHomeworkList_byIdx(homeworkNoticeIdx);
					if(homeworkList.size()!=0) {
						msg.put("HomeworkList", homeworkList);
						res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.OK);
					} else {
						res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.NO_CONTENT);
					}
				}
				else {
					msg.put("error", "해당 과제 담당자가 아니거나 권한이 없습니다.");
					res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.FORBIDDEN);
				}
			} else {
				homeworkList = ser.getHomeworkList_byIdx(homeworkNoticeIdx, member.getMemberIdx());
				if(homeworkList.size()==0) {
					res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.NO_CONTENT);
				} else {
					msg.put("HomeworkList", homeworkList);
					res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.OK);
				}
			}
		} catch(Exception e) {
			Object[] input = {token, homeworkNoticeIdx};
			msg.put("Input Data", input);
			msg.put("SAY", "Error msg를 참고하여 Input Data을 다시 한 번 확인해보세요.");
			msg.put("Error msg", e.getMessage());
			res = new ResponseEntity<Map<String, Object>>(msg, HttpStatus.BAD_REQUEST);
		}
		return res;
	}
	
}
