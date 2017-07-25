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
package net.ymate.thirdparty.geolocation;

import net.ymate.thirdparty.core.AbstractThirdpartyService;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.URLEncoder;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/12/21 下午11:18
 * @version 1.0
 */
public abstract class AbstractBaiduService extends AbstractThirdpartyService {

    /**
     * 接口参数签名
     *
     * @param params 参数映射
     * @param apiUrl API请求地址
     * @throws Exception 可能产生的任何异常
     */
    protected void __doBuildSN(Map<String, String> params, String apiUrl) throws Exception {
        StringBuilder _encodingStr = new StringBuilder();
        for (Map.Entry<String, String> _entry : params.entrySet()) {
            _encodingStr.append(_entry.getKey()).append("=");
            _encodingStr.append(URLEncoder.encode(_entry.getValue(), "UTF-8")).append("&");
        }
        if (_encodingStr.length() > 0) {
            _encodingStr.deleteCharAt(_encodingStr.length() - 1);
        }
        String _wholeStr = URLEncoder.encode(apiUrl + "?" + _encodingStr.toString() + getServiceKey(), "UTF-8");
        params.put("sn", DigestUtils.md5Hex(_wholeStr));
    }
}
