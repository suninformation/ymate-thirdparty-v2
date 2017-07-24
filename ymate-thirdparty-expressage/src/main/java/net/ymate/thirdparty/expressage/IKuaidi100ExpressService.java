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
 * 快递100(http://www.kuaidi100.com)
 *
 * @author 刘镇 (suninformation@163.com) on 16/12/19 上午3:58
 * @version 1.0
 */
public interface IKuaidi100ExpressService {

    String __NAME = "expressage.kuaidi100";

    String __GATEWAY_URL = "http://api.kuaidi100.com/api";

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
     * @return 返回物流查询页面URL地址，用于PC端前台跳转
     */
    String redirectUrl(String shipperCode, String logisticCode);

    /**
     * @param shipperCode  快递公司编码
     * @param logisticCode 物流单号
     * @param callbackUrl  查询结果页面点击"返回"时跳转的地址
     * @return 返回物流查询页面URL地址，用于移动手机端前台跳转
     */
    String redirectUrl(String shipperCode, String logisticCode, String callbackUrl);
}
