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

import net.ymate.platform.cache.Caches;
import net.ymate.platform.cache.ICache;
import net.ymate.platform.cache.ICacheLocker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/7/25 上午11:59
 * @version 1.0
 */
public abstract class AbstractSmsSender implements ISmsSender {

    protected List<To> __tos = new ArrayList<To>();

    protected String __tmplateId;

    protected List<String> __tmplateParams = new ArrayList<String>();

    public ISmsSender to(String mobile) {
        __tos.add(new To(mobile));
        return this;
    }

    public ISmsSender to(String nationCode, String mobile) {
        __tos.add(new To(nationCode, mobile));
        return this;
    }

    public ISmsSender to(To to) {
        __tos.add(to);
        return this;
    }

    public ISmsSender to(To[] to) {
        __tos.addAll(Arrays.asList(to));
        return this;
    }

    public ISmsSender to(Collection<To> to) {
        __tos.addAll(to);
        return this;
    }

    public ISmsSender tmplateId(String tmplateId) {
        __tmplateId = tmplateId;
        return this;
    }

    public ISmsSender param(String param) {
        __tmplateParams.add(param);
        return this;
    }

    public SendResult send() throws Exception {
        return send(0, null);
    }

    public SendResult send(String message) throws Exception {
        return send(0, message);
    }

    public SendResult send(int type, String message) throws Exception {
        SendConfig _sendConfig = SendConfig.getInstance();
        if (message != null && message.length() > _sendConfig.messageLength()) {
            throw new RuntimeException("Message content length not greater than " + _sendConfig.messageLength());
        }
        if (__tos.size() > _sendConfig.batchSize()) {
            throw new RuntimeException("The maximum number of batchSize not greater than " + _sendConfig.batchSize());
        }
        // 通过缓存过滤不符合发送次数限制的手机号码
        List<To> _newTos = new ArrayList<To>();
        List<SendCounter> _counters = new ArrayList<SendCounter>();
        ICache _cache = Caches.get().getCacheProvider().getCache(_sendConfig.cacheName());
        ICacheLocker _locker = _cache.acquireCacheLocker();
        if (_locker != null) {
            _locker.writeLock(ISmsSender.class.getName());
        }
        try {
            for (To _to : __tos) {
                SendCounter _counter = (SendCounter) _cache.get(_to.getMobile());
                if (_counter == null || _counter.canSend(_sendConfig)) {
                    _newTos.add(_to);
                    _counters.add(_counter == null ? new SendCounter(_to.getMobile()) : _counter);
                }
            }
            __tos = _newTos;
            SendResult _result = __doSend(type, message);
            if (_result != null && _result.getResult() == 0) {
                for (SendCounter _c : _counters) {
                    _cache.update(_c.getMobile(), _c.updateCount());
                }
            }
            return _result;
        } finally {
            if (_locker != null) {
                _locker.releaseWriteLock(ISmsSender.class.getName());
            }
        }
    }

    protected abstract SendResult __doSend(int type, String message) throws Exception;
}
