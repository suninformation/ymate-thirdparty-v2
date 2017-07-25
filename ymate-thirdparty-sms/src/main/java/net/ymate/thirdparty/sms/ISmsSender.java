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

import java.util.Collection;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/7/24 下午4:50
 * @version 1.0
 */
public interface ISmsSender {

    ISmsSender to(String mobile);

    ISmsSender to(String nationCode, String mobile);

    ISmsSender to(To to);

    ISmsSender to(To[] to);

    ISmsSender to(Collection<To> to);

    ISmsSender tmplateId(String tmplateId);

    ISmsSender param(String param);

    SendResult send() throws Exception;

    SendResult send(String message) throws Exception;

    SendResult send(int type, String message) throws Exception;

    class To {

        private String nationCode;

        private String mobile;

        public To(String mobile) {
            this.nationCode = "86";
            this.mobile = mobile;
        }

        public To(String nationCode, String mobile) {
            this.nationCode = nationCode;
            this.mobile = mobile;
        }

        public String getNationCode() {
            return nationCode;
        }

        public String getMobile() {
            return mobile;
        }
    }
}
