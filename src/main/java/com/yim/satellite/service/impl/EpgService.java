package com.yim.satellite.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yim.satellite.dao.epg.EpgMapper;
import com.yim.satellite.domain.epg.Epg;
import com.yim.satellite.service.IEpgService;

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
	public List<Epg> findEpgFrom_1055e() {
		// TODO Auto-generated method stub
		List<Epg> epgs = epgMapper.selectEpgFrom_1055e();
		for (int i = 0; i < epgs.size(); i++) {
			StringBuffer stringBuffer = new StringBuffer("1055:");
			stringBuffer.append(epgs.get(i).getFrequency() / 1000).append(":").append(epgs.get(i).getServiceId());
			epgs.get(i).setSatelliteAngle(stringBuffer.toString());
		}
		return epgs;
	}

}
