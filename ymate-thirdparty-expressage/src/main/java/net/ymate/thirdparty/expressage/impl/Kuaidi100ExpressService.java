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
package net.ymate.thirdparty.expressage.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import net.ymate.framework.commons.HttpClientHelper;
import net.ymate.framework.commons.IHttpResponse;
import net.ymate.thirdparty.core.AbstractThirdpartyService;
import net.ymate.thirdparty.core.annotation.Service;
import net.ymate.thirdparty.expressage.ExpressResult;
import net.ymate.thirdparty.expressage.IKuaidi100ExpressService;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/12/19 上午4:40
 * @version 1.0
 */
@Service(IKuaidi100ExpressService.__NAME)
public class Kuaidi100ExpressService extends AbstractThirdpartyService implements IKuaidi100ExpressService {

    public Kuaidi100ExpressService() {
        super("service_key");
    }

    @Override
    public ExpressResult query(String shipperCode, String logisticCode) throws Exception {
        if (StringUtils.isBlank(shipperCode)) {
            throw new NullArgumentException("shipperCode");
        }
        if (StringUtils.isBlank(logisticCode)) {
            throw new NullArgumentException("logisticCode");
        }
        //
        Map<String, String> _params = new HashMap<String, String>();
        _params.put("id", this.getServiceKey());
        _params.put("com", shipperCode);
        _params.put("nu", logisticCode);
        _params.put("order", "desc");
        IHttpResponse _response = HttpClientHelper.create().get(__GATEWAY_URL, _params);
        if (_response != null && _response.getStatusCode() == 200) {
            JSONObject _json = JSON.parseObject(_response.getContent(), Feature.OrderedField);
            ExpressResult.Builder _builder = ExpressResult.Builder.create()
                    .result(_json.getString("status"))
                    .errMsg(_json.getString("message"))
                    .status(_json.getString("state"));
            //
            JSONArray _data = _json.getJSONArray("data");
            if (_data != null) {
                for (Object _d : _data) {
                    JSONObject _item = (JSONObject) JSONObject.toJSON(_d);
                    _builder.trackInfo(_item.getString("time"), _item.getString("context"), null);
                }
            }
            return _builder.build();
        }
        return null;
    }

    @Override
    public String redirectUrl(String shipperCode, String logisticCode) {
        if (StringUtils.isBlank(shipperCode)) {
            throw new NullArgumentException("shipperCode");
        }
        if (StringUtils.isBlank(logisticCode)) {
            throw new NullArgumentException("logisticCode");
        }
        return "http://www.kuaidi100.com/chaxun?com=" + shipperCode + "&nu=" + logisticCode;
    }

    @Override
    public String redirectUrl(String shipperCode, String logisticCode, String callbackUrl) {
        if (StringUtils.isBlank(shipperCode)) {
            throw new NullArgumentException("shipperCode");
        }
        if (StringUtils.isBlank(logisticCode)) {
            throw new NullArgumentException("logisticCode");
        }
        return "https://m.kuaidi100.com/index_all.html?type=" + shipperCode + "&postid=" + logisticCode + "&callbackurl=" + callbackUrl;
    }
}
