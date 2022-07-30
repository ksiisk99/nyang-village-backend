package com.ay.talk.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.ay.talk.dto.Msg;
import com.ay.talk.dto.request.ReqConnectChat;
import com.ay.talk.dto.response.ResConnectChat;
import com.ay.talk.entity.ChatMsg;
import com.ay.talk.entity.User;
import com.ay.talk.entity.UserRoomData;
import com.ay.talk.repository.DbRepository;
import com.ay.talk.repository.ServerRepository;
import com.ay.talk.service.ChatService;

@Service
public class ChatServiceImpl implements ChatService{
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final ServerRepository serverRepository;
	private final DbRepository dbRepository;
	private final int doubleLoginSignal=1; //이중로그인 신호 1
	private final int suspendedSignal=2; //정지회원 신호 2
	private final int semeterSignal=3; //새학기 신호 3
	private final int updateSignal=4; //업데이트 신호 4
	private final int sendMsgSignal=2; //메시지 전송 신호 2
	
	@Autowired
	public ChatServiceImpl(SimpMessagingTemplate simpMessagingTemplate, ServerRepository serverRepository
			,DbRepository dbRepository) {
		this.simpMessagingTemplate=simpMessagingTemplate;
		this.serverRepository=serverRepository;
		this.dbRepository=dbRepository;
	}
	
	//입장 퇴장 메시지 전송
	@Override
	public void enterExitMsg(int type, int roomId, String nickName) { 
		//type 0:입장 / 1:퇴장 / 2:메시지
		Msg msg=new Msg(String.valueOf(roomId),nickName, type);
		simpMessagingTemplate.convertAndSend("/sub/chat/"+msg.getRoomId(),msg);
	}
	
	//채팅 메시지 전송
	@Override
	public void sendMsg(Msg msg) { 
		//type 0:입장 / 1:퇴장 / 2:메시지
		msg.setType(sendMsgSignal);
		simpMessagingTemplate.convertAndSend("/sub/chat/"+msg.getRoomId(),msg);
		dbRepository.insertChatMsg(new ChatMsg(msg.getRoomId(), msg.getNickName(),msg.getContent(),msg.getTime()));
	}
	
	//pc 채팅 메시지 전송
	@Override
	public void sendPcMsg(Msg msg) {
		// TODO Auto-generated method stub
		msg.setpmType(); //pc 보낸 신호 1
		msg.setType(sendMsgSignal);
		simpMessagingTemplate.convertAndSend("/sub/chat/"+msg.getRoomId(),msg);
		dbRepository.insertChatMsg(new ChatMsg(msg.getRoomId(), msg.getNickName(),msg.getContent(),msg.getTime()));
	}
	
	//첫 웹소켓 연결 시 버전, 이중로그인, 정지유무, 새학기인지를 확인한다.
	@Override
	public void connectChat(ReqConnectChat cc) {
		//자동로그인인데 토큰 값이 달라졌다면 다른 기기에서 로그인 했다는 것이니 현재 기기를 로그아웃 시킨다.
		//System.out.println(cc.getStudentId()+" "+cc.getToken());
		if(cc.getVersion()!=serverRepository.getVersion()) { //앱 버전이 다르므로 클라이언트 업데이트
			ResConnectChat resConnectChat=new ResConnectChat(updateSignal, null,cc.getStudentId()); //앱 버전 업데이트 신호
			simpMessagingTemplate.convertAndSend("/sub/chat/"+cc.getRoomId(),resConnectChat);
			return;
		}
		
		String userInfo=serverRepository.getUserInfo(cc.getStudentId());
		
		if(userInfo==null){ //새학기 시작이라 해당하는 학번에 토큰값이 없다면 이전 학기에서 새롭게 로그인을 안한 유저이다.
			ResConnectChat resConnectChat=new ResConnectChat(semeterSignal, null,cc.getStudentId()); //3 새학기 신호
			simpMessagingTemplate.convertAndSend("/sub/chat/"+cc.getRoomId(),resConnectChat);
			return;
		}
		String[] userInfos=userInfo.split(",");
		if(!userInfos[0].equals(cc.getToken())){//이중로그인 이라면
			ResConnectChat resConnectChat=new ResConnectChat(doubleLoginSignal, null,cc.getStudentId()); //1 이중로그인 신호
			simpMessagingTemplate.convertAndSend("/sub/chat/"+cc.getRoomId(),resConnectChat);
		}else{
			if(!userInfos[1].equals("0")) { //정지 회원이라면
				//현재날짜와 정지날짜를 비교한다
				if(Integer.parseInt(userInfos[1])>=Integer.parseInt(getCurrentTime())) { //정지회원
					ResConnectChat resConnectChat=new ResConnectChat(suspendedSignal, userInfos[1],cc.getStudentId()); //2 정지 회원
					simpMessagingTemplate.convertAndSend("/sub/chat/"+cc.getRoomId(),resConnectChat);
					
					//모든 방들에 대해서 토큰 값들을 제거한다. 정지유저에게 메시지가 안가게 된다.
					User user=dbRepository.findUser(cc.getStudentId());
					ArrayList<UserRoomData>curSubjects=user.getRoomIds();
					for(int i=0;i<curSubjects.size();i++) {
						serverRepository.removeRoomInToken(curSubjects.get(i).getRoomName(), cc.getToken());
					}
					//System.out.println("정지 회원");
				}else { //정지가 풀린 회원
					serverRepository.removeSuspendedUser(cc.getStudentId()
							,new StringBuilder().append(cc.getToken()+",0").toString());
					dbRepository.removeSuspendedUser(cc.getStudentId());
				}
			}
		}
	}
	
	//현재날짜
    private String getCurrentTime() {
        long now = System.currentTimeMillis();
        TimeZone tz=TimeZone.getTimeZone("Asia/Seoul");
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.KOREA);
        dateFormat.setTimeZone(tz);
        String getTim = dateFormat.format(date);
        return getTim;
    }
}
