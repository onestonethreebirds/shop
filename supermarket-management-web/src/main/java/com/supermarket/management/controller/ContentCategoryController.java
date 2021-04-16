package com.supermarket.management.controller;

import com.supermarket.management.pojo.ContentCategory;
import com.supermarket.management.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("content/category")
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
     public List<ContentCategory> queryContentCategoryListByParentId(@RequestParam(value = "id",defaultValue = "0") Long ParentId){
        List<ContentCategory> list=this.contentCategoryService.queryContentCategoryListByParentId(ParentId);
        return list;

    }
    /**
@RequestMapping(value = "add",method = RequestMethod.POST)
@ResponseBody
public void add(@RequestParam(value = "parentId",defaultValue = "1") Long parentId,@RequestParam(value = "name") String name){
       this.contentCategoryService.saveContentCategory(parentId,name);

}
@RequestMapping(value = "update",method = RequestMethod.POST)
@ResponseBody
public  void update(@RequestParam(value = "id") Long ContentCategoryId,@RequestParam(value = "name") String name){
this.contentCategoryService.updateContentCategory(ContentCategoryId,name);
}
@RequestMapping(value = "delete",method = RequestMethod.POST)
@ResponseBody
public void delete(@RequestParam(value = "id") Long id,@RequestParam(value = "parentId") Long parentId){
this.contentCategoryService.deleteContentCategory(id,parentId);
}
*/
    @RequestMapping(value="add",method=RequestMethod.POST)
    @ResponseBody
    public ContentCategory saveContentCategory(ContentCategory contentCategory){
        ContentCategory result = null;
        try {
            result = contentCategoryService.saveContentCategory(contentCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // url: "/rest/content/category/update",
    /**
     * 更新
     * @param category
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public String update(ContentCategory category) {
        String msg = "0";
        try {
            contentCategoryService.updateByIdSelective(category);
        } catch (Exception e) {
            msg = "1";
        }
        return msg;
    }

    //parentId=${节点的父id}&id=${选中节点的id}
//	   url: "/rest/content/category/delete",
//	   data : {parentId:node.parentId,id:node.id},
    /**
     * 删除
     * @param parentId
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public String deleteContentCategory(Long parentId,Long id){
        String msg = "0";
        try {
            contentCategoryService.deleteContentCategory(parentId,id);
        } catch (Exception e) {
            msg = "1";
        }
        return msg;
    }



}
