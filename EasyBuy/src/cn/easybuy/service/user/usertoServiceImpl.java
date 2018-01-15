package cn.easybuy.service.user;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import cn.easybuy.dao.order.UserAddressDao;
import cn.easybuy.dao.order.UserAddressDaoImpl;
import cn.easybuy.dao.user.UserDao;
import cn.easybuy.dao.user.UserDaoImpl;
import cn.easybuy.dao.user.UserMapper;
import cn.easybuy.entity.UserAddress;
import cn.easybuy.utils.DataSourceUtil;
import cn.easybuy.utils.MyBatisUtil;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import cn.easybuy.entity.User;

public class usertoServiceImpl implements UserService {
	
	private Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Override
	public boolean add(User user){
		SqlSession sqlSession=null;
		
		boolean flag=false;
		try {
			sqlSession = MyBatisUtil.createSqlSession(false);	
			if(sqlSession.getMapper(UserMapper.class).add(user) >0){
				flag=true;
				sqlSession.commit();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback();
			flag=false;
		}finally {
			MyBatisUtil.closeSqlSession(sqlSession);
		}
		return flag;
	}

	@Override
	public boolean update(User user) {
		SqlSession sqlSession=null;
		Integer count=0;
		try {
			sqlSession=MyBatisUtil.createSqlSession(false);
			
			count=sqlSession.getMapper(UserMapper.class).update(user);
			sqlSession.commit();
		}  catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback();
		}finally {
			MyBatisUtil.closeSqlSession(sqlSession);
			return  count>0;
		}
	}

	@Override
	public boolean deleteUserById(Integer userId) {
		SqlSession sqlSession=null;
		Integer count=0;
		try {
			sqlSession=MyBatisUtil.createSqlSession(false);
			
			count=sqlSession.getMapper(UserMapper.class).deleteUserById(userId);
			sqlSession.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			sqlSession.rollback();
		}finally {
			MyBatisUtil.closeSqlSession(sqlSession);
			return  count>0;
		}
	}

	@Override
	public User getUser(Integer userId, String loginName) {
		SqlSession sqlSession=null;
		
		User user=null;
		try {
			sqlSession = MyBatisUtil.createSqlSession(false);	
			
			user=sqlSession.getMapper(UserMapper.class).getUser(userId, loginName);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			MyBatisUtil.closeSqlSession(sqlSession);
			return user;
		}
	}

	@Override
	public List<User> getUserList(Integer currentPageNo, Integer pageSize) {
		Connection connection = null;
		List<User> userList=null;
		try {
			connection = DataSourceUtil.openConnection();
			UserDao userDao = new UserDaoImpl(connection);
			userList=userDao.getUserList(currentPageNo,pageSize);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DataSourceUtil.closeConnection(connection);
			return userList;
		}
	}

	@Override
	public int count() {
		Connection connection = null;
		Integer count=null;
		try {
			connection = DataSourceUtil.openConnection();
			UserDao userDao = new UserDaoImpl(connection);
			count=userDao.count();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DataSourceUtil.closeConnection(connection);
			return count;
		}
	}
}
