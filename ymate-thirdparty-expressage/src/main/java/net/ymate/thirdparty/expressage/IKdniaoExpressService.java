/*
 * Copyright 2007-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.thirdparty.expressage;

/**
 * 快递鸟(http://www.kdniao.com/)
 *
 * @author 刘镇 (suninformation@163.com) on 2017/7/22 下午1:35
 * @version 1.0
 */
public interface IKdniaoExpressService {

    String __NAME = "expressage.kdniao";

    String __GATEWAY_URL = "http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";

    String __SUBSCRIBE_URL = "http://api.kdniao.cc/api/dist";

    /**
     * @param shipperCode  快递公司编码
     * @param logisticCode 物流单号
     * @return 执行物流状态即时查询并返回结果
     * @throws Exception 可能产生的任何异常
     */
    ExpressResult query(String shipperCode, String logisticCode) throws Exception;

    /**
     * @param shipperCode  快递公司编码
     * @param logisticCode 物流单号
     * @return 订阅物流跟踪信息推送并返回成功与否
     * @throws Exception 可能产生的任何异常
     */
    boolean subscribe(String shipperCode, String logisticCode) throws Exception;
}
