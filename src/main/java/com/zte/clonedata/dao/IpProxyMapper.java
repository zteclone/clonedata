package com.zte.clonedata.dao;

import com.zte.clonedata.model.IpProxy;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IpProxyMapper {
    int deleteByPrimaryKey(String id);
    int insert(IpProxy record);
    IpProxy selectByPrimaryKey(String id);
    IpProxy selectRandOne();
    void updateExecuteCount(String id);
    void updateErrorCount(String id);
    void deleteError();
    void updateOld();
    int proxyCount();
}