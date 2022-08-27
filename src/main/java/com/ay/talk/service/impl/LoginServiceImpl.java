package com.ay.talk.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import org.springframework.stereotype.Service;

import com.ay.talk.dto.RoomInfo;
import com.ay.talk.dto.SubjectInfo;
import com.ay.talk.dto.request.ReqLogin;
import com.ay.talk.dto.request.ReqLogout;
import com.ay.talk.dto.response.ResLogin;
import com.ay.talk.dto.response.ResLogout;
import com.ay.talk.jpaentity.Authority;
import com.ay.talk.jpaentity.User;
import com.ay.talk.jpaentity.UserRoomInfo;
import com.ay.talk.jparepository.SuspendedRepository;
import com.ay.talk.jparepository.UserRepository;
import com.ay.talk.jparepository.UserRoomInfoRepository;
import com.ay.talk.jwt.JwtTokenProvider;
import com.ay.talk.repository.ServerRepository;
import com.ay.talk.service.ChatService;
import com.ay.talk.service.FcmService;
import com.ay.talk.service.LoginService;
import com.google.firebase.messaging.FirebaseMessagingException;

@Service
@PropertySource("classpath:application.properties")
public class LoginServiceImpl implements LoginService{
	@Value("${crawling.path}")
	private String crawlingPath;
	@Value("${crawling.path2}")
	private String crawlingPath2;
	private final ServerRepository serverRepository; //서버 인 메모리 변수
	private final FcmService fcmService;
	private final ChatService chatService;
	private final JwtTokenProvider jwtTokenProvider; //토큰 발급
	private final int updateSignal=1; //업데이트 신호
	private final int firstLoginSignal=3; //처음 로그인 신호 3
	private final int reviseSubjectSignal=4; //수강정정한 회원
	private final int doubleLoginSignal=4; //이중로그인 신호 수강정정과 클라이언트에서 처리하는 로직이 같음
	private final int misspellSignal=5; //아이디 비밀번호가 잘못 입력됨 신호 5
	private final int suspendedSignal=6; //정지회원 신호 6
//	private final int pcNotMobileLoginSignal=2; //pc에서 로그인 할 때 모바일에서 로그인이 안되어 있으면 신호 2
//	private final int pcSuspendedUserLoginSignal=3; //pc에서 로그인 할 때 정지회원이라면 신호3
//	private final int pcSuccessLoginSignal=1; //pc로그인 성공 신호 1
//	private final int pcFailLoginSignal=4; //pc 아이디 비밀번호 잘못 입력됨 신호 4
	private final int enterMsgSignal=0; //입장 신호 0
	private final int exitMsgSignal=1; //퇴장 신호 1
	//private final int failLogoutSignal=0; //로그아웃 실패 신호 0
	private final int successLogoutSignal=1; //로그아웃 성공 신호 1
	
	private final UserRepository userRepository;
	private final SuspendedRepository suspendedRepository;
	private final UserRoomInfoRepository userRoomInfoRepository;
	private Logger logger; //로그
	
	@Autowired
	public LoginServiceImpl(ServerRepository serverRepository
			,FcmService fcmService, ChatService chatService, JwtTokenProvider jwtTokenProvider
			,UserRepository userRepository, SuspendedRepository suspendedRepository
			,UserRoomInfoRepository userRoomInfoRepository) {
		this.serverRepository=serverRepository;
		this.fcmService=fcmService;
		this.chatService=chatService;
		this.jwtTokenProvider=jwtTokenProvider;
		this.suspendedRepository=suspendedRepository;
		this.userRepository=userRepository;
		this.userRoomInfoRepository=userRoomInfoRepository;
		logger=LoggerFactory.getLogger(this.getClass());
	}
	

	@Override
	public ResLogin login(ReqLogin reqLogin) throws FirebaseMessagingException, IOException {
		ResLogin resLogin=new ResLogin();
		//logger.info("studentId: {}",reqLogin.getStudentId());
		
		if(reqLogin.getVersion()!=serverRepository.getVersion()) { //버전이 다르면 업데이트 신호 전달
			resLogin.setSignal(updateSignal); //업데이트 신호 1
			return resLogin;
		}
		
		String studentId=reqLogin.getStudentId();
		String password=reqLogin.getPassword();
				
		ArrayList<SubjectInfo>userSubjects=Crawling(studentId, password); //크롤링 정보 가져오기
		if(userSubjects==null) { //아이디 비밀번호가 잘못 입력됨 신호 5
			resLogin.setSignal(misspellSignal);
			return resLogin;
		}
		String tmp=serverRepository.getUserInfo(studentId);
		String[] userInfo=null;
		if(tmp!=null)
			userInfo=serverRepository.getUserInfo(studentId).split(","); //0:fcm , 1:suspendedPriod
		
		if(userInfo!=null && !userInfo[1].equals("0")) { //정지 회원이면 정지 기간 전달
			//현재날짜와 정지날짜 비교후 정지가 아닐때와 정지일때 처리
			if(Integer.parseInt(userInfo[1])>=Integer.parseInt(getCurrentTime())) { //정지 회원
				resLogin.setSuspendedDate(userInfo[1]);
				resLogin.setSignal(suspendedSignal); //정지 회원 신호 6
				return resLogin;
			}else {//정지가 풀린 회원
				serverRepository.removeSuspendedUser(studentId
						,new StringBuilder().append(reqLogin.getFcm()+",0").toString());
				suspendedRepository.deleteById(studentId);
				//dbRepository.removeSuspendedUser(studentId);
			}
		}
		//long beforeTime = System.currentTimeMillis();
		String jwt=null;
		
		if(userInfo==null) { //처음 로그인한 회원
			resLogin.setSignal(firstLoginSignal); //처음 로그인 신호 3
			User user=new User(studentId, reqLogin.getFcm(), getCurrentTime(),Authority.Customer);
			ArrayList<UserRoomInfo> userRoomInfos=new ArrayList<UserRoomInfo>(); //방에 대한 사용자 정보(닉네임,방아이디,방이름)
			ArrayList<SubjectInfo> curSubjects=userSubjects; //사용자의 과목 정보를 임시 변수에 저장
			ArrayList<RoomInfo> roomInfos=new ArrayList<RoomInfo>(); //response하기 위한 과목별 방안에 유저 닉네임

			for(int i=0;i<curSubjects.size();i++) { //사용자의 모든 수강 과목 정보
				String subjectName=curSubjects.get(i).getSubjectName();
				for(int j=serverRepository.getStartRandomNickNameIdx(subjectName);j<200;j++) { //각 방에 대한 미사용 랜덤닉네임 찾기 
					if(serverRepository.setCheckRoomName(subjectName, j)) { //미사용중인 랜덤닉네임을 먼저 사용중으로 check한다.
						UserRoomInfo userRoomInfo=new UserRoomInfo(serverRepository.getRandomNickName(j),
								serverRepository.getRoomId(subjectName), subjectName,curSubjects.get(i).getProfessorName());
						user.addUserRoomInfo(userRoomInfo);
						userRoomInfos.add(userRoomInfo); //db에 저장 시키기 위해 방 정보 추가
						
								
						//fcm 입장 메시지 전송
						fcmService.enterExitMsg(enterMsgSignal,serverRepository.getRoomId(subjectName),serverRepository.getRandomNickName(j));
					
						//웹 소켓 입장 메시지 전송
						chatService.enterExitMsg(enterMsgSignal,serverRepository.getRoomId(subjectName)
								,serverRepository.getRandomNickName(j)); 
						serverRepository.addRoomInName(subjectName,j); //방 안에 사용자 닉네임 추가
						serverRepository.addRoomInToken(subjectName,reqLogin.getFcm()); //방 안에 사용자 토큰 추가
						RoomInfo roomInfo=new RoomInfo(subjectName,
								serverRepository.getRoomId(subjectName),
								serverRepository.getRandomNickName(j),curSubjects.get(i).getProfessorName(),
								(ArrayList<String>)serverRepository.getRoomInNames(subjectName));
						roomInfos.add(roomInfo); //response할 방 정보 추가
						break;
					}
				}
			}
			userRepository.save(user);
			resLogin.setRoomInfos(roomInfos);
			serverRepository.addUserInfo(studentId,
					new StringBuilder().append(reqLogin.getFcm()+",0").toString()); //학번 토큰 등록
			jwt=jwtTokenProvider.createToken(studentId, Authority.Customer); //jwt 생성
		}else if(userInfo[0].equals(reqLogin.getFcm())) { //같은 기기로 로그아웃하고 로그인한 회원
			resLogin.setSignal(reviseSubjectSignal); //같은 기기 로그아웃하고 로그인한 회원 신호4
			ArrayList<SubjectInfo> curSubjects=userSubjects; //사용자의 과목 정보를 임시 변수에 저장
			ArrayList<RoomInfo> roomInfos=new ArrayList<RoomInfo>(); //response하기 위한 과목별 방안에 유저 닉네임
			int[] checkRoom=new int[curSubjects.size()]; //새롭게 듣는 과목 인덱스 기존 방은 유지 시켜야하기 때문이다.
			int cnt=0; //이전 과목과 겹치지 않는 새로 듣는 과목 수
			ArrayList<UserRoomInfo> resultSubjects=new ArrayList<UserRoomInfo>(); //최종 수강과목 db 저장용
			//User user=dbRepository.findUser(studentId); //db에 저장된 유저 정보를 불러온다
			User user=userRepository.findByStudentId(studentId).get();
			
			List<UserRoomInfo> beforeSubjects=user.getUserRoomInfos(); //바뀌기 전 수강 과목
			boolean[] overlap=new boolean[beforeSubjects.size()]; //이전과 현재 수강과목 중 겹치는 부분 체크
			
			int curSize=curSubjects.size();
			int beforeSize=beforeSubjects.size();
			for(int i=0;i<curSize;i++) { //현재 수강과목
				for(int j=0;j<beforeSize;j++) { //바뀌기 전 수강과목
					if(beforeSubjects.get(j).getRoomName().equals(curSubjects.get(i).getSubjectName())) { //바뀌기 전 수강과목과 기존의 수강과목이 동일하다면
						UserRoomInfo userRoomInfo=new UserRoomInfo(beforeSubjects.get(j).getNickName(),
								beforeSubjects.get(j).getRoomId(), beforeSubjects.get(j).getRoomName(),curSubjects.get(i).getProfessorName());
						userRoomInfo.makeFk(user);
						resultSubjects.add(userRoomInfo);
						overlap[j]=true; //겹치는 수강과목 인덱스 체크 겹치지 않는 기존의 이전 방들을 나가기 위함이다.
						RoomInfo roomInfo=new RoomInfo(beforeSubjects.get(j).getRoomName()
								,beforeSubjects.get(j).getRoomId(),beforeSubjects.get(j).getNickName()
								,curSubjects.get(i).getProfessorName()
								,(ArrayList<String>)serverRepository.getRoomInNames(beforeSubjects.get(j).getRoomName()));
						roomInfos.add(roomInfo);
						break;
					}
					
					if(j==(beforeSize-1)) { //다 비교할때까지 겹치는 과목이 없다면 그 과목은 새로 듣는 과목임
						String subjectName=curSubjects.get(i).getSubjectName();
						for(int k=serverRepository.getStartRandomNickNameIdx(subjectName);k<200;k++){
							if(serverRepository.setCheckRoomName(subjectName, k)) { //해당 과목에 랜덤닉네임 인덱스에 해당하는 이름을 사용하는 사용자가 없다면
								UserRoomInfo userRoomData=new UserRoomInfo(serverRepository.getRandomNickName(k)
										,serverRepository.getRoomId(subjectName), subjectName,curSubjects.get(i).getProfessorName());
								resultSubjects.add(userRoomData);
								checkRoom[cnt++]=resultSubjects.size()-1; //새로 듣는 과목 현재 과목 인덱스 번호 저장 나중에 입장 메시지를 보내기 위함
								break;
							}
						}
					}
				}
			}
			
			user.changeUserRoomInfos(resultSubjects);
			userRoomInfoRepository.deleteUserRoomInfos(user); //기존 방 정보 삭제
			userRepository.save(user); //유저의 방 정보를 db에 업데이트 한다
			
			for(int i=0;i<beforeSize;i++) { //기존 과목중에서 겹치지 않는 과목에 있어서 퇴장메시지를 보낸다.
				if(overlap[i])continue; //겹치는 과목은 방을 유지한다.
				serverRepository.removeRoomInName(beforeSubjects.get(i).getRoomName(),
						beforeSubjects.get(i).getNickName());
				serverRepository.removeRoomInToken(beforeSubjects.get(i).getRoomName(),
						reqLogin.getFcm());
				//퇴장 메시지 전송
				fcmService.enterExitMsg(exitMsgSignal, beforeSubjects.get(i).getRoomId(),beforeSubjects.get(i).getNickName());
				chatService.enterExitMsg(exitMsgSignal, beforeSubjects.get(i).getRoomId(), beforeSubjects.get(i).getNickName());
			}	
			for(int i=0;i<cnt;i++) { //현재 과목중에서 겹치지 않는 새로운 과목에 있어서 입장메시지를 보낸다.
				fcmService.enterExitMsg(enterMsgSignal,resultSubjects.get(checkRoom[i]).getRoomId(),
						resultSubjects.get(checkRoom[i]).getNickName());
				chatService.enterExitMsg(enterMsgSignal,resultSubjects.get(checkRoom[i]).getRoomId(),
						resultSubjects.get(checkRoom[i]).getNickName());
				serverRepository.addRoomInName(resultSubjects.get(checkRoom[i]).getRoomName(),
						resultSubjects.get(checkRoom[i]).getNickName());
				serverRepository.addRoomInToken(resultSubjects.get(checkRoom[i]).getRoomName(),
						reqLogin.getFcm());
				
				RoomInfo roomInfo=new RoomInfo(resultSubjects.get(checkRoom[i]).getRoomName()
						,resultSubjects.get(checkRoom[i]).getRoomId()
						,resultSubjects.get(checkRoom[i]).getNickName()
						,resultSubjects.get(checkRoom[i]).getProfessorName()
						,(ArrayList<String>)serverRepository.getRoomInNames(resultSubjects.get(checkRoom[i]).getRoomName()));
				roomInfos.add(roomInfo);
			}
			resLogin.setRoomInfos(roomInfos);
			jwt=jwtTokenProvider.createToken(studentId, user.getAuthority()); //jwt 생성
		} else { //다른 기기로 로그인하거나 삭제하고 다시 깐 회원(토큰이 달라짐)
			resLogin.setSignal(doubleLoginSignal); //다른 기기로 로그인한 회원 신호4
			User user=userRepository.findByStudentId(studentId).get();
			ArrayList<SubjectInfo> curSubjects=userSubjects; //사용자의 과목 정보를 임시 변수에 저장
			List<UserRoomInfo> beforeSubjects=user.getUserRoomInfos(); //이전 수강과목
			
			ArrayList<RoomInfo> roomInfos=new ArrayList<RoomInfo>(); //response하기 위한 과목별 방안에 유저 닉네임
			int[] checkRoom=new int[curSubjects.size()]; //새롭게 듣는 과목 인덱스 기존 방은 유지 시켜야하기 때문이다.
			int cnt=0; //이전 과목과 겹치지 않는 새로 듣는 과목 수
			ArrayList<UserRoomInfo> resultSubjects=new ArrayList<UserRoomInfo>(); //최종 수강과목 db 저장용
			boolean[] overlap=new boolean[beforeSubjects.size()]; //이전과 현재 수강과목 중 겹치는 부분 체크
			
			int curSize=curSubjects.size(); //현재 수강과목 수
			int beforeSize=beforeSubjects.size(); //이전 수강과목 수
						
			//이전 수강과목에 대해 모든 토큰 값을 삭제한다.
			for(int i=0;i<beforeSubjects.size();i++) {
				serverRepository.removeRoomInToken(beforeSubjects.get(i).getRoomName(), user.getFcm());
			}
			
			for(int i=0;i<curSize;i++) { //현재 수강과목
				for(int j=0;j<beforeSize;j++) { //바뀌기 전 수강과목
					if(beforeSubjects.get(j).getRoomName().equals(curSubjects.get(i).getSubjectName())) { //바뀌기 전 수강과목과 기존의 수강과목이 동일하다면
						UserRoomInfo userRoomInfo=new UserRoomInfo(beforeSubjects.get(j).getNickName(),
								beforeSubjects.get(j).getRoomId(), beforeSubjects.get(j).getRoomName(),beforeSubjects.get(i).getProfessorName());
						userRoomInfo.makeFk(user);
						resultSubjects.add(userRoomInfo);
						overlap[j]=true; //겹치는 수강과목 인덱스 체크 겹치지 않는 기존의 이전 방들을 나가기 위함이다.
						RoomInfo roomInfo=new RoomInfo(beforeSubjects.get(j).getRoomName()
								,beforeSubjects.get(j).getRoomId()
								,beforeSubjects.get(j).getNickName()
								,beforeSubjects.get(i).getProfessorName()
								,(ArrayList<String>)serverRepository.getRoomInNames(beforeSubjects.get(j).getRoomName()));
						roomInfos.add(roomInfo);
						break;
					}
					
					if(j==(beforeSize-1)) { //다 비교할때까지 겹치는 과목이 없다면 그 과목은 새로 듣는 과목임
						String subjectName=curSubjects.get(i).getSubjectName();
						for(int k=serverRepository.getStartRandomNickNameIdx(subjectName);k<200;k++){
							if(serverRepository.setCheckRoomName(subjectName, k)) {//해당 과목에 랜덤닉네임 인덱스에 해당하는 이름을 사용하는 사용자가 없다면
								UserRoomInfo userRoomInfo=new UserRoomInfo(serverRepository.getRandomNickName(k)
										,serverRepository.getRoomId(subjectName), subjectName,curSubjects.get(i).getProfessorName());
								userRoomInfo.makeFk(user);
								resultSubjects.add(userRoomInfo);
								checkRoom[cnt++]=resultSubjects.size()-1; //새로 듣는 과목 현재 과목 인덱스 번호 저장 나중에 입장 메시지를 보내기 위함
								break;
							}
						}
					}
				}
			}
			
			user.changeUserRoomInfos(resultSubjects);	//유저 정보 방과 fcm을 db에 업데이트 한다
			user.changeFcm(reqLogin.getFcm());
			userRoomInfoRepository.deleteUserRoomInfos(user);
			userRepository.save(user);
			
			for(int i=0;i<beforeSize;i++) { //기존 과목중에서 겹치지 않는 과목에 있어서 퇴장메시지를 보낸다.
				//겹치는 과목은 방을 유지하고 바뀐 토큰 값을 넣어준다.
				if(overlap[i]) {
					serverRepository.addRoomInToken(beforeSubjects.get(i).getRoomName(), reqLogin.getFcm());
					continue; 
				}
				serverRepository.removeRoomInName(beforeSubjects.get(i).getRoomName(),
						beforeSubjects.get(i).getNickName());
			
				//퇴장 메시지 전송
				fcmService.enterExitMsg(exitMsgSignal, beforeSubjects.get(i).getRoomId(),beforeSubjects.get(i).getNickName());
				chatService.enterExitMsg(exitMsgSignal, beforeSubjects.get(i).getRoomId(), beforeSubjects.get(i).getNickName());
			}			
			for(int i=0;i<cnt;i++) { //현재 과목중에서 겹치지 않는 새로운 과목에 있어서 입장메시지를 보낸다.
				fcmService.enterExitMsg(enterMsgSignal,resultSubjects.get(checkRoom[i]).getRoomId(),
						resultSubjects.get(checkRoom[i]).getNickName());
				chatService.enterExitMsg(enterMsgSignal,resultSubjects.get(checkRoom[i]).getRoomId(),
						resultSubjects.get(checkRoom[i]).getNickName());
				
				serverRepository.addRoomInName(resultSubjects.get(checkRoom[i]).getRoomName(),
						resultSubjects.get(checkRoom[i]).getNickName());
				serverRepository.addRoomInToken(resultSubjects.get(checkRoom[i]).getRoomName(),
						reqLogin.getFcm());
				
				RoomInfo roomInfo=new RoomInfo(resultSubjects.get(checkRoom[i]).getRoomName()
						,resultSubjects.get(checkRoom[i]).getRoomId()
						,resultSubjects.get(checkRoom[i]).getNickName()
						,resultSubjects.get(checkRoom[i]).getProfessorName()
						,(ArrayList<String>)serverRepository.getRoomInNames(resultSubjects.get(checkRoom[i]).getRoomName()));
				roomInfos.add(roomInfo);
			}
			resLogin.setRoomInfos(roomInfos);
			serverRepository.addUserInfo(studentId,
					new StringBuilder().append(reqLogin.getFcm()+",0").toString());
			jwt=jwtTokenProvider.createToken(studentId, user.getAuthority()); //jwt 생성
		}
		
		resLogin.setJwt(jwt);
		return resLogin;
	}
	
	@Override
	public ResLogout logout(ReqLogout reqLogout) {
		// TODO Auto-generated method stub
		ResLogout resLogout=new ResLogout();
		String userInfo=serverRepository.getUserInfo(reqLogout.getStudentId());
		if(userInfo==null) { //새학기 시작
			resLogout.setSignal(successLogoutSignal);
			return resLogout;
		}
		if(!userInfo.split(",")[0].equals(reqLogout.getFcm())) { //이중로그인 이므로 로그아웃만 시킨다.
			resLogout.setSignal(successLogoutSignal);
			return resLogout;
		}
		
		User user=userRepository.findByStudentId(reqLogout.getStudentId()).get();
		List<UserRoomInfo> subjects=user.getUserRoomInfos();
		for(int i=0;i<subjects.size();i++) {
			serverRepository.removeRoomInToken(subjects.get(i).getRoomName(), user.getFcm()); //방에 속한 파베토큰 삭제
		}
		resLogout.setSignal(successLogoutSignal);
		return resLogout;
	}

	@Override
	public ArrayList<SubjectInfo> Crawling(String studentId, String password) throws IOException{
		Document html;
        Connection.Response loginPageResponse=null;
        Connection.Response response=null;
        loginPageResponse = Jsoup.connect(crawlingPath)
                .timeout(5000)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header("Upgrade-Insecure-Requests", "1")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36")
                .header("Aceept-Encoding","gzip, deflate")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Content-Type", "Content-Type: application/x-www-form-urlencoded")
                .method(Connection.Method.GET)
                .execute();

        Map<String, String>loginTryCookie = loginPageResponse.cookies();
        Map<String, String> data=new HashMap<>();

        data.put("cmd", "loginUser");
        data.put("userDTO.userId", studentId);
        data.put("userDTO.password", password);
        data.put("userDTO.localeKey", "ko");

        response = Jsoup.connect(crawlingPath2)
                .timeout(5000)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Upgrade-Insecure-Requests", "1")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.74 Safari/537.36")
                .cookies(loginTryCookie)
                .data(data)
                .method(Connection.Method.POST)
                .execute();

        html=Jsoup.parse(response.body()); //수강과목 파싱

        Elements crawlingList=html.select(".class_sbox select>option");
        if(crawlingList.size()>0){ //수강과목이 있으니 현재 재학생임        	
        	ArrayList<SubjectInfo> subjectList=new ArrayList<>();
            for(int i=1;i< crawlingList.size();i++){
//            	String attr=crawlingList.get(i).attr("value");
//            	String[] attrValue=attr.split(",");
            	//본학기
            	subjectList.add(new SubjectInfo(crawlingList.get(i).text().toString()
            			,serverRepository.getProfessorName(crawlingList.get(i).text().toString())));
            	//계절학기용
            	//subjectList.add(new SubjectInfo(crawlingList.get(i).text().toString()+" "+attrValue[1], attrValue[1])); //계절학기용
            }
            return subjectList;
        }
        return null;
	}
	
	//PC로그인
//	@Override
//	public ResPcLogin pcLogin(ReqPcLogin reqPcLogin) throws IOException {
//		// TODO Auto-generated method stub
//		ResPcLogin resPcLogin=new ResPcLogin();
//		String userInfo=serverRepository.getUserInfo(reqPcLogin.getStudentId());
//		if(userInfo==null) { //모바일에서 로그인 안 한 유저라면
//			resPcLogin.setSignal(pcNotMobileLoginSignal);
//			return resPcLogin;
//		}else { //모바일에서 로그인 한 유저라면
//			String suspendedPriod=userInfo.split(",")[1];
//			if(!suspendedPriod.equals("0")) { //정지 회원이라면
//				resPcLogin.setSignal(pcSuspendedUserLoginSignal);
//				resPcLogin.setSuspendedDate(suspendedPriod);
//				return resPcLogin;
//			}else {
//				pcCrawling(reqPcLogin, resPcLogin);
//			}
//		}
//		return resPcLogin;
//	}
	
//	@Override
//	public void pcCrawling(ReqPcLogin reqPcLogin, ResPcLogin resPcLogin) throws IOException {
//		// TODO Auto-generated method stub
//		Document html;
//        Connection.Response loginPageResponse=null;
//        Connection.Response response=null;
//        loginPageResponse = Jsoup.connect(crawlingPath)
//                .timeout(5000)
//                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
//                .header("Upgrade-Insecure-Requests", "1")
//                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36")
//                .header("Aceept-Encoding","gzip, deflate")
//                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
//                .header("Content-Type", "Content-Type: application/x-www-form-urlencoded")
//                .method(Connection.Method.GET)
//                .execute();
//
//        Map<String, String>loginTryCookie = loginPageResponse.cookies();
//        Map<String, String> data=new HashMap<>();
//
//        data.put("cmd", "loginUser");
//        data.put("userDTO.userId", reqPcLogin.getStudentId());
//        data.put("userDTO.password", reqPcLogin.getPassword());
//        data.put("userDTO.localeKey", "ko");
//
//        response = Jsoup.connect(crawlingPath2)
//                .timeout(5000)
//                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
//                .header("Accept-Encoding", "gzip, deflate")
//                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
//                .header("Upgrade-Insecure-Requests", "1")
//                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.74 Safari/537.36")
//                .cookies(loginTryCookie)
//                .data(data)
//                .method(Connection.Method.POST)
//                .execute();
//        html=Jsoup.parse(response.body()); //수강과목 파싱
//
//        Elements crawlingList=html.select(".class_sbox select>option");
//        if(crawlingList.size()>1){ //수강과목이 있으니 현재 재학생임
//        	User user=userRepository.findByStudentId(reqPcLogin.getStudentId()).get(); //모바일로 로그인한 db에 저장된 유저 정보를 불러온다
//        	
//        	List<UserRoomInfo> userRoomInfos=user.getUserRoomInfos();
//			List<RoomInfo> roomInfos=new ArrayList<RoomInfo>();
//			
//			for(int i=0;i<userRoomInfos.size();i++) {
//				RoomInfo roomInfo=new RoomInfo(userRoomInfos.get(i).getRoomName()
//						,userRoomInfos.get(i).getRoomId()
//						,userRoomInfos.get(i).getNickName()
//						,userRoomInfos.get(i).getProfessorName()
//						,(ArrayList<String>)serverRepository.getRoomInNames(userRoomInfos.get(i).getRoomName()));
//				roomInfos.add(roomInfo);
//			}
//			resPcLogin.setSignal(pcSuccessLoginSignal);
//			resPcLogin.setAuthority(user.getAuthority());
//			resPcLogin.setJwt(jwtTokenProvider.createToken(reqPcLogin.getStudentId(), user.getAuthority()));
//			resPcLogin.setRoomInfos(roomInfos);
//        }else {
//        	resPcLogin.setSignal(pcFailLoginSignal); //비밀번호가 잘못 입력됨
//        }
//	}

	//현재날짜 구하는 함수
    private String getCurrentTime() {
        long now = System.currentTimeMillis();
        TimeZone tz=TimeZone.getTimeZone("Asia/Seoul");
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.KOREA);
        dateFormat.setTimeZone(tz);
        return dateFormat.format(date);
    }
	

    
    
}