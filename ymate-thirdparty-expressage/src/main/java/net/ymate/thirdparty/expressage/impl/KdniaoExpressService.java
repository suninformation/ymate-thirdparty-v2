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
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.thirdparty.core.AbstractThirdpartyService;
import net.ymate.thirdparty.core.annotation.Service;
import net.ymate.thirdparty.expressage.ExpressResult;
import net.ymate.thirdparty.expressage.IKdniaoExpressService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/7/22 下午1:37
 * @version 1.0
 */
@Service(IKdniaoExpressService.__NAME)
public class KdniaoExpressService extends AbstractThirdpartyService implements IKdniaoExpressService {

    private Map<String, String> __buildRequestMap(String requestData, String requestType) throws Exception {
        Map<String, String> _params = new HashMap<String, String>();
        _params.put("RequestData", URLEncoder.encode(requestData, "UTF-8"));
        _params.put("EBusinessID", this.getServiceId());
        _params.put("RequestType", requestType);
        _params.put("DataSign", URLEncoder.encode(Base64.encodeBase64String(DigestUtils.md5Hex(requestData + this.getServiceKey()).getBytes("UTF-8")), "UTF-8"));
        _params.put("DataType", "2");
        //
        return _params;
    }

    @Override
    public ExpressResult query(String shipperCode, String logisticCode) throws Exception {
        Map<String, String> _params = __buildRequestMap("{'ShipperCode':'" + shipperCode + "','LogisticCode':'" + logisticCode + "'}", "1002");
        //
        IHttpResponse _response = HttpClientHelper.create().post(__GATEWAY_URL, _params);
        if (_response != null && _response.getStatusCode() == 200) {
            JSONObject _result = JSON.parseObject(_response.getContent(), Feature.OrderedField);
            ExpressResult.Builder _builder = ExpressResult.Builder.create()
                    .result(_result.getString("Success"))
                    .errMsg(_result.getString("Reason"))
                    .shipperCode(_result.getString("ShipperCode"))
                    .logisticCode(_result.getString("LogisticCode"))
                    .status(_result.getString("Status"));
            JSONArray _traces = _result.getJSONArray("Traces");
            if (_traces != null) {
                for (Object _obj : _traces) {
                    JSONObject _item = (JSONObject) JSONObject.toJSON(_obj);
                    _builder.trackInfo(_item.getString("AcceptTime"), _item.getString("AcceptStation"), _item.getString("Remark"));
                }
            }
            return _builder.build();
        }
        //
        return null;
    }

    @Override
    public boolean subscribe(String shipperCode, String logisticCode) throws Exception {
        Map<String, String> _params = __buildRequestMap("{'ShipperCode':'" + shipperCode + "','LogisticCode':'" + logisticCode + "'}", "1008");
        //
        IHttpResponse _response = HttpClientHelper.create().post(__SUBSCRIBE_URL, _params);
        if (_response != null && _response.getStatusCode() == 200) {
            JSONObject _result = JSON.parseObject(_response.getContent());
            if (!BlurObject.bind(_result.getString("Success")).toBooleanValue()) {
                throw new RuntimeException(_result.getString("Reason"));
            }
            return true;
        }
        return false;
    }
}
