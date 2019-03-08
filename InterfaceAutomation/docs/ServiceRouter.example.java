package com.asiainfo.cem.controller;

import com.ai.aif.csf.client.service.factory.CsfServiceFactory;
import com.ai.aif.csf.client.service.interfaces.IClientSV;
import com.ai.aif.csf.common.exception.CsfException;

import com.asiainfo.cem.common.Constants;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import com.asiainfo.cem.protocol.uesHmData.FindHMInput;
import com.asiainfo.cem.protocol.uesHmData.ResultUesHmData;

@Controller
@RequestMapping("/hmDate")
public class BusinessController {
    @PostMapping("/uesHmData")
    @ResponseBody
    public ResultUesHmData uesHmData(@RequestBody FindHMInput postData) {
        ResultUesHmData remoteResult=null;
        try {
            IClientSV client = CsfServiceFactory.getService("ord_ICemUserDataServiceCSV_findHm1");
            Map clientParam=Maps.newHashMap();
            clientParam.put("clientParam",postData);
            remoteResult = (ResultUesHmData) client.service(clientParam);
        } catch (Exception e) {
            log.error("热力图数据查询错误", e);
            //TODO if debug return stack trace of the exception else use predefined error message to avoid information leakage
            remoteResult=new ResultUesHmData();
            remoteResult.resultCode=99999;
            remoteResult.resultMessage=Constants.errorMessage;
        }
        return remoteResult;
    }
}