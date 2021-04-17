package com.supermarket.management.service.impl;
import com.supermarket.management.pojo.ContentCategory;
import com.supermarket.management.service.ContentCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ContentCategoryServiceImpl extends BaseServiceImpl<ContentCategory> implements ContentCategoryService {


    @Override
    public List<ContentCategory> queryContentCategoryListByParentId(Long parentId) {
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setParentId(parentId);
        List<ContentCategory> list = super.queryListByWhere(contentCategory);
        return list;
    }

    @Override
    public ContentCategory saveContentCategory(ContentCategory contentCategory) {
        // 保存新增节点
        contentCategory.setStatus(1);
        contentCategory.setIsParent(false);
        super.save(contentCategory);
        // 查询当前新增节点的父节点
        ContentCategory parent = super.queryById(contentCategory.getParentId());
        // 如果父节点为叶节点时，更新父节点的IsParent属性为true
        if (!parent.getIsParent()) {
            parent.setIsParent(true);
            super.updateByIdSelective(parent);
        }
        return contentCategory;

    }

    @Override
    public void deleteContentCategory(Long parentId, Long id) {
        // 计算出要删除的id列表
        List<Object> ids = new ArrayList<Object>();
        // 本身节点加入删除列表
        ids.add(id);
        // 查询当前要删除的节点下的所有子节点
        getDeleteIds(id, ids);
        // 删除当前节点下所有的子节点，包括本身
        super.deleteByIds(ids);
        // 跟据父ID查询子节点
        ContentCategory category = new ContentCategory();
        category.setParentId(parentId);
        //查询当前要删除的节点的父节点下还有没有子节点
        Integer countByWhere = super.queryCountByWhere(category);
        //如果查询结果为0，说明没有子节点
        if(countByWhere == 0){
            //更新当前父节点为叶子节点
            ContentCategory parent = new ContentCategory();
            parent.setId(parentId);
            parent.setIsParent(false);
            super.updateByIdSelective(parent);
        }
    }

    /**
     * 跟据节点ID查询所有子节点的ID
     *
     * @param id
     *            当前要删除的节点ID
     * @param ids
     *            子节点列表
     */
    private void getDeleteIds(Long id, List<Object> ids) {
        // 跟据父ID查询子节点
        ContentCategory category = new ContentCategory();
        category.setParentId(id);
        List<ContentCategory> list = super.queryListByWhere(category);
        // 找到了子节点
        if (list != null && list.size() > 0) {
            for (ContentCategory contentCategory : list) {
                // 把子节点ID返回
                ids.add(contentCategory.getId());

                // 如果当前循环节点是个父节点
                if (contentCategory.getIsParent()) {
                    // 递归查询所有子节点
                    getDeleteIds(contentCategory.getId(), ids);
                }
            }
        }
    }



    /**

    @Override
    public void saveContentCategory(Long parentId, String name) {
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        super.saveSelective(contentCategory);
    }

    @Override
    public void updateContentCategory(Long contentCategoryId, String name) {
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setId(contentCategoryId);
        contentCategory.setName(name);
        super.updateByIdSelective(contentCategory);
    }

    @Override
    public void deleteContentCategory(Long id, Long parentId) {
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setParentId(parentId);
        contentCategory.setId(id);
        super.deleteById(id);
    }
    */
}
