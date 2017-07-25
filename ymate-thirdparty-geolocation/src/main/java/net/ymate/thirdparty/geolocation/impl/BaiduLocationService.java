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
import net.ymate.thirdparty.core.annotation.Service;
import net.ymate.thirdparty.geolocation.AbstractBaiduService;
import net.ymate.thirdparty.geolocation.IBaiduLocationService;
import net.ymate.thirdparty.geolocation.LocationResult;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/7/24 上午11:03
 * @version 1.0
 */
@Service(IBaiduLocationService.__NAME)
public class BaiduLocationService extends AbstractBaiduService implements IBaiduLocationService {

    @Override
    public LocationResult getPositionInfo(String lng, String lat) throws Exception {
        if (StringUtils.isBlank(lng)) {
            throw new NullArgumentException("lng");
        }
        if (StringUtils.isBlank(lat)) {
            throw new NullArgumentException("lat");
        }
        Map<String, String> __params = new LinkedHashMap<String, String>();
        __params.put("location", lat + "," + lng);
        __params.put("output", "json");
        __params.put("ak", getServiceId());
        __doBuildSN(__params, "/geocoder/v2/");
        //
        IHttpResponse _response = HttpClientHelper.create().get("http://api.map.baidu.com/geocoder/v2/", __params);
        if (_response != null && _response.getStatusCode() == 200) {
            JSONObject _json = JSON.parseObject(_response.getContent());
            LocationResult.Builder _builder = LocationResult.Builder.create()
                    .result(_json.getString("status"))
                    .errMsg(_json.getString("message"));
            //
            JSONObject _content = _json.getJSONObject("result");
            if (_content != null) {
                _builder.location(_content.getString("formatted_address"));
                JSONObject _tmp = _content.getJSONObject("addressComponent");
                if (_tmp != null) {
                    _builder.country(_tmp.getString("country"))
                            .province(_tmp.getString("province"))
                            .city(_tmp.getString("city"))
                            .area(_tmp.getString("district"))
                            .zip(_tmp.getString("adcode"));
                }
            }
            return _builder.build();
        }
        return null;
    }

    @Override
    public LocationResult getIpInfo(String ipAddr) throws Exception {
        if (StringUtils.isBlank(ipAddr)) {
            throw new NullArgumentException("ipAddr");
        }
        Map<String, String> __params = new LinkedHashMap<String, String>();
        __params.put("ip", ipAddr);
        __params.put("ak", getServiceId());
        __doBuildSN(__params, "/location/ip");
        //
        IHttpResponse _response = HttpClientHelper.create().get("https://api.map.baidu.com/location/ip", __params);
        if (_response != null && _response.getStatusCode() == 200) {
            JSONObject _json = JSON.parseObject(_response.getContent());
            LocationResult.Builder _builder = LocationResult.Builder.create()
                    .result(_json.getString("status"))
                    .errMsg(_json.getString("message"))
                    .location(_json.getString("address"));
            JSONObject _content = _json.getJSONObject("content");
            if (_content != null) {
                _builder.area(_content.getString("address"));
            }
            return _builder.build();
        }
        return null;
    }
}
