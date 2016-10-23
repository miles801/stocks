package com.michael.base.org.vo;

import com.michael.base.emp.vo.EmpVo;
import com.michael.base.org.domain.Org;

import java.util.List;

/**
 * @author Michael
 */
public class OrgDTO {
    private List<Org> orgs;
    private List<EmpVo> emps;

    public List<Org> getOrgs() {
        return orgs;
    }

    public void setOrgs(List<Org> orgs) {
        this.orgs = orgs;
    }

    public List<EmpVo> getEmps() {
        return emps;
    }

    public void setEmps(List<EmpVo> emps) {
        this.emps = emps;
    }
}
