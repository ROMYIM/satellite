package com.iptv.satellite.dao.informationSchema;

import java.util.List;

/**
 * InformationSchemaMapper
 */
public interface InformationSchemaMapper {

    List<String> selectEpgTablesFromInformationSchema();
}