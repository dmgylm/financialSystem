package cn.financial.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.financial.model.Business;
import cn.financial.model.BusinessData;
import cn.financial.model.BusinessDataInfo;
import cn.financial.model.Capital;
import cn.financial.model.DataModule;
import cn.financial.model.Organization;
import cn.financial.model.User;
import cn.financial.model.UserRole;
import cn.financial.model.response.BusinessResult;
import cn.financial.model.response.HtmlResult;
import cn.financial.model.response.ResultUtils;
import cn.financial.service.BusinessDataInfoService;
import cn.financial.service.BusinessDataService;
import cn.financial.service.DataModuleService;
import cn.financial.service.OrganizationService;
import cn.financial.service.UserOrganizationService;
import cn.financial.service.UserRoleService;
import cn.financial.service.impl.BusinessDataInfoServiceImpl;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.HtmlGenerate;
import cn.financial.util.JsonConvertExcel;
import cn.financial.util.JsonConvertProcess;
import cn.financial.util.TimeUtils;
import cn.financial.util.UuidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.mail.imap.protocol.Item;

/**
 * 损益表Controller
 * 
 * @author lmn
 *
 */
@Api(tags = "损益/预算数据")
@Controller
@RequestMapping("/businessData")
public class BusinessDataController {

	@Autowired
	private BusinessDataService businessDataService;

	@Autowired
	private BusinessDataInfoServiceImpl businessDataInfoServiceImpl;

	@Autowired
	private DataModuleService dataModuleService;

	@Autowired
	private UserOrganizationService userOrganizationService; // 角色的权限

	@Autowired
	private OrganizationService organizationService; // 组织架构

	@Autowired
	private DataModuleService dmService;
	
	@Autowired
    private UserRoleService userRoleService;

	@Autowired
	private BusinessDataInfoService businessDataInfoService;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	protected Logger logger = LoggerFactory.getLogger(OrganizationController.class);

	/**
	 * 根据条件查损益数据(提交不传就是查询全部)
	 * 
	 * @param request
	 * @param map
	 * 
	 * @return
	 */
	@RequiresPermissions("businessData:view")
	@RequestMapping(value = "/listBy", method = RequestMethod.POST)
	@ApiOperation(value = "查询损益/预算数据", notes = "根据条件查数据 (不传数据就是查询所有的)", response = BusinessResult.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "查询数据的开始页码（第一页开始）page=1", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "每页显示数据的条数（如每页显示10条数据）", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "year", value = "年份", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "month", value = "月份", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "keyword", value = "关键字搜索,地区，公司名字等", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sId", value = "判断是损益还是预算表  1损益  2 预算", required = false, dataType = "String", paramType = "query"), })
	@ResponseBody
	public BusinessResult listBusinessDataBy(HttpServletRequest request, String keyword, String year, String month,
			Integer sId, Integer page, Integer pageSize) {
		BusinessResult businessResult = new BusinessResult();
		try {
            Map<Object, Object> map = new HashMap<>();
            User user = (User) request.getAttribute("user");
            String uId = user.getId();
            List<JSONObject> userOrganization = userOrganizationService.userOrganizationList(uId); // 判断 权限的数据
            List<JSONObject> listOrganization = new ArrayList<>(); // 筛选过后就的权限数据
            List<JSONObject> listTree = new ArrayList<>(); // 筛选过后就的权限数据
           // List<UserRole> userRole = userRoleService.listUserRole(user.getName(), null);//根据用户名查询对应角色信息
            for (int i = 0; i < userOrganization.size(); i++) {
                //Integer org=Integer.parseInt(userOrganization.get(i).getString("orgType"));
               // String name=userOrganization.get(i).getString("name"); 
               // if (org==BusinessData.ORGNUM || org==BusinessData.DEPNUM || name.contains(BusinessData.NAME)||role==true) { //公司及其公司以下级别才有权限进行录入中心
                    if(keyword!=null&&!keyword.equals("")){
                        Organization CompanyName = organizationService.getCompanyNameBySon(userOrganization.get(i).getString("pid"));// 查询所属的公司名
                        if(CompanyName!=null){
                            if(CompanyName.getOrgName().contains(keyword)){
                                listOrganization.add(userOrganization.get(i));
                            } 
                        }
                        List<Organization> listTreeByIdForSon = organizationService.listTreeByIdForSon(userOrganization.get(i).getString("pid"));
                        JSONArray jsonArr = (JSONArray) JSONArray.toJSON(listTreeByIdForSon);
                        for (int j = 0; j < jsonArr.size(); j++) {
                            JSONObject json = jsonArr.getJSONObject(j);
                            if(json.getString("orgName").contains(keyword)){
                              listTree.add(jsonArr.getJSONObject(j)); 
                            }
                        }                    
                    }else{//查询orgName以下所有级别数据
                            List<Organization> listTreeByIdForSon = organizationService.listTreeByIdForSon(userOrganization.get(i).getString("pid"));
                            JSONArray jsonArr = (JSONArray) JSONArray.toJSON(listTreeByIdForSon);
                            for (int j = 0; j < jsonArr.size(); j++) {
                                JSONObject json = jsonArr.getJSONObject(j);
                                if(Integer.parseInt(json.getString("orgType"))==BusinessData.DEPNUM||json.getString("orgName").contains(BusinessData.NAME)){
                                    listTree.add(jsonArr.getJSONObject(j)); 
                                }
                            }
                        }  
                //}
            }
              if(listOrganization.size()>0 && listOrganization!=null||listTree.size()>0 && listTree!=null){
               // //查询所有符合搜索条件的表数据
                  String[] oId = new String[listOrganization.size() + listTree.size()];// 获取权限的typeId
                  for (int i = 0; i < listOrganization.size(); i++){ // 循环权限全部数据
                      JSONObject pidJosn = listOrganization.get(i);
                      String pid = pidJosn.getString("pid"); // 找到权限数据里面的组织id
                      oId[i] = pid;
                      // 找权限的pid和损益表的typeId进行筛选
                      /*
                       * for (int j = 0; j < list.size(); j++) { String
                       * typeId=list.get(j).getTypeId();//找损益表里面的typeId if(pid.equals(typeId)){
                       * //判断权限pid 和全部数据的typeId是否相同 businessData.add(list.get(j)); // 可以显示的损益数据 } }
                       */
                  }
                  for (int i = 0; i < listTree.size(); i++) {
                      String id = listTree.get(i).getString("id");
                      int m = listOrganization.size();
                      oId[m + i] = id;
                  }
                  List<String> oIds = Arrays.asList(oId);
                  map.put("year", year); // 年份
                  map.put("month", month); // 月份
                  if (sId == null || sId < 1 || sId > 2) {
                      map.put("sId", ""); // 判断是损益还是预算表 1损益 2 预算
                  } else {
                      map.put("sId", sId); // 判断是损益还是预算表 1损益 2 预算
                  }
                  map.put("typeId", oIds); 
                  /*map.put("orgName", keyword); //查询的关键词
  */                
                  List<BusinessData> total = businessDataService.businessDataExport(map); // 查询权限下的所有数据 未经分页
                  if (pageSize == null || pageSize == 0) {
                      map.put("pageSize", 10);
                  } else {
                      map.put("pageSize", pageSize);
                  }
                  if (page == null) {
                      map.put("start", 0);
                  } else {
                      map.put("start", pageSize * (page - 1));
                  }
                  List<BusinessData> businessData = businessDataService.listBusinessDataBy(map); // 查询权限下分页数据
                  // 根据oId查询部门信息
                  // 循环合格数据的oid 去查询他的所有部门
                  List<Business> businessList = new ArrayList<>();
                  for (int i = 0; i < businessData.size(); i++) {
                      String id=businessData.get(i).getTypeId();
                      List<Organization> listTreeByIdForSon = organizationService
                              .listTreeByIdForSon(id); // 根据oId查出公司以下的部门
                      Organization CompanyName = organizationService.getCompanyNameBySon(businessData.get(i).getoId());// 查询所属的公司名
                      for (int j = 0; j < listTreeByIdForSon.size(); j++) {
                          if (listTreeByIdForSon.get(j).getOrgType() == BusinessData.DEPNUM) { // 找到公司以下的节点业务
                              Business business = new Business();
                              business.setId(businessData.get(i).getId());// id
                              business.setYear(businessData.get(i).getYear()); // 年份
                              business.setMonth(businessData.get(i).getMonth()); // 月份
                              business.setUserName(user.getName()); // 用户
                              business.setUpdateTime(businessData.get(i).getUpdateTime()); // 操作时间
                              business.setStatus(businessData.get(i).getStatus());// 状态
                              business.setCompany(CompanyName.getOrgName()); // 公司
                              business.setStructures(listTreeByIdForSon.get(j).getOrgName()); // 业务方式
                              businessList.add(business);
                          }
                      }
                  }
                  ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, businessResult);
                  businessResult.setData(businessList); // 返回的资金流水数据
                  businessResult.setTotal(total.size());// 返回的总条数  
            } else {
                businessResult.setResultDesc("您没有权限操作损益表！");
            }             
        } catch (Exception e) {
			ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, businessResult);
			this.logger.error(e.getMessage(), e);
		}
		return businessResult;
	}

	/**
	 * 根据id查询损益数据
	 * 
	 * @param request
	 * @param id
	 *            业务表id
	 * @param htmlType
	 *            1.HTML类型:配置模板 2HTML类型:录入模板
	 * 
	 * @return
	 */
	@RequiresPermissions("businessData:view")
	@RequestMapping(value = "/listById", method = RequestMethod.POST)
	@ApiOperation(value = "根据id查询损益/预算数据", notes = "根据url的id来获取损益/预算数据", response = HtmlResult.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "表id", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "htmlType", value = "1：HTML类型:配置模板  2：HTML类型:录入页面 3：HTML类型:查看页面 这里的htmlType是2", required = true, dataType = "integer", paramType = "query") })
	@ResponseBody
	public HtmlResult selectBusinessDataById(HttpServletRequest request, String id, Integer htmlType) {
		HtmlResult htmlResult = new HtmlResult();
		try {
            User user = (User) request.getAttribute("user");
		    List<UserRole> userRole = userRoleService.listUserRole(user.getName(), null);//根据用户名查询对应角色信息
		    Boolean role=false;
		    if(userRole.size()>0){
		        for(UserRole list : userRole){
                 if(list.getRoleName().contains("制单员")){
                     role=true;
                 }
                }
		    }
			if (id != null && !id.equals("") && htmlType != null) {
			    if(htmlType==2&&role==true){ //有权限编辑录入中心页面
			        BusinessData businessData = businessDataService.selectBusinessDataById(id);
	                DataModule dm = dmService.getDataModule(businessData.getDataModuleId());
	                BusinessDataInfo busInfo = businessDataInfoService.selectBusinessDataById(id);
	                JSONObject joTemp = JSONObject.parseObject(dm.getModuleData());
	                JSONObject joInfo = JSONObject.parseObject(busInfo.getInfo());
	                //JsonConvertProcess.mergeJson(joTemp, joInfo);
	                HtmlGenerate htmlGenerate = new HtmlGenerate();
	                htmlGenerate.disableBudgetInput();
	                String html = htmlGenerate.generateHtml(JsonConvertProcess.mergeJson(joTemp, joInfo), htmlType);
	                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, htmlResult);
	                htmlResult.setData(html);  
			    }else if(htmlType==3){//查看详情
			        BusinessData businessData = businessDataService.selectBusinessDataById(id);
                    DataModule dm = dmService.getDataModule(businessData.getDataModuleId());
                    BusinessDataInfo busInfo = businessDataInfoService.selectBusinessDataById(id);
                    JSONObject joTemp = JSONObject.parseObject(dm.getModuleData());
                    JSONObject joInfo = JSONObject.parseObject(busInfo.getInfo());
                    //JsonConvertProcess.mergeJson(joTemp, joInfo);
                    HtmlGenerate htmlGenerate = new HtmlGenerate();
                    String html = htmlGenerate.generateHtml(JsonConvertProcess.mergeJson(joTemp, joInfo), htmlType);
                    ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, htmlResult);
                    htmlResult.setData(html); 
			    }else{
			        htmlResult.setResultDesc("您不可以编辑录入中心数据！"); 
			    }
			} else {
				htmlResult.setResultDesc("id或者htmlType为空！");
			}
		} catch (Exception e) {
			ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, htmlResult);
			this.logger.error(e.getMessage(), e);
		}
		return htmlResult;
	}

	/**
	 * 修改损益数据
	 * 
	 * @param request
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequiresPermissions("businessData:update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ApiOperation(value = "修改损益/预算的数据", notes = "根据条件修改损益/预算数据", response = ResultUtils.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "strJson", value = "传的json格式:[{'name':'name1','value':'value1'},{'name':'name2','value':'value2'}]", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "id", value = "表id", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "status", value = "传过来的状态（1保存 , 2提交   ）", required = false, dataType = "String", paramType = "query") })
	@ResponseBody
	public ResultUtils updateBusinessData(HttpServletRequest request, String strJson, Integer status, String id) {
		// 需要参数，前端传来的HTML，业务表的id，状态（1保存 2提交 4退回 ） 0 待提交 1待修改 2已提交 3新增 4 退回修改
		ResultUtils result = new ResultUtils();
		// File file = new File("C:/Users/ellen/Downloads/测试html.txt");
		try {
			// 如果有html则是在编辑页面提交（可能是保存 或者提交）
			if (strJson != null && !strJson.equals("") && id != null && !id.equals("")) {
				if (status == 1 || status == 2) {
					if (status == 1) {// 如果状态是保存 数据库状态修改为 待提交 0 否者 改为已提交 2
						status = 0;
					} else {
						status = 2;// 否者 改为已提交 2
					}
					/*
					 * Document doc = Jsoup.parse(html);// 得到html // Document doc =
					 * Jsoup.parse(file, "UTF-8", "http://example.com/"); Elements inputHtml =
					 * doc.select("input");// 获取HTML所有input属性
					 */ /* System.out.println(inputHtml); */
					BusinessData businessDataById = businessDataService.selectBusinessDataById(id); // 查询出表的数据 得到模板id
					if(status == 2 && businessDataById.getStatus()==status){
					    result.setResultDesc("此数据已经提交！不能重复提交"); 
					}else{
					    JSONArray jsonArr = JSONArray.parseArray(strJson);
	                    Map<String, Object> mo = new HashMap<String, Object>();
	                    for (int i = 0; i < jsonArr.size(); i++) {
	                        JSONObject json = jsonArr.getJSONObject(i);
	                        mo.put(json.getString("name"), json.getString("value"));
	                    }
	                    DataModule dm = dataModuleService.getDataModule(businessDataById.getDataModuleId());// 获取原始模板
	                    JSONObject dataMjo = JSONObject.parseObject(dm.getModuleData());// 获取损益表数据模板
	                    /*
	                     * Map<String, Object> mo = new HashMap<String, Object>(); for (int i = 0; i <
	                     * inputHtml.size(); i++) {// 解析HTML获取所有input name和value值
	                     * mo.put(inputHtml.get(i).attr("name"), inputHtml.get(i).val()); }
	                     */
	                    JSONObject newBudgetHtml = businessDataInfoServiceImpl.dgkey(dataMjo, mo);
	                    BusinessData businessData = new BusinessData();
	                    businessData.setId(id);
	                    businessData.setStatus(status);
	                    // map.put("info",JsonConvertProcess.simplifyJson(newBudgetHtml).toString());
	                    Integer i = businessDataService.updateBusinessData(businessData); // 修改损益表/预算的状态
	                    if (i == 1) {
	                        // 修改从表的info
	                        BusinessDataInfo selectBusinessDataById = businessDataInfoService.selectBusinessDataById(id); // 查询出从表的数据
	                        BusinessDataInfo businessDataInfo = new BusinessDataInfo();
	                        businessDataInfo.setId(selectBusinessDataById.getId());
	                        businessDataInfo.setInfo(JsonConvertProcess.simplifyJson(newBudgetHtml).toString());
	                        Integer infoId = businessDataInfoService.updateBusinessDataInfo(businessDataInfo);
	                        if (infoId == 1) {
	                            ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
	                            result.setResultDesc("修改成功");
	                        } else {
	                            ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
	                            result.setResultDesc("修改失败");
	                        }
	                    } else {
	                        ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
	                        result.setResultDesc("修改失败");
	                    } 
					}
				} else {
					result.setResultDesc("您所给的状态不对！状态（1保存 , 2提交   ）");
				}
			} else if (status == 2 && id != null && !id.equals("")) { // 如果html为空，则是直接提交（此时状态是提交 2）只需要把损益表的状态修改下
			    BusinessData businessDataById = businessDataService.selectBusinessDataById(id); // 查询出表的数据 得到模板id
			    if(businessDataById.getStatus()==status){
			        result.setResultDesc("此数据已经提交！不能重复提交");  
			    }else{
			        BusinessData businessData = new BusinessData();
	                businessData.setId(id);
	                businessData.setStatus(status);
	                Integer i = businessDataService.updateBusinessData(businessData); // 修改损益表/预算的状态
	                if (i == 1) {
	                    ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
	                    result.setResultDesc("修改成功");
	                } else {
	                    ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
	                    result.setResultDesc("修改失败");
	                }  
			    }
			} else {
				result.setResultDesc("id不能为空");
			}
		} catch (Exception e) {
			ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);

			this.logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 修改退回的信息
	 * 
	 * @param request
	 * @return
	 */

	@RequiresPermissions("businessData:update")
	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	@ApiOperation(value = "修改退回的损益/预算的数据", notes = "根据天剑修改退回损益/预算数据", response = ResultUtils.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "表id", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "status", value = "状态 只能为4", required = true, dataType = "integer", paramType = "query") })
	@ResponseBody
	public ResultUtils updateStatus(HttpServletRequest request, String id, Integer status) {
		// 需要参数，前端传来的HTML，业务表的id，状态（1保存 还是 2提交 4退回 ） 0 待提交 1待修改 2已提交 3新增 4 退回修改
		// Map<String, Object> dataMap = new HashMap<String, Object>();
		ResultUtils result = new ResultUtils();
		try {
			if (id!=null&&!id.equals("")&&status == 4) {
				// 如果是退回状态的话 1,2个表里面都增加一条一模一样的数据 2,旧数据的删除状态为0 已经删除不能显示 在新增时候修改新数据的status状态为1 待修改
				// 版本号状态加1
			    String businessId=UuidUtil.getUUID();
				BusinessData selectBusinessDataById = businessDataService.selectBusinessDataById(id);// 查询对应id的数据
				BusinessData businessData = new BusinessData();
				businessData.setId(businessId); // id自动生成
				businessData.setoId(selectBusinessDataById.getoId());
				businessData.setTypeId(selectBusinessDataById.getTypeId());
				businessData.setuId(selectBusinessDataById.getuId());
				businessData.setYear(selectBusinessDataById.getYear());
				businessData.setMonth(selectBusinessDataById.getMonth());
				businessData.setStatus(1);
				businessData.setDelStatus(selectBusinessDataById.getDelStatus());
				businessData.setsId(selectBusinessDataById.getsId());
				businessData.setDataModuleId(selectBusinessDataById.getDataModuleId());
				businessData.setVersion(selectBusinessDataById.getVersion() + 1);
				Integer insertBusinessData = businessDataService.insertBusinessData(businessData); // 新增一条一模一样的新数据
				if (insertBusinessData == 1) {
					ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
					result.setResultDesc("修改成功");
				} else {
					ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
					result.setResultDesc("修改失败");
				}
				Integer deleteBusinessData = businessDataService.deleteBusinessData(id);// 旧数据的删除状态为0
				if (deleteBusinessData == 1) {
					ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
					result.setResultDesc("修改成功");
				} else {
					ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
					result.setResultDesc("修改失败");
				}
				BusinessDataInfo selectBusiness = businessDataInfoService.selectBusinessDataById(id);
				BusinessDataInfo businessDataInfo = new BusinessDataInfo();
				businessDataInfo.setId(UuidUtil.getUUID()); // id自动生成
				businessDataInfo.setBusinessDataId(businessId);
				businessDataInfo.setInfo(selectBusiness.getInfo());
				businessDataInfo.setuId(selectBusiness.getuId());
				Integer insertBusinessDataInfo = businessDataInfoService.insertBusinessDataInfo(businessDataInfo);
				if (insertBusinessDataInfo == 1) {
					ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
					result.setResultDesc("修改成功");
				} else {
					ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
					result.setResultDesc("修改失败");
				}
				Integer deleteBusinessDataInfo = businessDataInfoService.deleteBusinessDataInfo(selectBusiness.getId());
				if (deleteBusinessDataInfo == 1) {
					ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
					result.setResultDesc("修改成功");
				} else {
					ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
					result.setResultDesc("修改失败");
				}
			} else {
				result.setResultDesc("该状态不是4退回状态");
			}
		} catch (Exception e) {
			ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
			this.logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 根据业务表id导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequiresPermissions("businessData:download")
	@RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
	@ApiOperation(value = "导出Excel", notes = "根据业务表id导出excel", response = ResultUtils.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "businId", value = "业务表id", dataType = "string", paramType = "query", required = true) })
	@ApiResponses({ @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 400, message = "失败"),
			@ApiResponse(code = 500, message = "系统错误"), @ApiResponse(code = 251, message = "业务表id为空") })
	public ResultUtils exportExcel(HttpServletRequest request, HttpServletResponse response, String businId)
			throws IOException {
		ResultUtils result = new ResultUtils();
		if (("").equals(businId) || businId == null) {
			ElementXMLUtils.returnValue(ElementConfig.BUSINESSDATA_ID_NULL, result);
		} else {
			try {
				BusinessData businessData = businessDataService.selectBusinessDataById(businId);
				if (businessData == null) {
					ElementXMLUtils.returnValue(ElementConfig.BUSINESSDATA_ID_FAIL, result);
				} else {
					DataModule dm = dataModuleService.getDataModule(businessData.getDataModuleId());
					BusinessDataInfo busInfo = businessDataInfoService.selectBusinessDataById(businId);
					JSONObject joTemp = JSONObject.parseObject(dm.getModuleData());
					JSONObject joInfo = JSONObject.parseObject(busInfo.getInfo());
					// HtmlGenerate htmlGenerate=new HtmlGenerate();
					JSONObject mergeJson = JsonConvertProcess.mergeJson(joTemp, joInfo);
					Workbook wb = JsonConvertExcel.getExcel(mergeJson, dm.getModuleName());
					/* response.setContentType("application/vnd.ms-excel;charset=utf-8"); */
					response.setContentType("application/x-download");
					response.setCharacterEncoding("utf-8");
					// 对文件名进行处理。防止文件名乱码
					String fileName = dm.getModuleName() + ".xlsx";
					fileName = URLEncoder.encode(fileName, "UTF-8");
					System.out.println(fileName + "~~~");
					// Content-disposition属性设置成以附件方式进行下载
					response.setHeader("Content-disposition", "attachment;filename=" + fileName);
					OutputStream os = response.getOutputStream();
					wb.write(os);
					os.flush();
					os.close();
					ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
				}
			} catch (Exception e) {
				ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
			}
		}
		return result;

	}

	/**
	 * 删除损益数据 （修改Status为0）
	 * 
	 * @param request
	 * @return
	 *//*
		 * @RequiresPermissions("businessData:update")
		 * 
		 * @RequestMapping(value="/delete", method = RequestMethod.POST) public
		 * Map<Object, Object> deleteOrganization(HttpServletRequest request) {
		 * Map<Object, Object> dataMap = new HashMap<Object, Object>(); String id =
		 * request.getParameter("id"); try { Integer i
		 * =businessDataService.deleteBusinessData(id); if (i == 1) {
		 * dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
		 * 
		 * } else {
		 * dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
		 * 
		 * } } catch (Exception e) {
		 * dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
		 * this.logger.error(e.getMessage(), e); } return dataMap; }
		 */

	/***
	 * 导出下载
	 * 
	 * @param response
	 * @throws Exception
	 *//*
		 * @RequiresPermissions("businessData:download")
		 * 
		 * @RequestMapping(value = "/export", method = RequestMethod.POST)
		 * 
		 * @ApiOperation(value = "导出损益/预算数据", notes = "根据条件导出所有的数据", response =
		 * ResultUtils.class)
		 * 
		 * @ApiImplicitParams({
		 * 
		 * @ApiImplicitParam(name = "year", value = "年份", required = false, dataType =
		 * "String", paramType = "query"),
		 * 
		 * @ApiImplicitParam(name = "month", value = "月份", required = false, dataType =
		 * "String", paramType = "query"),
		 * 
		 * @ApiImplicitParam(name = "sId", value = "判断是损益还是预算表  1损益  2 预算", required =
		 * true, dataType = "String", paramType = "query"), })
		 * 
		 * @ResponseBody public void export(HttpServletRequest request,
		 * HttpServletResponse response, String year, String month, Integer sId) throws
		 * Exception { OutputStream os = null; // Map<String, Object> dataMap = new
		 * HashMap<String, Object>(); ResultUtils result = new ResultUtils(); try {
		 * Map<Object, Object> map = new HashMap<>(); User user = (User)
		 * request.getAttribute("user"); String uId = user.getId(); map.put("year",
		 * year); // 年份 map.put("month", month); // 月份 map.put("sId", sId); //
		 * 判断是损益还是预算表 1损益 2 预算 List<JSONObject> userOrganization =
		 * userOrganizationService.userOrganizationList(uId); // 判断 权限的数据
		 * List<JSONObject> listOrganization = new ArrayList<>(); // 筛选过后就的权限数据
		 * List<JSONObject> listTree = new ArrayList<>(); // 筛选过后就的权限数据 for (int i = 0;
		 * i < userOrganization.size(); i++) { JSONObject obu = (JSONObject)
		 * userOrganization.get(i); Integer num =
		 * Integer.parseInt(obu.get("orgType").toString()); if (num ==
		 * BusinessData.DEPNUM) {// 部门级别 listOrganization.add(userOrganization.get(i));
		 * } else {// 查询以下级别的部门 List<Organization> listTreeByIdForSon =
		 * organizationService
		 * .listTreeByIdForSon(userOrganization.get(i).getString("pid")); JSONArray
		 * jsonArr = (JSONArray) JSONArray.toJSON(listTreeByIdForSon); for (int j = 0; j
		 * < jsonArr.size(); j++) { JSONObject json = jsonArr.getJSONObject(j); if
		 * (Integer.parseInt(json.getString("orgType")) == BusinessData.DEPNUM &&
		 * !json.getString("orgName").contains(BusinessData.NAME)) {// 部门级别
		 * listTree.add(jsonArr.getJSONObject(j)); } } } } if (listOrganization.size() >
		 * 0 || listTree.size() > 0) { // List<BusinessData> list =
		 * businessDataService.listBusinessDataBy(map); // //查询所有符合搜索条件的表数据 String[]
		 * typeId = new String[listOrganization.size() + listTree.size()];// 获取权限的typeId
		 * // List<BusinessData> businessData=new ArrayList<>(); //所有符合权限的数据 for (int i
		 * = 0; i < listOrganization.size(); i++) { // 循环权限全部数据 JSONObject pidJosn =
		 * userOrganization.get(i); String pid = pidJosn.getString("pid"); //
		 * 找到权限数据里面的组织id typeId[i] = pid; // 找权限的pid和损益表的typeId进行筛选
		 * 
		 * for (int j = 0; j < list.size(); j++) { String
		 * typeId=list.get(j).getTypeId();//找损益表里面的typeId if(pid.equals(typeId)){
		 * //判断权限pid 和全部数据的typeId是否相同 businessData.add(list.get(j)); // 可以显示的损益数据 } }
		 * 
		 * } for (int i = 0; i < listTree.size(); i++) { String id =
		 * listTree.get(i).getString("id"); int m = listOrganization.size(); typeId[m +
		 * i] = id; } List<String> typeIds = Arrays.asList(typeId); map.put("typeId",
		 * typeIds);// 根据权限的typeId查询相对应的数据 List<BusinessData> businessData =
		 * businessDataService.businessDataExport(map); // 查询权限下的所有数据 // 根据oId查询部门信息 //
		 * 循环合格数据的oid 去查询他的所有部门 List<Business> businessList = new ArrayList<>();//
		 * 页面列表排列数据 for (int i = 0; i < businessData.size(); i++) { List<Organization>
		 * listTreeByIdForSon = organizationService
		 * .listTreeByIdForSon(businessData.get(i).getTypeId()); // 根据oId查出公司以下的部门
		 * Organization CompanyName =
		 * organizationService.getCompanyNameBySon(businessData.get(i).getoId());//
		 * 查询所属的公司名 for (int j = 0; j < listTreeByIdForSon.size(); j++) { if
		 * (listTreeByIdForSon.get(j).getOrgType() == BusinessData.DEPNUM) { //
		 * 找到公司以下的节点业务 Business business = new Business();
		 * business.setYear(businessData.get(i).getYear()); // 年份
		 * business.setMonth(businessData.get(i).getMonth()); // 月份
		 * business.setUserName(user.getName()); // 用户
		 * business.setUpdateTime(businessData.get(i).getUpdateTime()); // 操作时间
		 * business.setStatus(businessData.get(i).getStatus());// 状态
		 * business.setCompany(CompanyName.getOrgName()); // 公司
		 * business.setStructures(listTreeByIdForSon.get(j).getOrgName()); // 业务方式
		 * businessList.add(business); } } } List<String[]> strList = new ArrayList<>();
		 * String[] ss = { "年份", "月份", "公司名称", "业务方式", "创建用户", "操作时间", "状态" };
		 * strList.add(ss); for (int i = 0; i < businessList.size(); i++) { String[] str
		 * = new String[7]; Business business = businessList.get(i); if
		 * (business.getYear() != null && !business.getYear().equals("")) { str[0] =
		 * business.getYear().toString(); } if (business.getMonth() != null &&
		 * !business.getMonth().equals("")) { str[1] = business.getMonth().toString(); }
		 * if (business.getCompany() != null && !business.getCompany().equals("")) {
		 * str[2] = business.getCompany(); } if (business.getStructures() != null &&
		 * !business.getStructures().equals("")) { str[3] = business.getStructures(); }
		 * if (business.getUserName() != null && !business.getUserName().equals("")) {
		 * str[4] = business.getUserName(); } if (business.getUpdateTime() != null &&
		 * !business.getUpdateTime().equals("")) { str[5] =
		 * sdf.format(business.getUpdateTime()); } if (business.getStatus() != null &&
		 * !business.getStatus().equals("")) { if (business.getStatus() == 0) { str[6] =
		 * "待提交"; } else if (business.getStatus() == 1) { str[6] = "待修改"; } else if
		 * (business.getStatus() == 2) { str[6] = "已提交"; } else if (business.getStatus()
		 * == 3) { str[6] = "新增"; } else if (business.getStatus() == 4) { str[6] = "退回";
		 * } } strList.add(str); } response.setHeader("Content-Disposition",
		 * "attachment; filename=" + URLEncoder.encode("管理损益表", "UTF-8") + ".xls");
		 * response.setContentType("application/octet-stream"); os =
		 * response.getOutputStream(); ExcelUtil.export(strList, os);
		 * ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result); } else {
		 * result.setResultDesc("您没有权限操作损益数据"); } } catch (IOException e) {
		 * ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
		 * e.printStackTrace(); } finally { if (os != null) try { os.close(); } catch
		 * (IOException e) { e.printStackTrace(); } } }
		 */

}
