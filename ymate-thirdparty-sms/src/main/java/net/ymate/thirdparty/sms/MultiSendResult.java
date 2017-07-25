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

import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/7/24 下午5:40
 * @version 1.0
 */
public class MultiSendResult extends SendResult {

    private List<SendResult> detail = new ArrayList<SendResult>();

    public List<SendResult> getDetail() {
        return detail;
    }

    public static class Builder {

        MultiSendResult __target;

        public static Builder create() {
            return new Builder();
        }

        public Builder() {
            __target = new MultiSendResult();
        }

        public Builder result(int result) {
            __target.result = result;
            return this;
        }

        public Builder errMsg(String errMsg) {
            __target.errMsg = errMsg;
            return this;
        }

        public Builder ext(String ext) {
            __target.ext = ext;
            return this;
        }

        public Builder fee(int fee) {
            __target.fee = fee;
            return this;
        }

        public Builder detail(SendResult result) {
            __target.detail.add(result);
            return this;
        }

        public MultiSendResult build() {
            return __target;
        }
    }
}
