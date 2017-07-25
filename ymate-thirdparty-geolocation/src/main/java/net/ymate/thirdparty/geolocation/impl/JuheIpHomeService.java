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
import net.ymate.thirdparty.geolocation.IJuheIpHomeService;
import net.ymate.thirdparty.geolocation.LocationResult;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/12/19 上午4:47
 * @version 1.0
 */
@Service(IJuheIpHomeService.__NAME)
public class JuheIpHomeService extends AbstractThirdpartyService implements IJuheIpHomeService {

    public JuheIpHomeService() {
        super("service_key");
    }

    @Override
    public LocationResult getIpInfo(String ipAddr) throws Exception {
        if (StringUtils.isBlank(ipAddr)) {
            throw new NullArgumentException("ipAddr");
        }
        Map<String, String> _params = new HashMap<String, String>();
        _params.put("key", getServiceKey());
        _params.put("ip", ipAddr);
        IHttpResponse _response = HttpClientHelper.create().get("http://apis.juhe.cn/ip/ip2addr", _params);
        if (_response != null && _response.getStatusCode() == 200) {
            JSONObject _json = JSON.parseObject(_response.getContent());
            LocationResult.Builder _builder = LocationResult.Builder.create()
                    .result(_json.getString("status"))
                    .errMsg(_json.getString("message"));
            //
            JSONObject _data = _json.getJSONObject("result");
            if (_data != null) {
                _builder.area(_data.getString("area"))
                        .location(_data.getString("location"));
            }
            return _builder.build();
        }
        return null;
    }
}
