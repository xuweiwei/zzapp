package com.zz91.sms.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zz91.sms.dao.SmsLogDao;
import com.zz91.sms.domain.SmsLog;
import com.zz91.sms.dto.Pager;
import com.zz91.sms.service.SmsLogService;
import com.zz91.util.Assert;

@Component("smsLogService")
public class SmsLogServiceImpl implements SmsLogService {

	@Resource
	private SmsLogDao smsLogDao;

	@Override
	public Pager<SmsLog> pageLog(String from, String to, Integer sendStatus,
			String receiver, String gatewayCode, Integer priority,
			String content, String templateCode, Pager<SmsLog> page) {
		if (page.getSort() == null) {
			page.setSort("gmt_send");
		}
		if (page.getDir() == null) {
			page.setDir("desc");
		}

		page.setRecords(smsLogDao.queryLog(from, to, sendStatus, receiver, gatewayCode, priority, content, templateCode, page));
		page.setTotals(smsLogDao.queryLogCount(from, to, receiver, gatewayCode, priority, content,templateCode, sendStatus));
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
		sms.setContent("你好");
		sms.setGmtSend(new Date());
		sms.setPriority(0);
		sms.setTemplateCode("");
		sms.setSendStatus(0);
		return smsLogDao.insert(sms);
	}
}
