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
import com.ay.talk.dto.request.ReqPcLogin;
import com.ay.talk.dto.response.ResLogin;
import com.ay.talk.dto.response.ResLogout;
import com.ay.talk.dto.response.ResPcLogin;
import com.ay.talk.entity.User;
import com.ay.talk.entity.UserRoomData;
import com.ay.talk.jwt.JwtTokenProvider;
import com.ay.talk.repository.DbRepository;
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
	private final DbRepository dbRepository;
	private final ServerRepository serverRepository; //���� �� �޸� ����
	private final FcmService fcmService;
	private final ChatService chatService;
	private final JwtTokenProvider jwtTokenProvider; //��ū �߱�
	private final int updateSignal=1; //������Ʈ ��ȣ
	private final int firstLoginSignal=3; //ó�� �α��� ��ȣ 3
	private final int reviseSubjectSignal=4; //���������� ȸ��
	private final int doubleLoginSignal=4; //���߷α��� ��ȣ ���������� Ŭ���̾�Ʈ���� ó���ϴ� ������ ����
	private final int misspellSignal=5; //���̵� ��й�ȣ�� �߸� �Էµ� ��ȣ 5
	private final int suspendedSignal=6; //����ȸ�� ��ȣ 6
	private final int pcNotMobileLoginSignal=2; //pc���� �α��� �� �� ����Ͽ��� �α����� �ȵǾ� ������ ��ȣ 2
	private final int pcSuspendedUserLoginSignal=3; //pc���� �α��� �� �� ����ȸ���̶�� ��ȣ3
	private final int pcSuccessLoginSignal=1; //pc�α��� ���� ��ȣ 1
	private final int pcFailLoginSignal=4; //pc ���̵� ��й�ȣ �߸� �Էµ� ��ȣ 4
	private final int enterMsgSignal=0; //���� ��ȣ 0
	private final int exitMsgSignal=1; //���� ��ȣ 1
	private final int failLogoutSignal=0; //�α׾ƿ� ���� ��ȣ 0
	private final int successLogoutSignal=1; //�α׾ƿ� ���� ��ȣ 1

	private Logger logger; //�α�
	@Autowired
	public LoginServiceImpl(DbRepository dbRepository, ServerRepository serverRepository
			,FcmService fcmService, ChatService chatService, JwtTokenProvider jwtTokenProvider) {
		this.dbRepository=dbRepository;
		this.serverRepository=serverRepository;
		this.fcmService=fcmService;
		this.chatService=chatService;
		this.jwtTokenProvider=jwtTokenProvider;
		logger=LoggerFactory.getLogger(this.getClass());
	}
	

	@Override
	public ResLogin login(ReqLogin reqLogin) throws FirebaseMessagingException, IOException {
		ResLogin resLogin=new ResLogin();
		//logger.info("studentId: {}",reqLogin.getStudentId());
		
		if(reqLogin.getVersion()!=serverRepository.getVersion()) { //������ �ٸ��� ������Ʈ ��ȣ ����
			resLogin.setSignal(updateSignal); //������Ʈ ��ȣ 1
			return resLogin;
		}
		
		String studentId=reqLogin.getStudentId();
		String password=reqLogin.getPassword();
				
		ArrayList<SubjectInfo>userSubjects=Crawling(studentId, password); //ũ�Ѹ� ���� ��������
		if(userSubjects==null) { //���̵� ��й�ȣ�� �߸� �Էµ� ��ȣ 5
			resLogin.setSignal(misspellSignal);
			return resLogin;
		}
		String tmp=serverRepository.getUserInfo(studentId);
		String[] userInfo=null;
		if(tmp!=null)
			userInfo=serverRepository.getUserInfo(studentId).split(","); //0:fcm , 1:suspendedPriod
		
		if(userInfo!=null && !userInfo[1].equals("0")) { //���� ȸ���̸� ���� �Ⱓ ����
			//���糯¥�� ������¥ ���� ������ �ƴҶ��� �����϶� ó��
			if(Integer.parseInt(userInfo[1])>=Integer.parseInt(getCurrentTime())) { //���� ȸ��
				resLogin.setSuspendedDate(userInfo[1]);
				resLogin.setSignal(suspendedSignal); //���� ȸ�� ��ȣ 6
				return resLogin;
			}else {//������ Ǯ�� ȸ��
				serverRepository.removeSuspendedUser(studentId
						,new StringBuilder().append(reqLogin.getFcm()+",0").toString());
				dbRepository.removeSuspendedUser(studentId);
			}
		}
		//long beforeTime = System.currentTimeMillis();
		String jwt=null;
		
		if(userInfo==null) { //ó�� �α����� ȸ��
			resLogin.setSignal(firstLoginSignal); //ó�� �α��� ��ȣ 3
			
			ArrayList<UserRoomData> roomIds=new ArrayList<UserRoomData>(); //�濡 ���� ����� ����(�г���,����̵�,���̸�)
			ArrayList<SubjectInfo> curSubjects=userSubjects; //������� ���� ������ �ӽ� ������ ����
			ArrayList<RoomInfo> roomInfos=new ArrayList<RoomInfo>(); //response�ϱ� ���� ���� ��ȿ� ���� �г���
			ArrayList<String> authorities=new ArrayList<String>(); //����
			authorities.add("Customer");
			for(int i=0;i<curSubjects.size();i++) { //������� ��� ���� ���� ����
				String subjectName=curSubjects.get(i).getSubjectName();
				for(int j=serverRepository.getStartRandomNickNameIdx(subjectName);j<200;j++) { //�� �濡 ���� �̻�� �����г��� ã�� 
					
					if(serverRepository.isCheckRoomName(subjectName,j)==false) { //�̻������ �����г���
						UserRoomData userRoomData=new UserRoomData(serverRepository.getRandomNickName(j),
								serverRepository.getRoomId(subjectName), subjectName,curSubjects.get(i).getProfessorName());
						RoomInfo roomInfo=new RoomInfo(subjectName,
								serverRepository.getRoomId(subjectName),
								serverRepository.getRandomNickName(j),curSubjects.get(i).getProfessorName(),
								(ArrayList<String>)serverRepository.getRoomInNames(subjectName));
						serverRepository.setCheckRoomName(subjectName,j,true); //�ش� ���� �����г����� ��������� check�Ѵ�.
						roomIds.add(userRoomData); //db�� ���� ��Ű�� ���� �� ���� �߰�
						roomInfos.add(roomInfo); //response�� �� ���� �߰�
								
						//fcm ���� �޽��� ����
						fcmService.enterExitMsg(enterMsgSignal,serverRepository.getRoomId(subjectName),serverRepository.getRandomNickName(j));
					
						//�� ���� ���� �޽��� ����
						chatService.enterExitMsg(enterMsgSignal,serverRepository.getRoomId(subjectName)
								,serverRepository.getRandomNickName(j)); 
						serverRepository.addRoomInName(subjectName,j); //�� �ȿ� ����� �г��� �߰�
						serverRepository.addRoomInToken(subjectName,reqLogin.getFcm()); //�� �ȿ� ����� ��ū �߰�
						break;
					}
				}
			}
			User user=new User(studentId, reqLogin.getFcm(), getCurrentTime(), roomIds,authorities);
			dbRepository.insertUser(user);
			resLogin.setRoomInfos(roomInfos);
			serverRepository.addUserInfo(studentId,
					new StringBuilder().append(reqLogin.getFcm()+",0").toString()); //�й� ��ū ���
			jwt=jwtTokenProvider.createToken(studentId, authorities); //jwt ����
		}else if(userInfo[0].equals(reqLogin.getFcm())) { //���� ���� �α׾ƿ��ϰ� �α����� ȸ��
			resLogin.setSignal(reviseSubjectSignal); //���� ��� �α׾ƿ��ϰ� �α����� ȸ�� ��ȣ4
			ArrayList<SubjectInfo> curSubjects=userSubjects; //������� ���� ������ �ӽ� ������ ����
			ArrayList<RoomInfo> roomInfos=new ArrayList<RoomInfo>(); //response�ϱ� ���� ���� ��ȿ� ���� �г���
			int[] checkRoom=new int[curSubjects.size()]; //���Ӱ� ��� ���� �ε��� ���� ���� ���� ���Ѿ��ϱ� �����̴�.
			int cnt=0; //���� ����� ��ġ�� �ʴ� ���� ��� ���� ��
			ArrayList<UserRoomData> resultSubjects=new ArrayList<UserRoomData>(); //���� �������� db �����
			User user=dbRepository.findUser(studentId); //db�� ����� ���� ������ �ҷ��´�
			List<UserRoomData> beforeSubjects=user.getRoomIds(); //�ٲ�� �� ���� ����
			boolean[] overlap=new boolean[beforeSubjects.size()]; //������ ���� �������� �� ��ġ�� �κ� üũ
			List<String> authorities=user.getAuthorities();
			
			int curSize=curSubjects.size();
			int beforeSize=beforeSubjects.size();
			for(int i=0;i<curSize;i++) { //���� ��������
				for(int j=0;j<beforeSize;j++) { //�ٲ�� �� ��������
					if(beforeSubjects.get(j).getRoomName().equals(curSubjects.get(i).getSubjectName())) { //�ٲ�� �� ��������� ������ ���������� �����ϴٸ�
						UserRoomData userRoomData=new UserRoomData(beforeSubjects.get(j).getNickName(),
								beforeSubjects.get(j).getRoomId(), beforeSubjects.get(j).getRoomName(),curSubjects.get(i).getProfessorName());
						resultSubjects.add(userRoomData);
						overlap[j]=true; //��ġ�� �������� �ε��� üũ ��ġ�� �ʴ� ������ ���� ����� ������ �����̴�.
						RoomInfo roomInfo=new RoomInfo(beforeSubjects.get(j).getRoomName()
								,beforeSubjects.get(j).getRoomId(),beforeSubjects.get(j).getNickName()
								,curSubjects.get(i).getProfessorName()
								,(ArrayList<String>)serverRepository.getRoomInNames(beforeSubjects.get(j).getRoomName()));
						roomInfos.add(roomInfo);
						break;
					}
					
					if(j==(beforeSize-1)) { //�� ���Ҷ����� ��ġ�� ������ ���ٸ� �� ������ ���� ��� ������
						String subjectName=curSubjects.get(i).getSubjectName();
						for(int k=serverRepository.getStartRandomNickNameIdx(subjectName);k<200;k++){
							if(serverRepository.isCheckRoomName(subjectName, k)==false) {//�ش� ���� �����г��� �ε����� �ش��ϴ� �̸��� ����ϴ� ����ڰ� ���ٸ�
								UserRoomData userRoomData=new UserRoomData(serverRepository.getRandomNickName(k)
										,serverRepository.getRoomId(subjectName), subjectName,curSubjects.get(i).getProfessorName());
								serverRepository.setCheckRoomName(subjectName, k, true);
								resultSubjects.add(userRoomData);
								checkRoom[cnt++]=resultSubjects.size()-1; //���� ��� ���� ���� ���� �ε��� ��ȣ ���� ���߿� ���� �޽����� ������ ����
								break;
							}
						}
					}
				}
			}
			
			dbRepository.updateUserRoom(studentId,resultSubjects); //���� ������ db�� ������Ʈ �Ѵ�
			
			for(int i=0;i<beforeSize;i++) { //���� �����߿��� ��ġ�� �ʴ� ���� �־ ����޽����� ������.
				if(overlap[i])continue; //��ġ�� ������ ���� �����Ѵ�.
				serverRepository.removeRoomInName(beforeSubjects.get(i).getRoomName(),
						beforeSubjects.get(i).getNickName());
				serverRepository.removeRoomInToken(beforeSubjects.get(i).getRoomName(),
						reqLogin.getFcm());
				//���� �޽��� ����
				fcmService.enterExitMsg(exitMsgSignal, beforeSubjects.get(i).getRoomId(),beforeSubjects.get(i).getNickName());
				chatService.enterExitMsg(exitMsgSignal, beforeSubjects.get(i).getRoomId(), beforeSubjects.get(i).getNickName());
			}			
			for(int i=0;i<cnt;i++) { //���� �����߿��� ��ġ�� �ʴ� ���ο� ���� �־ ����޽����� ������.
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
			jwt=jwtTokenProvider.createToken(studentId, authorities); //jwt ����
		} else { //�ٸ� ���� �α����ϰų� �����ϰ� �ٽ� �� ȸ��(��ū�� �޶���)
			resLogin.setSignal(doubleLoginSignal); //�ٸ� ���� �α����� ȸ�� ��ȣ4
			User user=dbRepository.findUser(studentId);
			ArrayList<SubjectInfo> curSubjects=userSubjects; //������� ���� ������ �ӽ� ������ ����
			List<UserRoomData> beforeSubjects=user.getRoomIds(); //���� ��������
			List<String> authorities=user.getAuthorities();
			ArrayList<RoomInfo> roomInfos=new ArrayList<RoomInfo>(); //response�ϱ� ���� ���� ��ȿ� ���� �г���
			int[] checkRoom=new int[curSubjects.size()]; //���Ӱ� ��� ���� �ε��� ���� ���� ���� ���Ѿ��ϱ� �����̴�.
			int cnt=0; //���� ����� ��ġ�� �ʴ� ���� ��� ���� ��
			ArrayList<UserRoomData> resultSubjects=new ArrayList<UserRoomData>(); //���� �������� db �����
			boolean[] overlap=new boolean[beforeSubjects.size()]; //������ ���� �������� �� ��ġ�� �κ� üũ
			
			int curSize=curSubjects.size(); //���� �������� ��
			int beforeSize=beforeSubjects.size(); //���� �������� ��
						
			//���� �������� ���� ��� ��ū ���� �����Ѵ�.
			for(int i=0;i<beforeSubjects.size();i++) {
				serverRepository.removeRoomInToken(beforeSubjects.get(i).getRoomName(), user.getFcm());
			}
			
			for(int i=0;i<curSize;i++) { //���� ��������
				for(int j=0;j<beforeSize;j++) { //�ٲ�� �� ��������
					if(beforeSubjects.get(j).getRoomName().equals(curSubjects.get(i).getSubjectName())) { //�ٲ�� �� ��������� ������ ���������� �����ϴٸ�
						UserRoomData userRoomData=new UserRoomData(beforeSubjects.get(j).getNickName(),
								beforeSubjects.get(j).getRoomId(), beforeSubjects.get(j).getRoomName(),beforeSubjects.get(i).getProfessorName());
						resultSubjects.add(userRoomData);
						overlap[j]=true; //��ġ�� �������� �ε��� üũ ��ġ�� �ʴ� ������ ���� ����� ������ �����̴�.
						RoomInfo roomInfo=new RoomInfo(beforeSubjects.get(j).getRoomName()
								,beforeSubjects.get(j).getRoomId()
								,beforeSubjects.get(j).getNickName()
								,beforeSubjects.get(i).getProfessorName()
								,(ArrayList<String>)serverRepository.getRoomInNames(beforeSubjects.get(j).getRoomName()));
						roomInfos.add(roomInfo);
						break;
					}
					
					if(j==(beforeSize-1)) { //�� ���Ҷ����� ��ġ�� ������ ���ٸ� �� ������ ���� ��� ������
						String subjectName=curSubjects.get(i).getSubjectName();
						for(int k=serverRepository.getStartRandomNickNameIdx(subjectName);k<200;k++){
							if(serverRepository.isCheckRoomName(subjectName, k)==false) {//�ش� ���� �����г��� �ε����� �ش��ϴ� �̸��� ����ϴ� ����ڰ� ���ٸ�
								UserRoomData userRoomData=new UserRoomData(serverRepository.getRandomNickName(k)
										,serverRepository.getRoomId(subjectName), subjectName,curSubjects.get(i).getProfessorName());
								serverRepository.setCheckRoomName(subjectName, k, true);
								resultSubjects.add(userRoomData);
								checkRoom[cnt++]=resultSubjects.size()-1; //���� ��� ���� ���� ���� �ε��� ��ȣ ���� ���߿� ���� �޽����� ������ ����
								break;
							}
						}
					}
				}
			}
			
			dbRepository.updateUserRoomAndToken(studentId,resultSubjects,reqLogin.getFcm()); //���� ���� ��� fcm�� db�� ������Ʈ �Ѵ�
			
			for(int i=0;i<beforeSize;i++) { //���� �����߿��� ��ġ�� �ʴ� ���� �־ ����޽����� ������.
				//��ġ�� ������ ���� �����ϰ� �ٲ� ��ū ���� �־��ش�.
				if(overlap[i]) {
					serverRepository.addRoomInToken(beforeSubjects.get(i).getRoomName(), reqLogin.getFcm());
					continue; 
				}
				serverRepository.removeRoomInName(beforeSubjects.get(i).getRoomName(),
						beforeSubjects.get(i).getNickName());
			
				//���� �޽��� ����
				fcmService.enterExitMsg(exitMsgSignal, beforeSubjects.get(i).getRoomId(),beforeSubjects.get(i).getNickName());
				chatService.enterExitMsg(exitMsgSignal, beforeSubjects.get(i).getRoomId(), beforeSubjects.get(i).getNickName());
			}			
			for(int i=0;i<cnt;i++) { //���� �����߿��� ��ġ�� �ʴ� ���ο� ���� �־ ����޽����� ������.
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
			jwt=jwtTokenProvider.createToken(studentId, authorities); //jwt ����
		}
		
		resLogin.setJwt(jwt);
		return resLogin;
	}
	
	
	
	@Override
	public ResLogout logout(ReqLogout reqLogout) {
		// TODO Auto-generated method stub
		ResLogout resLogout=new ResLogout();
		String userInfo=serverRepository.getUserInfo(reqLogout.getStudentId());
		if(userInfo==null) { //���б� ����
			resLogout.setSignal(successLogoutSignal);
			return resLogout;
		}
		
		User user=dbRepository.findUser(reqLogout.getStudentId());
		List<UserRoomData> subjects=user.getRoomIds();
		for(int i=0;i<subjects.size();i++) {
			serverRepository.removeRoomInToken(subjects.get(i).getRoomName(), user.getFcm()); //�濡 ���� �ĺ���ū ����
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

        html=Jsoup.parse(response.body()); //�������� �Ľ�

        Elements crawlingList=html.select(".class_sbox select>option");
        if(crawlingList.size()>1){ //���������� ������ ���� ���л���        	
        	ArrayList<SubjectInfo> subjectList=new ArrayList<>();
            for(int i=1;i< crawlingList.size();i++){
            	String attr=crawlingList.get(i).attr("value");
            	String[] attrValue=attr.split(",");
            	//���б�
            	//subjectList.add(new SubjectInfo(crawlingList.get(i).text().toString(), attrValue[1]));
            	//�����б��
            	subjectList.add(new SubjectInfo(crawlingList.get(i).text().toString()+" "+attrValue[1], attrValue[1])); //�����б��
            }
            return subjectList;
        }
        return null;
	}
	
	//PC�α���
	@Override
	public ResPcLogin pcLogin(ReqPcLogin reqPcLogin) throws IOException {
		// TODO Auto-generated method stub
		ResPcLogin resPcLogin=new ResPcLogin();
		String userInfo=serverRepository.getUserInfo(reqPcLogin.getStudentId());
		if(userInfo==null) { //����Ͽ��� �α��� �� �� �������
			resPcLogin.setSignal(pcNotMobileLoginSignal);
			return resPcLogin;
		}else { //����Ͽ��� �α��� �� �������
			String suspendedPriod=userInfo.split(",")[1];
			if(!suspendedPriod.equals("0")) { //���� ȸ���̶��
				resPcLogin.setSignal(pcSuspendedUserLoginSignal);
				resPcLogin.setSuspendedDate(suspendedPriod);
				return resPcLogin;
			}else {
				pcCrawling(reqPcLogin, resPcLogin);
			}
		}
		return resPcLogin;
	}
	
	@Override
	public void pcCrawling(ReqPcLogin reqPcLogin, ResPcLogin resPcLogin) throws IOException {
		// TODO Auto-generated method stub
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
        data.put("userDTO.userId", reqPcLogin.getStudentId());
        data.put("userDTO.password", reqPcLogin.getPassword());
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
        html=Jsoup.parse(response.body()); //�������� �Ľ�

        Elements crawlingList=html.select(".class_sbox select>option");
        if(crawlingList.size()>1){ //���������� ������ ���� ���л���
        	User user=dbRepository.findUser(reqPcLogin.getStudentId()); //����Ϸ� �α����� db�� ����� ���� ������ �ҷ��´�
        	
        	ArrayList<UserRoomData> userRoomInfos=user.getRoomIds();
			ArrayList<RoomInfo> roomInfos=new ArrayList<RoomInfo>();
			
			for(int i=0;i<userRoomInfos.size();i++) {
				RoomInfo roomInfo=new RoomInfo(userRoomInfos.get(i).getRoomName()
						,userRoomInfos.get(i).getRoomId()
						,userRoomInfos.get(i).getNickName()
						,userRoomInfos.get(i).getProfessorName()
						,(ArrayList<String>)serverRepository.getRoomInNames(userRoomInfos.get(i).getRoomName()));
				roomInfos.add(roomInfo);
			}
			resPcLogin.setSignal(pcSuccessLoginSignal);
			resPcLogin.setAuthority(user.getAuthorities());
			resPcLogin.setJwt(jwtTokenProvider.createToken(reqPcLogin.getStudentId(), user.getAuthorities()));
			resPcLogin.setRoomInfos(roomInfos);
        }else {
        	resPcLogin.setSignal(pcFailLoginSignal); //��й�ȣ�� �߸� �Էµ�
        }
	}

	//���糯¥ ���ϴ� �Լ�
    private String getCurrentTime() {
        long now = System.currentTimeMillis();
        TimeZone tz=TimeZone.getTimeZone("Asia/Seoul");
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.KOREA);
        dateFormat.setTimeZone(tz);
        return dateFormat.format(date);
    }
	

    
    
}
