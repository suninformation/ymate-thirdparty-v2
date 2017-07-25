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
package net.ymate.thirdparty.sms;

import net.ymate.platform.core.lang.BlurObject;
import net.ymate.thirdparty.core.IThirdpartyModuleCfg;
import net.ymate.thirdparty.core.Thirdparty;
import org.apache.commons.lang.StringUtils;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/7/25 下午3:38
 * @version 1.0
 */
public class SendConfig {

    private static SendConfig __instance;

    public static SendConfig getInstance() {
        if (__instance == null) {
            synchronized (SendConfig.class) {
                if (__instance == null) {
                    __instance = new SendConfig();
                }
            }
        }
        return __instance;
    }

    private int __batchSize;

    private int __messageLength;

    private int __minuteCount;

    private int __hourCount;

    private int __dayCount;

    private String __cacheName;

    private SendConfig() {
        IThirdpartyModuleCfg _moduleCfg = Thirdparty.get().getModuleCfg();
        __batchSize = BlurObject.bind(StringUtils.defaultIfBlank(_moduleCfg.getCommonsParam("sms.batch_size"), "100")).toIntValue();
        if (__batchSize <= 0) {
            __batchSize = 100;
        }
        __messageLength = BlurObject.bind(StringUtils.defaultIfBlank(_moduleCfg.getCommonsParam("sms.message_length"), "400")).toIntValue();
        if (__messageLength <= 0) {
            __messageLength = 400;
        }
        //
        // 运营商规则：
        // 聚合：运营商限制同1个号码同1个签名的内容1分钟内只能接收1条，1小时内只能接收3条，一天最多接收10条，否则可能会被运营商屏蔽，短信api接口本身不限制发送频率，具体发送频率需要用户自行设置。
        // 腾讯：对于验证码、通知类短信，同一号码同一内容30秒内最多发送1条；对于营销类短信，同一号码同一内容24小时内最多发送3条，同一号码不同内容24小时内最多发送15条。
        //
        __minuteCount = BlurObject.bind(StringUtils.defaultIfBlank(_moduleCfg.getCommonsParam("sms.minute_count"), "1")).toIntValue();
        __hourCount = BlurObject.bind(StringUtils.defaultIfBlank(_moduleCfg.getCommonsParam("sms.hour_count"), "3")).toIntValue();
        __dayCount = BlurObject.bind(StringUtils.defaultIfBlank(_moduleCfg.getCommonsParam("sms.day_count"), "10")).toIntValue();
        //
        __cacheName = StringUtils.defaultIfBlank(_moduleCfg.getCommonsParam("sms.cache_name"), "sms_send_cache");
    }

    public int batchSize() {
        return __batchSize;
    }

    public int messageLength() {
        return __messageLength;
    }

    public int minuteCount() {
        return __minuteCount;
    }

    public int dayCount() {
        return __dayCount;
    }

    public int hourCount() {
        return __hourCount;
    }

    public String cacheName() {
        return __cacheName;
    }
}
