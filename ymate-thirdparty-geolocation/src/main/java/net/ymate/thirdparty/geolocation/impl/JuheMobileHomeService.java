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
package net.ymate.thirdparty.geolocation.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.ymate.framework.commons.HttpClientHelper;
import net.ymate.framework.commons.IHttpResponse;
import net.ymate.thirdparty.core.AbstractThirdpartyService;
import net.ymate.thirdparty.core.annotation.Service;
import net.ymate.thirdparty.geolocation.IJuheMobileHomeService;
import net.ymate.thirdparty.geolocation.LocationResult;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/12/19 上午4:47
 * @version 1.0
 */
@Service(IJuheMobileHomeService.__NAME)
public class JuheMobileHomeService extends AbstractThirdpartyService implements IJuheMobileHomeService {

    public JuheMobileHomeService() {
        super("service_key");
    }

    @Override
    public LocationResult getMobileInfo(String mobile) throws Exception {
        if (StringUtils.isBlank(mobile)) {
            throw new NullArgumentException("mobile");
        }
        Map<String, String> _params = new HashMap<String, String>();
        _params.put("key", getServiceKey());
        _params.put("phone", mobile);
        IHttpResponse _response = HttpClientHelper.create().get("http://apis.juhe.cn/mobile/get", _params);
        if (_response != null && _response.getStatusCode() == 200) {
            JSONObject _json = JSON.parseObject(_response.getContent());
            LocationResult.Builder _builder = LocationResult.Builder.create()
                    .result(_json.getString("status"))
                    .errMsg(_json.getString("message"));
            //
            JSONObject _data = _json.getJSONObject("result");
            if (_data != null) {
                _builder.province(_data.getString("province"))
                        .city(_data.getString("city"))
                        .area(_data.getString("areacode"))
                        .zip(_data.getString("zip"))
                        .company(_data.getString("company"))
                        .card(_data.getString("card"));
            }
            return _builder.build();
        }
        return null;
    }
}
