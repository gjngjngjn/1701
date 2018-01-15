package cn.easybuy.dao.user;

import java.sql.SQLException;
import java.util.List;

import cn.easybuy.dao.IBaseDao;
import cn.easybuy.entity.User;
import cn.easybuy.param.UserParam;

/***
 * UserDao 鐢ㄦ埛涓氬姟鐨刣ao灞�
 * 浠庣埗绫荤户鎵夸笅鐨勮浣跨敤鐨勬柟娉�
 * User getById(userId);
 * Integer userDao.getRowCount(params);
 * List<User> userDao.getRowList(params);
 */
public interface UserMapper extends IBaseDao {

	int add(User user) throws Exception;//鏂板鐢ㄦ埛淇℃伅

	int update(User user) throws Exception;//鏇存柊鐢ㄦ埛淇℃伅

	public int deleteUserById(Integer id) throws Exception;
	
	public List<User> getUserList(Integer currentPageNo,Integer pageSize)throws Exception;
	
	public Integer count() throws Exception;
	
	public User getUser(Integer id,String loginName) throws Exception;
}
