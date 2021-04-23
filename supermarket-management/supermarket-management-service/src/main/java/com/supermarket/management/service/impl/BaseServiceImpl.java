package com.supermarket.management.service.impl;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.supermarket.management.pojo.BasePojo;
import com.supermarket.management.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class BaseServiceImpl<T extends BasePojo> implements BaseService<T> {

    /**
     * 1.	queryById
     * 2.	queryAll
     * 3.	queryCountByWhere
     * 4.	queryListByWhere
     * 5.	queryByPage
     * 6.	queryOne
     * 7.	save
     * 8.	updateById
     * 9.	deleteById
     * 10.	deleteByIds
     *
     * @param id
     */

    @Autowired
    private Mapper<T> mapper;
    private Class<T> clazz;
    public BaseServiceImpl(){
        
        Type type =this.getClass().getGenericSuperclass();
        ParameterizedType Ptype = (ParameterizedType) type;
        this.clazz = (Class<T>) Ptype.getActualTypeArguments()[0];

    }
    @Override
    public T queryById(Long id) {
        T t = this.mapper.selectByPrimaryKey(id);
        return t;
    }

    @Override
    public List<T> queryAll() {
        List<T> list = this.mapper.select(null);
        return list;
    }

    @Override
    public Integer queryCountByWhere(T t) {
        int count = this.mapper.selectCount(t);
        return count;
    }

    @Override
    public List<T> queryListByWhere(T t) {
        List<T> list = this.mapper.select(t);
        return list;
    }

    @Override
    public List<T> queryByPage(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        List<T> list = this.mapper.select(null);
        return list;
    }

    @Override
    public T queryOne(T t) {
        return this.mapper.selectOne(t);
    }

    @Override
    public void save(T t) {
        if(t.getCreated()==null){
            t.setCreated(new Date());
            t.setUpdated(t.getCreated());
        }else if(t.getUpdated()==null){
            t.setUpdated(t.getCreated());
        }
        this.mapper.insert(t);

    }

    @Override
    public void saveSelective(T t) {
        if(t.getCreated()==null){
            t.setCreated(new Date());
            t.setUpdated(t.getCreated());
        }else if(t.getUpdated()==null){
            t.setUpdated(t.getCreated());
        }
         this.mapper.insertSelective(t);
    }

    @Override
    public void updateByIdSelective(T t) {
        t.setUpdated(new Date());
        this.mapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public void updateById(T t) {
        t.setUpdated(new Date());
        this.mapper.updateByPrimaryKey(t);
    }

    @Override
    public void deleteById(Long id) {
this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByIds(List<Object> ids) {
Example example=new Example(this.clazz);
example.createCriteria().andIn("id",ids);
this.mapper.deleteByExample(example);
    }
}
