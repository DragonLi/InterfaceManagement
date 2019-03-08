package com.asiainfo.cem.vace.services.interfaces;

import com.ai.aif.csf.common.annotation.Center;
import com.ai.aif.csf.common.annotation.ServiceAttributes;

import java.text.ParseException;
import java.util.Map;

import com.asiainfo.cem.protocol.uesHmData.FindHMInput;
import com.asiainfo.cem.protocol.uesHmData.ResultUesHmData;

@Center("ord")
@ServiceAttributes(centerCode = "ord")
public interface ICemUserDataServiceCSV {
    ResultUesHmData findHm(FindHMInput clientParam);
}