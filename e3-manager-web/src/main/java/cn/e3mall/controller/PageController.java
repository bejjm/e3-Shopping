package cn.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转Controller
 */
@Controller
public class PageController {

    //进入主页
    @RequestMapping("/")
    public String showIndex(){
        return "index";
    }

    //进入其他页面，
    // 前台请求 http://localhost:8081/item-list?_=1517210921151
    // 就进入item-list.jsp
    @RequestMapping("/{page}")
    public String showPage(@PathVariable String page){
        return page;
    }

}
