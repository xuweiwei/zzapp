/**
 * 
 */
package com.zz91.sms.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.zz91.sms.domain.SmsLog;
import com.zz91.sms.dto.ExtResult;
import com.zz91.sms.dto.Pager;
import com.zz91.sms.service.smslog.SmsLogService;
import com.zz91.util.datetime.DateUtil;

/**
 * @author root
 * 
 */
@Controller
public class SmsLogController extends BaseController {

	@Resource
	private SmsLogService smsService;

	@RequestMapping
	public ModelAndView index(HttpServletRequest request,
			Map<String, Object> out) {
		return null;
	}

	@RequestMapping
	public ModelAndView querySms(HttpServletRequest request,
			Map<String, Object> out, String from, String to, Integer sendStatus,
			String receiver, String gatewayCode, Integer priority,
			String content, Pager<SmsLog> page) {

//		Date begin = null;
//		Date end = null;
//		try {
//			if(StringUtils.isNotEmpty(from)) {
//				begin = DateUtil.getDate(from, "yyyy-mm-dd HH:mm:ss");
//			}
//			if(StringUtils.isNotEmpty(to)) {
//				end = DateUtil.getDate(to, "yyyy-mm-dd HH:mm:ss");
//			}
//		} catch(ParseException e) {
//		}
//		
		page = smsService.pageLog(from, to, sendStatus, receiver, gatewayCode, priority, content, page);

		return printJson(page, out);
	}

	@RequestMapping
	public ModelAndView deleteSms(HttpServletRequest request,
			Map<String, Object> out, Integer id) {

		Integer i = smsService.remove(id);

		ExtResult result = new ExtResult();
		if (i != null && i.intValue() > 0) {
			result.setSuccess(true);
		}
		return printJson(result, out);
	}

	@RequestMapping
	public ModelAndView resendSms(HttpServletRequest request,
			Map<String, Object> out, Integer id) {

		smsService.resend(id);
		ExtResult result = new ExtResult();
		if (id != null && id.intValue() > 0) {
			result.setSuccess(true);
		}
		return printJson(result, out);
	}
}
