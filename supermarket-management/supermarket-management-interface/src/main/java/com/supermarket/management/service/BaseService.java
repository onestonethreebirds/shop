package com.supermarket.management.service;
import java.util.List;

public interface BaseService<T> {
 /**   1.	queryById
    2.	queryAll
    3.	queryCountByWhere
    4.	queryListByWhere
    5.	queryByPage
    6.	queryOne
    7.	save
    8.	updateById
    9.	deleteById
    10.	deleteByIds
*/
 public T queryById(Long id);
 public List<T> queryAll();
 public Integer queryCountByWhere(T t);
 public List<T> queryListByWhere(T t);
 public List<T> queryByPage(Integer page,Integer rows);
 public T queryOne(T t);
 public void save(T t);
 public void saveSelective(T t);
 public void updateByIdSelective(T t);
 public void updateById(T t);
 public void deleteById(Long id);
 public void deleteByIds(List<Object> ids);

}
