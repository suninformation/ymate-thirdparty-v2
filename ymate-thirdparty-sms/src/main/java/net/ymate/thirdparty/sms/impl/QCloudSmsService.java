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
package net.ymate.thirdparty.sms.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.ymate.framework.commons.HttpClientHelper;
import net.ymate.framework.commons.IHttpResponse;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.util.DateTimeUtils;
import net.ymate.platform.core.util.ExpressionUtils;
import net.ymate.platform.core.util.UUIDUtils;
import net.ymate.thirdparty.core.AbstractThirdpartyService;
import net.ymate.thirdparty.core.annotation.Service;
import net.ymate.thirdparty.sms.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/7/24 下午1:51
 * @version 1.0
 */
@Service(IQCloudSmsService.__NAME)
public class QCloudSmsService extends AbstractThirdpartyService implements IQCloudSmsService {

    private static final String __SEND_SMS_API = "https://yun.tim.qq.com/v5/tlssmssvr/sendsms?sdkappid=${sdkappid}&random=${random}";

    private static final String __SEND_MULTI_SMS_API = "https://yun.tim.qq.com/v5/tlssmssvr/sendmultisms2?sdkappid=${sdkappid}&random=${random}";

    @Override
    public ISmsSender create() {
        return new AbstractSmsSender() {

            private String __doSignature(String randomStr, int timeUTC) {
                StringBuilder _tos = new StringBuilder();
                for (To _item : __tos) {
                    _tos.append(_item.getMobile()).append(",");
                }
                // sha256(appkey=$appkey&random=$random&time=$time&mobile=$mobile)
                String _signStr = "appkey=" + getServiceKey() +
                        "&random=" + randomStr +
                        "&time=" + timeUTC +
                        "&mobile=" + StringUtils.substringBeforeLast(_tos.toString(), ",");
                return DigestUtils.sha256Hex(_signStr);
            }

            private SendResult.Builder __doParseResult(JSONObject result) {
                if (result.containsKey("result")) {
                    return SendResult.Builder.create()
                            .result(result.getIntValue("result"))
                            .errMsg(result.getString("errmsg"))
                            .mobile(result.getString("mobile"))
                            .country(result.getString("country"))
                            .ext(result.getString("ext"))
                            .sid(result.getString("sid"))
                            .fee(result.getIntValue("fee"));
                }
                return null;
            }

            private JSONObject __doParseTo(To to) {
                JSONObject _json = new JSONObject();
                _json.put("nationcode", to.getNationCode());
                _json.put("mobile", to.getMobile());
                return _json;
            }

            @Override
            protected SendResult __doSend(int type, String message) throws Exception {
                if (!this.__tos.isEmpty()) {
                    String _random = "" + (UUIDUtils.randomLong(9999, 999999) % (999999 - 100000 + 1) + 100000);
                    int _timeUTC = DateTimeUtils.systemTimeUTC();
                    //
                    JSONObject _data = new JSONObject();
                    _data.put("time", _timeUTC);
                    _data.put("type", type);
                    _data.put("sig", __doSignature(_random, _timeUTC));
                    _data.put("ext", "");
                    _data.put("extend", "");
                    //
                    if (StringUtils.isNotBlank(__tmplateId)) {
                        _data.put("tpl_id", BlurObject.bind(__tmplateId).toIntValue());
                        _data.put("params", __tmplateParams);
                    } else {
                        _data.put("msg", message);
                    }
                    //
                    if (this.__tos.size() == 1) {
                        _data.put("tel", __doParseTo(__tos.get(0)));
                        //
                        IHttpResponse _response = HttpClientHelper.create()
                                .post(ExpressionUtils.bind(__SEND_SMS_API)
                                        .set("sdkappid", getServiceId())
                                        .set("random", _random).getResult(), ContentType.APPLICATION_JSON, _data.toJSONString());
                        if (_response.getStatusCode() == 200) {
                            JSONObject _json = JSON.parseObject(_response.getContent());
                            SendResult.Builder _builder = __doParseResult(_json);
                            if (_builder == null) {
                                throw new RuntimeException(_response.getContent());
                            }
                            return _builder.mobile(__tos.get(0).getMobile()).country(__tos.get(0).getNationCode()).build();
                        }
                    } else {
                        JSONArray _tos = new JSONArray();
                        for (To _to : __tos) {
                            _tos.add(__doParseTo(_to));
                        }
                        _data.put("tel", _tos);
                        //
                        IHttpResponse _response = HttpClientHelper.create()
                                .post(ExpressionUtils.bind(__SEND_MULTI_SMS_API)
                                        .set("sdkappid", getServiceId())
                                        .set("random", _random).getResult(), ContentType.APPLICATION_JSON, _data.toJSONString());
                        if (_response.getStatusCode() == 200) {
                            JSONObject _json = JSON.parseObject(_response.getContent());
                            if (_json.containsKey("result")) {
                                MultiSendResult.Builder _result = MultiSendResult.Builder.create()
                                        .result(_json.getIntValue("result"))
                                        .errMsg(_json.getString("errmsg"))
                                        .ext(_json.getString("ext"));
                                //
                                JSONArray _jsonArr = _json.getJSONArray("detail");
                                int _count = 0;
                                if (_jsonArr != null) {
                                    for (Object _detail : _jsonArr) {
                                        SendResult.Builder _tmp = __doParseResult((JSONObject) JSONObject.toJSON(_detail));
                                        if (_tmp != null) {
                                            _count += _tmp.build().getFee();
                                            _result.detail(_tmp.build());
                                        }
                                    }
                                }
                                return _result.fee(_count).build();
                            } else {
                                throw new RuntimeException(_response.getContent());
                            }
                        }
                    }
                }
                return null;
            }
        };
    }
}
