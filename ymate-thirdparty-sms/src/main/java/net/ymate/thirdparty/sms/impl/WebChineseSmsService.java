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

import net.ymate.framework.commons.HttpClientHelper;
import net.ymate.framework.commons.IHttpResponse;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.thirdparty.core.AbstractThirdpartyService;
import net.ymate.thirdparty.core.annotation.Service;
import net.ymate.thirdparty.sms.AbstractSmsSender;
import net.ymate.thirdparty.sms.ISmsSender;
import net.ymate.thirdparty.sms.IWebChineseSmsService;
import net.ymate.thirdparty.sms.SendResult;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/7/24 下午1:49
 * @version 1.0
 */
@Service(IWebChineseSmsService.__NAME)
public class WebChineseSmsService extends AbstractThirdpartyService implements IWebChineseSmsService {

    private static final Map<Integer, String> __errorMsgs = new HashMap<Integer, String>();

    static {
        __errorMsgs.put(-1, "没有该用户账户");
        __errorMsgs.put(-2, "接口密钥不正确");
        __errorMsgs.put(-21, "MD5接口密钥加密不正确");
        __errorMsgs.put(-3, "短信数量不足");
        __errorMsgs.put(-11, "该用户被禁用");
        __errorMsgs.put(-14, "短信内容出现非法字符");
        __errorMsgs.put(-4, "手机号格式不正确");
        __errorMsgs.put(-41, "手机号码为空");
        __errorMsgs.put(-42, "短信内容为空");
        __errorMsgs.put(-51, "短信签名格式不正确");
        __errorMsgs.put(-6, "IP限制");
    }

    @Override
    public ISmsSender create() {
        return new AbstractSmsSender() {

            @Override
            public ISmsSender tmplateId(String tmplateId) {
                throw new UnsupportedOperationException();
            }

            @Override
            public ISmsSender param(String param) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SendResult send() throws Exception {
                throw new UnsupportedOperationException();
            }

            @Override
            protected SendResult __doSend(int type, String message) throws Exception {
                Map<String, String> _params = new HashMap<String, String>();
                _params.put("Uid", getServiceId());
                _params.put("Key", getServiceKey());
                //
                String _tos = __tos.remove(0).getMobile();
                for (To _item : __tos) {
                    _tos += "," + _item.getMobile();
                }
                _params.put("smsMob", _tos);
                _params.put("smsText", message);
                //
                IHttpResponse _response = HttpClientHelper.create().get("http://utf8.sms.webchinese.cn/", _params, new Header[]{new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")});
                if (_response != null && _response.getStatusCode() == 200) {
                    SendResult.Builder _builder = SendResult.Builder.create().mobile(_tos).country("86");
                    int _fee = BlurObject.bind(_response.getContent()).toIntValue();
                    if (_fee > 0) {
                        _builder.fee(_fee);
                    } else {
                        _builder.result(_fee).errMsg(StringUtils.defaultIfBlank(__errorMsgs.get(_fee), "未知错误"));
                    }
                    return _builder.build();
                }
                return null;
            }
        };
    }
}
