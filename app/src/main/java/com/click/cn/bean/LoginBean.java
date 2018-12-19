package com.click.cn.bean;

import com.click.cn.base.Result;

import java.io.Serializable;

/**
 * Created by zhj on 2017/3/7.
 */

public class LoginBean  extends Result implements Serializable {
    /**
     * codeDesc : SUCCESS
     * code : 1000
     * data : [{"terminalId":6,"remark":"sdfsfwecaxc","lastReportTime":"2017-03-01 10:38:46","lastLng":10.3,"lastLat":10.4,"terminalMac":"12345678"}]
     */


    private int code;
    private String codeDesc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }
}
