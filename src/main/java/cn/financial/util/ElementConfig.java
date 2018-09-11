package cn.financial.util;

public class ElementConfig {

    /**
     * 成功
     */
    public static final String RUN_SUCCESSFULLY = "RUN_SUCCESSFULLY";

    /**
     * 失败
     */
    public static final String RUN_ERROR = "RUN_ERROR";

    /**
     * 系统错误
     */
    public static final String RUN_FAILURE = "RUN_FAILURE";

    /**
     * 该用户不存在
     */
    public static final String LOGIN_NO_USER = "LOGIN_NO_USER";

    /**
     * 密码或账户错误
     */
    public static final String LOGIN_USERNAME_ERROR = "LOGIN_USERNAME_ERROR";

    /**
     * 账户已锁
     */
    public static final String LOGIN_USER_LOCKOUT = "LOGIN_USER_LOCKOUT";

    /**
     * 其他错误
     */
    public static final String LOGIN_FAILURE = "LOGIN_FAILURE";

    /**
     * 文件大于5M
     */
    public static final String CAPITAL_FILE_EXCEED_5M = "CAPITAL_FILE_EXCEED_5M";

    /**
     * 新旧密码一致
     */
    public static final String USER_OLDPWD = "USER_OLDPWD";

    /**
     * 原密码输入错误
     */
    public static final String USER_OLDPWD_ERROR = "USER_OLDPWD_ERROR";

    /**
     * 密码输入格式错误
     */
    public static final String USER_PWDFORMAT_ERROR = "USER_PWDFORMAT_ERROR";

    /**
     * 用户名已存在
     */
    public static final String USERNAME_EXISTENCE = "USERNAME_EXISTENCE";
    
    /**
     * 色名称不能重复
     */
    public static final String ROLENAME_EXISTENCE = "ROLENAME_EXISTENCE";
    
    /**
     * 重置密码
     */
    public static final String RESET_PWD = "RESET_PWD";
    
    /**
     * 工号已存在
     */
    public static final String JOBNUMBER_EXISTENCE = "JOBNUMBER_EXISTENCE";
    
    /**
     * 此节点下存在未停用的节点，请先停用子节点
     */
    public static final String ORGANIZA_DELEFALSE = "ORGANIZA_DELEFALSE";
    /**
     * 用户名为空
     */
    public static final String USERNAME_NULL_ERROR = "USERNAME_NULL_ERROR";
    /**
     * 密码为空
     */
    public static final String PASSWORD_NULL_ERROR = "PASSWORD_NULL_ERROR";
    /**
     * 密码已失效，请联系管理员
     */
    public static final String PASSWORD_INVALID_ERROR = "PASSWORD_INVALID_ERROR";
    /**
     * 用户组织架构关联信息不存在
     */
    public static final String USER_ORGANIZATION = "USER_ORGANIZATION";
    /**
     * 用户角色关联系信息不存在
     */
    public static final String USER_ROLE = "USER_ROLE";
    /**
     * 角色功能权限关联信息不存在
     */
    public static final String USER_RESOURCE = "USER_RESOURCE";
    /**
     * session过期
     */
    public static final String USER_SESSION_OVERDUE = "USER_SESSION_OVERDUE";
    /**
     * 用户id为空
     */
    public static final String USER_ID_NULL = "USER_ID_NULL";
    /**
     * 角色id为空
     */
    public static final String USER_ROLEID_ISNULL = "USER_ROLEID_ISNULL";
    /**
     * 用户名为空
     */
    public static final String USER_NAME_NULL = "USER_NAME_NULL";
    /**
     * 真实姓名为空
     */
    public static final String USER_REALNAME_NULL = "USER_REALNAME_NULL";
    /**
     * 工号为空
     */
    public static final String USER_JOBNUMBER_NULL = "USER_JOBNUMBER_NULL";
    /**
     * 新密码为空
     */
    public static final String USER_NEWPWD_NULL = "USER_NEWPWD_NULL";
    /**
     * 旧密码为空
     */
    public static final String USER_OLDPWD_NULL = "USER_OLDPWD_NULL";
    /**
     * 组织结构id为空
     */
    public static final String USER_ORGID_NULL = "USER_ORGID_NULL";
    /**
     * 组织结构id为空
     */
    public static final String USER_ORGID_ISNULL = "USER_ORGID_ISNULL";
    /**
     * 角色id为空
     */
    public static final String USER_ROLEID_NULL = "USER_ROLEID_NULL";
    /**
     * 角色名称为空
     */
    public static final String USER_ROLENAME_NULL = "USER_ROLENAME_NULL";
    /**
     * 功能能权限id为空
     */
    public static final String USER_RESOURCEID_NULL = "USER_RESOURCEID_NULL";
    /**
     * 功能权限父节点为空
     */
    public static final String USER_RESOURCE_PARENTID_NULL = "USER_RESOURCE_PARENTID_NULL";
    /**
     * 功能权限名称为空
     */
    public static final String USER_RESOURCE_NAME_NULL = "USER_RESOURCE_NAME_NULL";
    /**
     * 功能权限为空
     */
    public static final String USER_RESOURCE_PERMSSION_NULL = "USER_RESOURCE_PERMSSION_NULL";
    
    /**
     * 报表类型为空
     */
    public static final String STATIC_REPORTTYPE_NULL = "STATIC_REPORTTYPE_NULL";
    /**
     * 业务板块为空
     */
    public static final String STATIC_BUSINESSTYPE_NULL = "STATIC_BUSINESSTYPE_NULL";
    /**
     * 开始时间为空
     */
    public static final String STATIC_STARTDATE_NULL = "STATIC_STARTDATE_NULL";
    /**
     * 结束时间为空
     */
    public static final String STATIC_ENDDATE_NULL = "STATIC_ENDDATE_NULL";
    /**
     * 选中组织架构id集合为空
     */
    public static final String STATIC_ORGID_NULL = "STATIC_ORGID_NULL";
    /**
     * 缓存id为空
     */
    public static final String STATIC_CACHEUUID_NULL = "STATIC_CACHEUUID_NULL";
    /**
     * 查询详情key为空
     */
    public static final String STATIC_INFOKEY_NULL = "STATIC_INFOKEY_NULL";
    /**
     * 消息id为空
     */
    public static final String MESSAGE_ID_NULL = "MESSAGE_ID_NULL";
    /**
     * 组织架构orgkey为空
     */
    public static final String ORGMOVE_ORGKEY_NULL = "ORGMOVE_ORGKEY_NULL"; 
    /**
     * 报表类型或业务板块有误
     */
    public static final String STATIC_REPORTTYPE_FAIL = "STATIC_REPORTTYPE_FAIL"; 
    /**
     * 业务id为null
     */
    public static final String BUSINESSDATA_ID_NULL="BUSINESSDATA_ID_NULL";
    /**
     * 业务id不正确
     */
    public static final String BUSINESSDATA_ID_FAIL="BUSINESSDATA_ID_FAIL";
    /**
     * 配置模板不存在
     */
    public static final String DATA_MODULE_NULL = "DATA_MODULE_NULL"; 
    /**
     * 根据用户组织权限查询消息为空
     */
    public static final String MESSAGE_LIST_NULL = "MESSAGE_LIST_NULL";
    /**
     * 下载文件失败
     */
    public static final String GETFILE_FAIL = "GETFILE_FAIL";
    /**
     * 查询部门下没有公司级别或部门错误
     */
    public static final String STATIC_ORGLIST_FAIL = "STATIC_ORGLIST_FAIL";
    /**
     * 查询移动节点时间判断
     */
    public static final String MOBILE_ORGANIZATION_FAIL="MOBILE_ORGANIZATION_FAIL";
    /**
     * 对应部门预算表已生成
     */
    public static final String BUDGET_GENERATE="BUDGET_GENERATE";
    /**
     * 部门上级必须有公司级别
     */
    public static final String DEPER_COMPANY="DEPER_COMPANY";
    /**
     * 模板或对应数据计算错误
     */
    public static final String MODULE_FAIL="MODULE_FAIL";
    /**
     * 规定停用节点时间
     */
    public static final String MOBILE_ORGANIZATION_DISABLE="MOBILE_ORGANIZATION_DISABLE";
    /**
     * 部门不能移到部门下
     */
    public static final String DEPER_MOBILE="DEPER_MOBILE";
    /**
     * 部门下面不能添加节点
     */
    public static final String DEPER_REMOVE="DEPER_REMOVE";
    /**
     * 公司上级必须有板块级别
     */
    public static final String DEPER_PLATE="DEPER_PLATE";
    /**
     * 板块上级不能有公司级别
     */
    public static final String DEPER_PLATELEVEL="DEPER_PLATELEVEL";
    /**
     * 公司下面不能添加公司
     */
    public static final String COMPANY_COMPANY="COMPANY_COMPANY";
    /**
     * 板块下面不能添加板块
     */
    public static final String PLATE_PLATELEVEL="PLATE_PLATELEVEL";
    /**
     * 所选板块下没有业务数据
     */
    public static final String BUSSINESS_NODATA="BUSSINESS_NODATA";
    /**
     * 同层级节点不可重复
     */
    public static final String NAMELY_NOSAME="NAMELY_NOSAME";
    /**
     * 公司名不可重复
     */
    public static final String COMPANY_REPEAT ="COMPANY_REPEAT";
    /**
     * 汇总下面不能添加汇总
     */
    public static final String PLATE_SUMMARY="PLATE_SUMMARY";
    /**
     * 没有足够的数据权限对该用户信息进行修改
     */
    public static final String USER_ORGANIZATION_RESULT="USER_ORGANIZATION_RESULT";
    /**
     * 修改当前登录人状态为离职
     */
    public static final String USER_QUIT ="USER_QUIT";

	public static final String ORG_PALTE_MOVE_PLATE = "ORG_PALTE_MOVE_PLATE";
}
