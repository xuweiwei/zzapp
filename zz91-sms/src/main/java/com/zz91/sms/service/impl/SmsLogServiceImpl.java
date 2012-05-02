package com.zz91.sms.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Component;

import com.zz91.sms.dao.SmsLogDao;
import com.zz91.sms.dao.TemplateDao;
import com.zz91.sms.domain.SmsLog;
import com.zz91.sms.domain.Template;
import com.zz91.sms.dto.Pager;
import com.zz91.sms.service.SmsLogService;
import com.zz91.util.Assert;

@Component("smsLogService")
public class SmsLogServiceImpl implements SmsLogService {

	@Resource
	private SmsLogDao smsLogDao;
	@Resource
	private TemplateDao templateDao;

	@Override
	public Pager<SmsLog> pageLog(String from, String to, Integer sendStatus,
			String receiver, String gatewayCode, Integer priority,
			String content, Pager<SmsLog> page) {
		if (page.getSort() == null) {
			page.setSort("gmt_send");
		}
		if (page.getDir() == null) {
			page.setDir("desc");
		}

		page.setRecords(smsLogDao.queryLog(from, to, sendStatus, receiver, gatewayCode, priority, content, page));
		page.setTotals(smsLogDao.queryLogCount(from, to, receiver, gatewayCode, priority, content, sendStatus));
		return page;
	}

	@Override
	public Integer remove(Integer id) {
		Assert.notNull(id, "the id can not be null!");
		return smsLogDao.delete(id);
	}

	@Override
	public void resend(Integer id) {
		smsLogDao.updateStatus(id, SEND_READY);
	}

	@Override
	public List<SmsLog> queryLogs(Integer i) {
		return smsLogDao.querySmsSend(i);
	}

	@Override
	public boolean shutdownRecovery(Integer fromStatus, Integer toStatus) {
		if (fromStatus == null || toStatus == null) {
			return false;
		}
		if (fromStatus.intValue() == toStatus.intValue()) {
			return false;
		}
		Integer i = smsLogDao.recoverStatus(fromStatus, toStatus);
		if (i != null) {
			return true;
		}
		return false;
	}

	@Override
	public Integer updateSuccess(Integer smsId, Integer sendStatus) {
		return smsLogDao.updateStatus(smsId, sendStatus);
	}

	@Override
	public Integer create(SmsLog sms) {
		String code=sms.getTemplateCode();
		String gatewayCode=sms.getGatewayCode();
		Integer priority=sms.getPriority();
		Date gmtSend=sms.getGmtSend();
		Integer sendStatus=sms.getSendStatus();
		Template template = templateDao.queryTemplateByCode(code);
		if(code==null){
			sms.setTemplateCode(template.getCode());
		}
		if(gatewayCode==null){
			sms.setGatewayCode("emay_jar");
		}
		if(priority==null){
			sms.setPriority(0);
		}
		if(gmtSend==null){
			sms.setGmtSend(new Date());
		}
		if(sendStatus==null){
			sms.setSendStatus(0);
		}
		sms.setContent(buildSmsContent(template.getContent()+template.getSigned(), sms.getSmsParameter()));		
		return smsLogDao.insert(sms);
	}

	private String buildSmsContent(String content, String smsParameter) {
		String str = smsParameter.toString();
		JSONArray obj = JSONArray.fromObject(str);
		String[] sa = (String[]) obj.toArray();
		return String.format(content, sa);
	}

	
}