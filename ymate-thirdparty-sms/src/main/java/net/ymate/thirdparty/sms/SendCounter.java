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

import net.ymate.framework.commons.DateTimeHelper;
import net.ymate.platform.core.util.DateTimeUtils;

import java.io.Serializable;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/7/25 上午11:57
 * @version 1.0
 */
public class SendCounter implements Serializable {

    private String __mobile;

    private long __lastSendTime;

    private int __hourCount;

    private int __dayCount;

    public SendCounter(String mobile) {
        __mobile = mobile;
        __lastSendTime = System.currentTimeMillis();
    }

    public boolean canSend(SendConfig sendConfig) {
        return canSend(sendConfig.minuteCount(), sendConfig.hourCount(), sendConfig.dayCount());
    }

    /**
     * @param minuteCount 每分钟发送次数
     * @return 验证是否可以向该号码发送短信
     */
    public boolean canSend(int minuteCount, int hourCount, int dayCount) {
        DateTimeHelper _now = DateTimeHelper.now();
        DateTimeHelper _target = DateTimeHelper.bind(__lastSendTime);
        //
        if (_now.timeMillis() - __lastSendTime > DateTimeUtils.DAY) {
            // 缓存数据已超过24小时则重置
            __hourCount = 0;
            __dayCount = 0;
        } else if (minuteCount > 0 && (_now.timeMillis() - __lastSendTime) <= (60000 / minuteCount)) {
            // 首先检测1分钟内发送次数
            return false;
        } else if (hourCount > 0) {
            if (_now.hour() == _target.hour()) {
                // 检测1小时内发送次数
                if (__hourCount >= hourCount) {
                    return false;
                }
            } else {
                __hourCount = 0;
                // 检测同一天内发送次数
                if (dayCount > 0 && _now.day() == _target.day() && __dayCount >= dayCount) {
                    return false;
                }
            }
        } else if (dayCount > 0 && _now.day() == _target.day() && __dayCount >= dayCount) {
            // 检测同一天内发送次数
            return false;
        }
        return true;
    }

    public SendCounter updateCount() {
        __lastSendTime = System.currentTimeMillis();
        __hourCount++;
        __dayCount++;
        //
        return this;
    }

    public String getMobile() {
        return __mobile;
    }

    public long getLastSendTime() {
        return __lastSendTime;
    }

    public int getHourCount() {
        return __hourCount;
    }

    public int getDayCount() {
        return __dayCount;
    }
}
