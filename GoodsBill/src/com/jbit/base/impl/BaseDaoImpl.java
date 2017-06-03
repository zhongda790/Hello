package com.jbit.base.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.jbit.base.BaseDao;

@SuppressWarnings("all")
public abstract class BaseDaoImpl<T> implements BaseDao<T> {
	private Class<T> entityClass;
	protected HibernateTemplate hibernateTemplate;

	public BaseDaoImpl() {
		// �����౻�̳�ʱ�����������Ҫ��ʼ��T��ͨ��ô����ȡT������
		entityClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	public void delete(T instance) {
		this.hibernateTemplate.delete(instance);
	}

	@Override
	public Integer executeByHql(final String hql, final Object... paras) {
		return this.hibernateTemplate
				.executeWithNativeSession(new HibernateCallback<Integer>() {
					public Integer doInHibernate(Session session)
							throws HibernateException {
						Query query = session.createQuery(hql);
						// hibernateTemplate.prepareQuery(query);
						if (paras != null) {
							for (int i = 0; i < paras.length; i++) {
								query.setParameter(i, paras[i]);
							}
						}
						return query.executeUpdate();
					}
				});
	}

	@Override
	public List<T> findAll() {
		String hql = "from " + this.entityClass.getSimpleName();
		List<T> list = hibernateTemplate.find(hql);
		return list;
	}

	@Override
	public List findByHql(String hql, Object... paras) {
		return hibernateTemplate.find(hql, paras);
	}

	@Override
	public List findByHqlInCache(final String hql, final Object... paras) {
		return hibernateTemplate
				.executeWithNativeSession(new HibernateCallback<List>() {
					public List doInHibernate(Session session)
							throws HibernateException {
						Query query = session.createQuery(hql);
						// hibernateTemplate.prepareQuery(query);
						query.setCacheable(true);
						if (paras != null) {
							for (int i = 0; i < paras.length; i++) {
								query.setParameter(i, paras[i]);
							}
						}
						return query.list();
					}
				});
	}

	@Override
	public T findById(Serializable id) {
		T instance = hibernateTemplate.get(entityClass, id);
		return instance;
	}

	@Override
	public List<T> findByProperty(String propertyName, Object value) {
		String hql = "from " + this.entityClass.getSimpleName() + " where "
				+ propertyName + "=?";
		List<T> list = hibernateTemplate.find(hql, value);
		return list;
	}

	@Override
	public List findBySQLQuery(final String sql, final Object... paras) {
		return hibernateTemplate
				.executeWithNativeSession(new HibernateCallback<List>() {
					public List doInHibernate(Session session) {
						SQLQuery query = session.createSQLQuery(sql);
						if (paras != null) {
							for (int i = 0; i < paras.length; i++) {
								query.setParameter(i, paras[i]);
							}
						}
						return query.list();
					}
				});
	}

	@Override
	public List findPage(final String hql, final int maxResult,
			final int firstResult, final Object... paras) {
		return hibernateTemplate
				.executeWithNativeSession(new HibernateCallback<List>() {
					public List doInHibernate(Session session) {
						Query query = session.createQuery(hql);
						// hibernateTemplate.prepareQuery(query);
						query.setMaxResults(maxResult);
						query.setFirstResult((firstResult - 1) * maxResult);
						if (paras != null) {
							for (int i = 0; i < paras.length; i++) {
								query.setParameter(i, paras[i]);
							}
						}
						return query.list();
					}
				});
	}

	@Override
	public Serializable save(T instance) {
		return this.hibernateTemplate.save(instance);
	}

	@Override
	public void saveOrUpdate(T instance) {
		this.hibernateTemplate.saveOrUpdate(instance);
	}

	@Override
	public Object uniqueResult(final String hql, final Object... paras) {
		return hibernateTemplate
				.executeWithNativeSession(new HibernateCallback<Object>() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Query query = session.createQuery(hql);
						// hibernateTemplate.prepareQuery(query);
						if (paras != null) {
							for (int i = 0; i < paras.length; i++) {
								query.setParameter(i, paras[i]);
							}
						}
						return query.uniqueResult();
					}
				});
	}

	@Override
	public void update(T instance) {
		this.hibernateTemplate.update(instance);
	}

	private void convertToTransient(T instance) {
		hibernateTemplate.getSessionFactory().getCurrentSession().lock(
				instance, LockMode.NONE);
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
