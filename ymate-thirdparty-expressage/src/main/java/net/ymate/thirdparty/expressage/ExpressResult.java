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

import java.util.ArrayList;
import java.util.List;

/**
 * 快递查询结果
 *
 * @author 刘镇 (suninformation@163.com) on 2017/7/22 下午4:07
 * @version 1.0
 */
public class ExpressResult {

    private String result;

    private String errMsg;

    /**
     * 物流公司代码
     */
    private String shipperCode;

    /**
     * 快递单号
     */
    private String logisticCode;

    /**
     * 投送状态
     */
    private String status;

    /**
     * 物流跟踪记录
     */
    private List<TrackInfo> traces = new ArrayList<TrackInfo>();

    public String getResult() {
        return result;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public String getShipperCode() {
        return shipperCode;
    }

    public String getLogisticCode() {
        return logisticCode;
    }

    public String getStatus() {
        return status;
    }

    public List<TrackInfo> getTraces() {
        return traces;
    }

    /**
     * 物流跟踪信息
     */
    public static class TrackInfo {

        private String trackTime;

        private String content;

        private String remark;

        public TrackInfo(String trackTime, String content) {
            this.trackTime = trackTime;
            this.content = content;
        }

        public TrackInfo(String trackTime, String content, String remark) {
            this.trackTime = trackTime;
            this.content = content;
            this.remark = remark;
        }

        public String getTrackTime() {
            return trackTime;
        }

        public String getContent() {
            return content;
        }

        public String getRemark() {
            return remark;
        }
    }

    public static class Builder {

        private ExpressResult __target;

        public static Builder create() {
            return new Builder();
        }

        public Builder() {
            __target = new ExpressResult();
        }

        public ExpressResult build() {
            return __target;
        }

        public Builder result(String result) {
            __target.result = result;
            return this;
        }

        public Builder errMsg(String errMsg) {
            __target.errMsg = errMsg;
            return this;
        }

        public Builder shipperCode(String shipperCode) {
            __target.shipperCode = shipperCode;
            return this;
        }

        public Builder logisticCode(String logisticCode) {
            __target.logisticCode = logisticCode;
            return this;
        }

        public Builder status(String status) {
            __target.status = status;
            return this;
        }

        public Builder trackInfo(String trackTime, String content) {
            __target.traces.add(new TrackInfo(trackTime, content));
            return this;
        }

        public Builder trackInfo(String trackTime, String content, String remark) {
            __target.traces.add(new TrackInfo(trackTime, content, remark));
            return this;
        }
    }
}
