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
	private final int doubleLoginSignal=1; //���߷α��� ��ȣ 1
	private final int suspendedSignal=2; //����ȸ�� ��ȣ 2
	private final int semeterSignal=3; //���б� ��ȣ 3
	private final int updateSignal=4; //������Ʈ ��ȣ 4
	private final int sendMsgSignal=2; //�޽��� ���� ��ȣ 2
	
	@Autowired
	public ChatServiceImpl(SimpMessagingTemplate simpMessagingTemplate, ServerRepository serverRepository
			,DbRepository dbRepository) {
		this.simpMessagingTemplate=simpMessagingTemplate;
		this.serverRepository=serverRepository;
		this.dbRepository=dbRepository;
	}
	
	//���� ���� �޽��� ����
	@Override
	public void enterExitMsg(int type, int roomId, String nickName) { 
		//type 0:���� / 1:���� / 2:�޽���
		Msg msg=new Msg(String.valueOf(roomId),nickName, type);
		simpMessagingTemplate.convertAndSend("/sub/chat/"+msg.getRoomId(),msg);
	}
	
	//ä�� �޽��� ����
	@Override
	public void sendMsg(Msg msg) { 
		//type 0:���� / 1:���� / 2:�޽���
		msg.setType(sendMsgSignal);
		simpMessagingTemplate.convertAndSend("/sub/chat/"+msg.getRoomId(),msg);
		dbRepository.insertChatMsg(new ChatMsg(msg.getRoomId(), msg.getNickName(),msg.getContent(),msg.getTime()));
	}
	
	//pc ä�� �޽��� ����
	@Override
	public void sendPcMsg(Msg msg) {
		// TODO Auto-generated method stub
		msg.setpmType(); //pc ���� ��ȣ 1
		msg.setType(sendMsgSignal);
		simpMessagingTemplate.convertAndSend("/sub/chat/"+msg.getRoomId(),msg);
		dbRepository.insertChatMsg(new ChatMsg(msg.getRoomId(), msg.getNickName(),msg.getContent(),msg.getTime()));
	}
	
	//ù ������ ���� �� ����, ���߷α���, ��������, ���б������� Ȯ���Ѵ�.
	@Override
	public void connectChat(ReqConnectChat cc) {
		//�ڵ��α����ε� ��ū ���� �޶����ٸ� �ٸ� ��⿡�� �α��� �ߴٴ� ���̴� ���� ��⸦ �α׾ƿ� ��Ų��.
		//System.out.println(cc.getStudentId()+" "+cc.getToken());
		if(cc.getVersion()!=serverRepository.getVersion()) { //�� ������ �ٸ��Ƿ� Ŭ���̾�Ʈ ������Ʈ
			ResConnectChat resConnectChat=new ResConnectChat(updateSignal, null,cc.getStudentId()); //�� ���� ������Ʈ ��ȣ
			simpMessagingTemplate.convertAndSend("/sub/chat/"+cc.getRoomId(),resConnectChat);
			return;
		}
		
		String userInfo=serverRepository.getUserInfo(cc.getStudentId());
		
		if(userInfo==null){ //���б� �����̶� �ش��ϴ� �й��� ��ū���� ���ٸ� ���� �б⿡�� ���Ӱ� �α����� ���� �����̴�.
			ResConnectChat resConnectChat=new ResConnectChat(semeterSignal, null,cc.getStudentId()); //3 ���б� ��ȣ
			simpMessagingTemplate.convertAndSend("/sub/chat/"+cc.getRoomId(),resConnectChat);
			return;
		}
		String[] userInfos=userInfo.split(",");
		if(!userInfos[0].equals(cc.getToken())){//���߷α��� �̶��
			ResConnectChat resConnectChat=new ResConnectChat(doubleLoginSignal, null,cc.getStudentId()); //1 ���߷α��� ��ȣ
			simpMessagingTemplate.convertAndSend("/sub/chat/"+cc.getRoomId(),resConnectChat);
		}else{
			if(!userInfos[1].equals("0")) { //���� ȸ���̶��
				//���糯¥�� ������¥�� ���Ѵ�
				if(Integer.parseInt(userInfos[1])>=Integer.parseInt(getCurrentTime())) { //����ȸ��
					ResConnectChat resConnectChat=new ResConnectChat(suspendedSignal, userInfos[1],cc.getStudentId()); //2 ���� ȸ��
					simpMessagingTemplate.convertAndSend("/sub/chat/"+cc.getRoomId(),resConnectChat);
					
					//��� ��鿡 ���ؼ� ��ū ������ �����Ѵ�. ������������ �޽����� �Ȱ��� �ȴ�.
					User user=dbRepository.findUser(cc.getStudentId());
					ArrayList<UserRoomData>curSubjects=user.getRoomIds();
					for(int i=0;i<curSubjects.size();i++) {
						serverRepository.removeRoomInToken(curSubjects.get(i).getRoomName(), cc.getToken());
					}
					//System.out.println("���� ȸ��");
				}else { //������ Ǯ�� ȸ��
					serverRepository.removeSuspendedUser(cc.getStudentId()
							,new StringBuilder().append(cc.getToken()+",0").toString());
					dbRepository.removeSuspendedUser(cc.getStudentId());
				}
			}
		}
	}
	
	//���糯¥
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
