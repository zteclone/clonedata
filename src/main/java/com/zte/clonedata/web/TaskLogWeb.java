package com.zte.clonedata.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zte.clonedata.dao.TaskLogMapper;
import com.zte.clonedata.model.TaskLog;
import com.zte.clonedata.model.TaskLogExample;
import com.zte.clonedata.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ProjectName: clonedata-com.zte.clonedata.web
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 13:15 2020/6/12
 * @Description:
 */
@RestController
@RequestMapping("taskLog")
public class TaskLogWeb {

    @Autowired
    private TaskLogMapper taskLogMapper;


    @GetMapping("/detail")
    public ResponseUtils detail(@RequestParam("taskId") String taskId
            ,@RequestParam("page") Integer page
            , @RequestParam("limit") Integer limit
    ){
        TaskLogExample taskLogExample = new TaskLogExample();
        TaskLogExample.Criteria criteria = taskLogExample.createCriteria();
        criteria.andTaskIdEqualTo(taskId);
        taskLogExample.setOrderByClause("begintime desc");
        PageHelper.startPage(page,limit);
        List<TaskLog> taskLogs = taskLogMapper.selectByExample(taskLogExample);
        PageInfo<TaskLog> pages = new PageInfo<>(taskLogs);
        return ResponseUtils.success((int) pages.getTotal(),taskLogs);
    }


    @GetMapping("/del")
    public ResponseUtils del(@RequestParam("id") String id){
        try {
            taskLogMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            return ResponseUtils.success("删除失败: "+ e.getMessage());
        }
        return ResponseUtils.success("删除成功");
    }


}
