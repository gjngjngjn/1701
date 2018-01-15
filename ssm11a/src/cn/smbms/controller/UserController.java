package cn.smbms.controller;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;

@Controller
@RequestMapping("/user")
public class UserController{
	private Logger logger = Logger.getLogger(UserController.class);
	
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	@RequestMapping(value="/userlist.html")
	public String getUserList(Model model,
		@RequestParam(value="queryname",required=false) String queryUserName,
		@RequestParam(value="queryUserRole",required=false) String queryUserRole,
		@RequestParam(value="pageIndex",required=false) String pageIndex){
		logger.info("getUserList------> queryUserName: "+queryUserName);
		logger.info("getUserList------> queryUserRole: "+queryUserRole);
		logger.info("getUserList------> pageIndex: "+pageIndex);
		
		int _queryUserRole=0;
		List<User> userList=null;
		//设置页面容量
		int pageSize=Constants.pageSize;
		//当前页码
		int currentPageNo=1;
		if(queryUserName == null){
			queryUserName="";
		}
		if(queryUserRole !=null && !queryUserRole.equals("")){
			_queryUserRole=Integer.parseInt(queryUserRole);
		}
		if(pageIndex!=null){
			try {
				currentPageNo=Integer.valueOf(pageIndex);
			} catch (Exception e) {
				return "redirect:/user/syserror.html";
			}
		}
		//总数量（表）
		int totalCount=userService.getUserCount(queryUserName, _queryUserRole);
		//总页数
		PageSupport pages=new PageSupport();
		pages.setCurrentPageNo(currentPageNo);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount=pages.getTotalPageCount();
		//控制首页和尾页
		if(currentPageNo<1){
			currentPageNo=1;
		}else if(currentPageNo>totalPageCount){
			currentPageNo=totalPageCount;
		}
		userList=userService.getUserList(queryUserName, _queryUserRole, currentPageNo, pageSize);
		model.addAttribute("userList", userList);
		List<Role> roleList=null;
		roleList=roleService.getRoleList();
		model.addAttribute("roleList", roleList);
		model.addAttribute("queryUserName", queryUserName);
		model.addAttribute("queryUserRole", queryUserRole);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("currentPageNo", currentPageNo);
		return "user/userlist";
	}
	@RequestMapping(value="/login.html")
	public String login(){
		logger.debug("UserController welcome SMBMS==================");
		return "login";
	}
	
	@RequestMapping(value="/dologin.html",method=RequestMethod.POST)
	public String doLogin(@RequestParam String userCode,@RequestParam String userPassword,HttpServletRequest request,HttpSession session){
		logger.debug("doLogin====================================");
		//调用service方法，进行用户匹配
		User user = userService.login(userCode,userPassword);
		if(null != user){//登录成功
			//放入session
			session.setAttribute(Constants.USER_SESSION, user);
			//页面跳转（frame.jsp）
			return "redirect:/user/main.html";
			//response.sendRedirect("jsp/frame.jsp");
		}else{
			//页面跳转（login.jsp）带出提示信息--转发
			request.setAttribute("error", "用户名或密码不正确");
			return "login";
		}
	}
	@RequestMapping(value="/main.html")
	public String main(HttpSession session){
		if(session.getAttribute(Constants.USER_SESSION) == null){
			return "redirect:/user/login.html";
		}
		return "frame";
	}
	
	@RequestMapping(value="/exlogin.html",method=RequestMethod.GET)
	public String exLogin(@RequestParam String userCode,@RequestParam String userPassword){
		logger.debug("exLogin====================================");
		//调用service方法，进行用户匹配
		User user = userService.login(userCode,userPassword);
		if(null == user){//登录失败
			throw new RuntimeException("用户名或者密码不正确！");
		}
		return "redirect:/user/main.html";
	}
	
	/*@ExceptionHandler(value={RuntimeException.class})
	public String handlerException(RuntimeException e,HttpServletRequest req){
		req.setAttribute("exception", e);
		return "error";
	}*/
	@RequestMapping(value="/useradd.html",method=RequestMethod.GET)
	public String addUser(User user,Model model){
		model.addAttribute("user", user);
		return "useradd";
	}
	
	@RequestMapping(value="addsave.html",method=RequestMethod.POST)
	public String addUserSave(User user,HttpSession session){
		user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
		user.setCreationDate(new Date());
		if(userService.add(user)){
			return "redirect:user/userlist.html";
		}
		return "useradd";
	}
	
}
