package com.iptv.satellite.domain.model;
/**
*@author:   yim
*@date:  2018年3月20日下午12:17:23
*@description:   
*/

import java.math.BigInteger;
import java.util.List;

import com.iptv.satellite.domain.db.EpgBean;

public class EpgTableModel {

	private final String tableName;
	private BigInteger currentMaxId;
	private List<EpgBean> epgBeans;
	private List<EpgModel> epgModels;
	private boolean updateFlag;

	public EpgTableModel(String tableName) {
		this.tableName = tableName;
		this.currentMaxId = BigInteger.valueOf(0);
	}

	public BigInteger getCurrentMaxId() {
		return currentMaxId;
	}

	public void setCurrentMaxId(BigInteger currentMaxId) {
		this.currentMaxId = currentMaxId;
	}

	public String getTableName() {
		return tableName;
	}

	/**
	 * @param epgBeans the epgBeans to set
	 */
	public void setEpgBeans(List<EpgBean> epgBeans) {
		this.epgBeans = epgBeans;
	}

	/**
	 * @return the epgBeans
	 */
	public List<EpgBean> getEpgBeans() {
		return epgBeans;
	}

	/**
	 * @param epgModels the epgModels to set
	 */
	public void setEpgModels(List<EpgModel> epgModels) {
		this.epgModels = epgModels;
	}

	/**
	 * @return the epgModels
	 */
	public List<EpgModel> getEpgModels() {
		return epgModels;
	}

	/**
	 * @param updateFlag the updateFlag to set
	 */
	public void setUpdateFlag(boolean updateFlag) {
		this.updateFlag = updateFlag;
	}

	public boolean getUpdateFlag() {
		return updateFlag;
	}
}
