package com.iptv.satellite.service;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iptv.satellite.dao.epg.EpgMapper;
import com.iptv.satellite.domain.db.EpgBean;
import com.iptv.satellite.domain.model.EpgModelBean;
import com.iptv.satellite.service.IEpgService;
import com.iptv.satellite.util.SatelliteUtil;

/**
*@author:   yim
*@date:  2018年3月14日下午4:51:22
*@description:   
*/
@Service
public class EpgService implements IEpgService {

	@Autowired
	private EpgMapper epgMapper;

	@Override
	public BigInteger findMaxIdFromEpg(String tableName) {
		// TODO Auto-generated method stub
		return epgMapper.selectMaxIdFromEpg(tableName);
	}

	@Override
	public List<EpgBean> findNewDataFromEpg(String tableName, BigInteger maxId) {
		// TODO Auto-generated method stub
		String angle = SatelliteUtil.getSatelliteAngle(tableName);
		return epgMapper.selectNewFromEpg(angle, maxId, tableName);
	}

	@Override
	public List<EpgModelBean> findOldFromNewData(String tableName, BigInteger maxId) {
		// TODO Auto-generated method stub
		String angle = SatelliteUtil.getSatelliteAngle(tableName);
		return epgMapper.selectOldFromEpg(angle, tableName, maxId);
	}

	@Override
	public BigInteger findFirstFromEpg() {
		// TODO Auto-generated method stub
		return epgMapper.selectFirstFromEpg();
	}

}
