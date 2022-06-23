package com.mvchibernate.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mvchibernate.model.Address;
import com.mvchibernate.model.User;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;

	private static Logger logger = Logger.getLogger(UserDaoImpl.class.getName());
	Transaction tx = null;

	@Override
	public void userRegister(User user) {

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
			// logger.info("Transaction sucessfull!!");
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				logger.info("Transaction is being rolled back..........");
			}
		}
	}

	@Override
	public boolean userLogin(User user) {
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			Query query = session.createQuery("FROM User u WHERE u.userEmail = :email and u.userPassword = :password");
			query.setParameter("email", user.getUserEmail());
			query.setParameter("password", user.getUserPassword());
			List<User> result = query.list();
			user.setUserStatus(result.get(0).getUserStatus());
			tx.commit();
			if (result.isEmpty()) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				logger.info("Transaction is being rolled back..........");
			}
		}
		return false;
	}

	@Override
	public void updatePassword(User user) {
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("UPDATE User u SET u.userPassword = :password WHERE u.userEmail = :email");
			query.setParameter("email", user.getUserEmail());
			query.setParameter("password", user.getUserPassword());
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				logger.info("Transaction is being rolled back..........");
			}
		}
	}

	@Override
	public List<User> userDetail(User user) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM User u WHERE u.userEmail = :email and u.userPassword = :password");
		query.setParameter("email", user.getUserEmail());
		query.setParameter("password", user.getUserPassword());
		List<User> result = query.list();
		return result;

	}

	@Override
	public List<User> adminDetail(User user) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM User u WHERE u.userStatus = 1 ");
		List<User> result = query.list();
		return result;
	}

	@Override
	public List<Address> userAddress(int id) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM Address a WHERE a.user.userId =:id");
		query.setParameter("id", id);
		List<Address> result = query.list();
		return result;
	}

	@Override
	public List<User> userIdDetail(int id) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM User a WHERE a.userId =:id");
		query.setParameter("id", id);
		List<User> result = query.list();
		return result;
	}

	@Override
	public List<User> viewUser(User user) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM User u WHERE u.userStatus = 0");
		List<User> result = query.list();
		return result;
	}

	@Override
	public boolean checkMail(String email) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query query = session.createQuery("SELECT u.userEmail FROM User u WHERE u.userEmail =:email");
		query.setParameter("email", email);
		List mailList = query.list();
		if (!mailList.isEmpty()) {
			return true;
		}

		return false;
	}

	@Override
	public void deleteUser(int id) {
		System.out.println("delete id" + id);
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			User user = session.get(User.class, id);
			if (user != null) {
				session.delete(user);
				tx.commit();
			}
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		}
	}

	@Override
	public void changeRole(int id) {
		System.out.println("inside method");
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			Query query = session.createQuery("UPDATE User u SET u.userStatus=1 WHERE u.userId =:id");
			query.setParameter("id", id);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		}
	}

	@Override
	public void updateUser(User user) {
		System.out.println("user in update" + user);
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			session.update(user);
			tx.commit();
			logger.info("Transaction sucessfull!!");
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				logger.info("Transaction is being rolled back..........");
			}
		}

	}

	public List removeId(Session session, int userId) {
		System.out.println("inside remove address");
		// get address id of user from database
		Query query = session.createQuery("select addId FROM Address WHERE user_id =:userId");
		query.setParameter("userId", userId);
		List list = query.list();
		System.out.println("list in remove" + list);
		return list;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void updateAddress(List<Address> addList) {

		System.out.println("inside address update");
		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {

			tx = session.beginTransaction();
			List addressId = new ArrayList();
			
			// get addressid
			for (int j = 0; j < addList.size(); j++) {
				addressId.add(addList.get(j).getAddressId());
			}

			// get existing id
			List existingId = removeId(session, addList.get(0).getUser().getUserId());
			for (int k = 0; k < existingId.size(); k++) {
				if (!addressId.contains(String.valueOf(existingId.get(k)))) {
					int addrId = (Integer) existingId.get(k);
					Query query = session.createQuery("DELETE from  Address WHERE addId = :id");
					query.setParameter("id", addrId);
					query.executeUpdate();
				}
			}

			for (int i = 0; i < addList.size(); i++) {
				String addrId = addList.get(i).getAddressId();
				if (addrId.equals("")) {
					session.save(addList.get(i));
				} else {
					addList.get(i).setAddId(Integer.parseInt(addrId));
					session.update(addList.get(i));
				}
			}
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				logger.info("Transaction is being rolled back..........");
			}
		}
	}

}
