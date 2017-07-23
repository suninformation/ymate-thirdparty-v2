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
package net.ymate.thirdparty.core;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/12/20 下午5:11
 * @version 1.0
 */
public abstract class AbstractThirdpartyService implements IThirdpartyService {

    private IThirdparty __owner;

    private Map<String, String> __initParams;

    private String __serviceId;

    private String __serviceKey;

    private String __serviceIdName;

    private String __serviceKeyName;

    public AbstractThirdpartyService(String serviceIdName, String serviceKeyName) {
        if (StringUtils.isBlank(serviceIdName)) {
            throw new NullArgumentException("serviceIdName");
        }
        if (StringUtils.isBlank(serviceKeyName)) {
            throw new NullArgumentException("serviceKeyName");
        }
        __serviceIdName = serviceIdName;
        __serviceKeyName = serviceKeyName;
    }

    public AbstractThirdpartyService(String serviceKeyName) {
        this("@NULL", serviceKeyName);
    }

    public AbstractThirdpartyService() {
        __serviceIdName = "service_id";
        __serviceKeyName = "service_key";
    }

    @Override
    public void init(IThirdparty owner, Map<String, String> initParams) throws Exception {
        __owner = owner;
        __initParams = initParams;
        //
        if ("@NULL".equalsIgnoreCase(__serviceIdName)) {
            __serviceId = "";
        } else {
            __serviceId = StringUtils.defaultIfBlank(initParams.get(__serviceIdName), owner.getModuleCfg().getCommonsParam(__serviceIdName));
            //
            if (StringUtils.isBlank(__serviceId)) {
                throw new NullArgumentException(__serviceIdName);
            }
        }
        __serviceKey = StringUtils.defaultIfBlank(initParams.get(__serviceKeyName), owner.getModuleCfg().getCommonsParam(__serviceKeyName));
        //
        if (StringUtils.isBlank(__serviceKey)) {
            throw new NullArgumentException(__serviceKeyName);
        }
        //
        if (owner.getModuleCfg().isPasswordEncrypted()) {
            __serviceKey = owner.getModuleCfg().getPasswordProcessor().decrypt(__serviceKey);
        }
    }

    @Override
    public void destroy() throws Exception {
    }

    public IThirdparty getOwner() {
        return __owner;
    }

    public String getInitParam(String paramName) {
        return __initParams.get(paramName);
    }

    public String getServiceId() {
        return __serviceId;
    }

    public String getServiceKey() {
        return __serviceKey;
    }
}
