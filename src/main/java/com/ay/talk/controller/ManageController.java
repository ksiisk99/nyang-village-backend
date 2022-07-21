package com.ay.talk.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.method.PreAuthorizeAuthorizationManager;
import org.springframework.security.config.method.MethodSecurityBeanDefinitionParser.PreAuthorizeAuthorizationMethodInterceptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ay.talk.dto.ReportDto;
import com.ay.talk.dto.request.ReqSuspend;
import com.ay.talk.dto.response.ResManage;
import com.ay.talk.service.ManageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@ApiOperation("swagger")
@Api(tags = "������ API")
@RestController
@RequestMapping("/ay")
@PropertySource("classpath:application.properties")
public class ManageController {
	private final ManageService manageService;
		
	@Autowired
	public ManageController(ManageService manageService) {
		this.manageService = manageService;
	}

	//�Ű� ��� ����
	@GetMapping(path="/manage")
	@PreAuthorize("@ManageSecurity.isJwt(#jwt)")
	@ApiOperation(value = "�Ű� ��� ��ȸ", notes = "����ڰ� ������ �Ű��� ��� ��ȸ")
	public @ResponseBody List<ReportDto> reports(@RequestHeader(name = "jwt") String jwt) {
		return manageService.displayReports();
	}
	
	//�Ű� ó�� �ϱ�==���� ����
	@PostMapping(path="/manage/report")
	@PreAuthorize("@ManageSecurity.isJwt(#jwt)")
	@ApiOperation(value = "����� ���� �ֱ�", notes = "�Ű� ��� �� �Ű� ����� ���� �ֱ�")
	public boolean manageReport(@RequestBody ReqSuspend reqSuspend
		,@RequestHeader(name = "jwt") String jwt){
		return manageService.manageReport(reqSuspend);
	}
}
