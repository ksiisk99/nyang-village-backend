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
@Api(tags = "관리자 API")
@RestController
@RequestMapping("/ay")
@PropertySource("classpath:application.properties")
public class ManageController {
	private final ManageService manageService;
		
	@Autowired
	public ManageController(ManageService manageService) {
		this.manageService = manageService;
	}

	//신고 목록 보기
	@GetMapping(path="/manage")
	@PreAuthorize("@ManageSecurity.isJwt(#jwt)")
	@ApiOperation(value = "신고 목록 조회", notes = "사용자가 유저를 신고한 목록 조회")
	public @ResponseBody List<ReportDto> reports(@RequestHeader(name = "jwt") String jwt) {
		return manageService.displayReports();
	}
	
	//신고 처리 하기==유저 정지
	@PostMapping(path="/manage/report")
	@PreAuthorize("@ManageSecurity.isJwt(#jwt)")
	@ApiOperation(value = "사용자 정지 주기", notes = "신고 목록 중 신고 대상자 정지 주기")
	public boolean manageReport(@RequestBody ReqSuspend reqSuspend
		,@RequestHeader(name = "jwt") String jwt){
		return manageService.manageReport(reqSuspend);
	}
}
