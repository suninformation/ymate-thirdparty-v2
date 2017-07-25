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

/**
 * @author 刘镇 (suninformation@163.com) on 2017/7/24 下午5:04
 * @version 1.0
 */
public class SendResult {

    protected int result;

    protected String errMsg;

    protected String mobile;

    private String country;

    protected String ext;

    private String sid;

    protected int fee;

    public int getResult() {
        return result;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCountry() {
        return country;
    }

    public String getExt() {
        return ext;
    }

    public String getSid() {
        return sid;
    }

    public int getFee() {
        return fee;
    }

    public static class Builder {

        private SendResult __target;

        public static Builder create() {
            return new Builder();
        }

        public Builder() {
            __target = new SendResult();
        }

        public Builder result(int result) {
            __target.result = result;
            return this;
        }

        public Builder errMsg(String errMsg) {
            __target.errMsg = errMsg;
            return this;
        }

        public Builder mobile(String mobile) {
            __target.mobile = mobile;
            return this;
        }

        public Builder country(String country) {
            __target.country = country;
            return this;
        }

        public Builder ext(String ext) {
            __target.ext = ext;
            return this;
        }

        public Builder sid(String sid) {
            __target.sid = sid;
            return this;
        }

        public Builder fee(int fee) {
            __target.fee = fee;
            return this;
        }

        public SendResult build() {
            return __target;
        }
    }
}
