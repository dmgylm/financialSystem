package cn.financial.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.financial.dao.OrganizationDAO;
import cn.financial.model.Organization;
import cn.financial.service.impl.OrganizationServiceImpl;
    /**
     * @author hsl
     */
@Service
public class OrganizaCodeService {
  @Autowired
  private OrganizationServiceImpl organizationServiceImpl;
  @Autowired
  private OrganizationDAO organizationDAO;
  /**
   * 
   * @param ids
   * @return  组织架构数据
   */
  public   List<Organization> organization(List  ids){
	  List<Organization> list = organizationDAO.listOrganization(ids);
	  List<Organization> codeSonList = new ArrayList<Organization>();
      List<String> listmap = new ArrayList<String>();
      for (Organization organization : list) {
          String his_permission = organization.getHis_permission();
          String[] hps = his_permission.split(",");// 分割逗号
          listmap.addAll(Arrays.asList(hps));// 所有的his_permission存到listmap当中
      }
      // 查询对应的节点的数据
     List<Organization> listshow = organizationDAO.listOrganizationcode(listmap);
     //根据每个节点id查询对应的子节点数据
     for (int i = 0; i < listshow.size(); i++) {
   	 List<Organization> codeSon = organizationServiceImpl
				.listTreeByIdForSon(listshow.get(i).getId());
		for (int j = 0; j < codeSon.size(); j++) {
			codeSonList.add(codeSon.get(j));
		}
	}

     
      return codeSonList;
        
}
}
