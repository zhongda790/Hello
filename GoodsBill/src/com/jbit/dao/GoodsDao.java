package com.jbit.dao;

import java.util.List;
import com.jbit.base.BaseDao;
import com.jbit.entity.Goods;

public interface GoodsDao extends BaseDao<Goods>{
   
	public List<Goods> getAll(int id);
	
	
}
