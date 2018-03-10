package com.aspire.controller;

import com.aspire.common.HttpClientService;
import com.aspire.pojo.app.JSONGetIndivUserRegisterVerifyCodeReq;
import com.aspire.pojo.app.JSONIndivUserDynLoginReq;
import com.aspire.pojo.app.ResultAppLoginMessage;
import com.aspire.pojo.app.SMSVerifyCodeVo;
import com.aspire.util.ClientUtil;
import com.aspire.util.JacksonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * 处理Android/ios客户端APP请求手机号注册Redis及登录请求
 *
 * @author xiaos
 * @create 2018-02-02-17:24
 */
@RestController
@RequestMapping("user")
public class APPClientController {

	 @Value("${spring.redis.host}")
	 private String redisUrl;
	 @Value("${spring.redis.port}")
	 private Integer redisPort;
	 @Value("${spring.redis.password}")
	 private String redisPwd;
	 @Value("${smsNotiyUrl}")
	 private  String smsNotiyUrl;

	 @Autowired
	 private HttpClientService httpClientService;


	/**
     * 客户端app:个人用户注册获取短信验证码接口
     *
     * @param request
     * @return JSONGetIndivUserRegisterVerifyCodeResp
     */
	@RequestMapping("/smsVerifyCode/applyVerifyCode.do")
	public ResultAppLoginMessage applyVerifyCode(@RequestBody String request) {

		if ( "".equals(request.trim())||null == request) {
			return ResultAppLoginMessage.build(201,"请求数据无效");
		}
		JSONGetIndivUserRegisterVerifyCodeReq jsonGetIndivUserRegisterVerifyCodeReq = JacksonUtil.readValue(request, JSONGetIndivUserRegisterVerifyCodeReq.class);
		String mobilePhone = (null != jsonGetIndivUserRegisterVerifyCodeReq ? jsonGetIndivUserRegisterVerifyCodeReq.getMsisdn() : null);

		if (mobilePhone.isEmpty() || !ClientUtil.isMobileNo(mobilePhone)) {
			return ResultAppLoginMessage.build(201,"手机号无效");
		}
		// 请求短信接口获取验证码
		String verifyCode = ClientUtil.getRandom();
		boolean b = smsNotiy(mobilePhone, verifyCode);
		if (!b) {
			return ResultAppLoginMessage.build(201,"获取验证码失败");
		}
		 //创建短信验证码对象
		SMSVerifyCodeVo smsVerifyCode = new SMSVerifyCodeVo();
		smsVerifyCode.setMobilePhone(mobilePhone);
		smsVerifyCode.setVerifyCode(verifyCode);
		smsVerifyCode.setCreateTime(new Date());

		Jedis jedis = new Jedis(redisUrl,redisPort);
		jedis.auth(redisPwd);
		Map<String,String> map = new HashMap<String,String>();
		map.put("mobilePhone",smsVerifyCode.getMobilePhone());
		map.put("verifyCode",smsVerifyCode.getVerifyCode());
		map.put("createTime",smsVerifyCode.getCreateTime().toString());
		jedis.hmset("VCode_"+mobilePhone,map);
		//设置过期时间
		jedis.expire("VCode_"+mobilePhone,60);
		jedis.close();
		return ResultAppLoginMessage.build(200,"短信注册成功");

	}


	/**
	 * 发送验证码到手机号码
	 * @param mobile
	 * @param random
	 * @return
	 */
	//	public static void main(String[] args) {
	//	smsNotiy("18823460446", "123765");
	//}
	public  boolean smsNotiy(String mobile, String random) {
		try {
			//String postUrl = ConfigureFileHelper.getProperty("context.smsNotiy.url", "http://10.12.9.241:9099/context/smsNotiy");
			//String postUrl = "http://10.12.9.241:9099/context/smsNotiy" ;

			Map<String, Object> body = new HashMap<String, Object>();
			body.put("MsgType", "smsNotiy");
			body.put("Version", "1.0.0");
			body.put("SendDeviceID", "300");
			body.put("RequestId", UUID.randomUUID().toString());

			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("smsContext", "登录|" + random);
			param.put("sendList", mobile);
			params.add(param);
			body.put("params", params);

			String req = JacksonUtil.toJSon(body);
			//调用短信平台接口
			String res = httpClientService.doPostJson(smsNotiyUrl,req);

			Map<String, Integer> result = JacksonUtil.readValue(res, Map.class);

			if (!result.isEmpty()) {
				int status = (int) result.get("status");
				if (0 == status) {
					return true;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
		return false;
	}

	/**
	 * 客户端app:个人用户动态登录接口
	 * @param request
	 * @return
	 */
	@RequestMapping("/indivUser/dynamiclogin.do")
	public ResultAppLoginMessage dynamicLogin(@RequestBody String request) {
		if (null == request || "".equals(request.trim())) {
			return ResultAppLoginMessage.build(201,"请求数据无效");
		}
		JSONIndivUserDynLoginReq jsonIndivUserDynLoginReq = JacksonUtil.readValue(request, JSONIndivUserDynLoginReq.class);
		String mobilePhone = (null != jsonIndivUserDynLoginReq ? jsonIndivUserDynLoginReq.getMobilePhone() : null);// 手机号
		String verifyCode = (null != jsonIndivUserDynLoginReq ? jsonIndivUserDynLoginReq.getCode() : null);// 验证码
		//手机号码  + 验证码方式校验
		if (mobilePhone.isEmpty()||!ClientUtil.isMobileNo(mobilePhone)||mobilePhone.length()<11) {
			System.out.println("手机号mobilePhone = " + mobilePhone);
			return ResultAppLoginMessage.build(201,"手机号无效");
		}
		if (verifyCode.isEmpty()) {
			System.out.println("验证码verifyCode = " + verifyCode);
			return ResultAppLoginMessage.build(201,"验证码无效");
		}
		Jedis jedis = new Jedis(redisUrl,redisPort);
		jedis.auth(redisPwd);
		Map<String,String> map = jedis.hgetAll("VCode_"+mobilePhone);
		String  code = map.get("verifyCode");
		//校验验证码
		if(verifyCode.equals(code)){
			return new ResultAppLoginMessage(0);
		}else{
			return ResultAppLoginMessage.build(201,"登录失败");
		}


	}








}
