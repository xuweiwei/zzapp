package com.zz91.sms.service.gateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zz91.sms.domain.Gateway;

public interface GatewayService {
	
	final static Map<String,Object> CACHE_GATEWAY = new HashMap<String, Object>();
	public static final int ENABLED_TRUE = 1;
	public static final int ENABLED_FALSE = 0;
	
	public Integer create(Gateway gateway);
	
	public Integer update(Gateway gateway);
	
	public Integer remove(Integer id);
	
	public void enabled(Integer id);
	
	public void disabled(Integer id);
	
	public Object balance(Integer id);
	
	public boolean gatwayTest(String to, Integer id);
	
	public List<Gateway> query(Integer enabled);
	
	public Gateway queryOne(Integer id);
	
	/**
	 * 初始化网关帐号Map数据
	 */
	public void initGateway();
}
