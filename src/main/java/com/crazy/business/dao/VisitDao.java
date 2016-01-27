package com.crazy.business.dao;

import com.crazy.business.model.VisitInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Author: crazy.jack
 * Date:   16-1-27
 */
@Repository
public interface VisitDao {

    void insert(@Param("visitInfo")VisitInfo visitInfo);

}
