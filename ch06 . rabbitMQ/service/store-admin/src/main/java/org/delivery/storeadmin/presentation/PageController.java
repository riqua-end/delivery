package org.delivery.storeadmin.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("")
public class PageController {

    @RequestMapping(path = {"/","/main"}) // SpringBoot 3 버젼부터 root path 는 "" 이 적용되지 않음
    public ModelAndView main(){
        return new ModelAndView("main");
    }

    @RequestMapping("/order")
    public ModelAndView order(){
        return new ModelAndView("order/order");
    }
}
