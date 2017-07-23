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
package net.ymate.thirdparty.core.impl;

import net.ymate.platform.core.YMP;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.support.IPasswordProcessor;
import net.ymate.platform.core.util.ClassUtils;
import net.ymate.thirdparty.core.IThirdparty;
import net.ymate.thirdparty.core.IThirdpartyModuleCfg;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/12/18 下午9:14
 * @version 1.0
 */
public class DefaultModuleCfg implements IThirdpartyModuleCfg {

    private Map<String, String> __moduleCfgs;

    private boolean __isPasswordEncrypted;

    private IPasswordProcessor __passwordProcessor;

    public DefaultModuleCfg(YMP owner) {
        __moduleCfgs = owner.getConfig().getModuleConfigs(IThirdparty.MODULE_NAME);
        //
        __isPasswordEncrypted = BlurObject.bind(__moduleCfgs.get("commons.password_encrypted")).toBooleanValue();
        if (__isPasswordEncrypted) {
            __passwordProcessor = ClassUtils.impl(__moduleCfgs.get("commons.password_class"), IPasswordProcessor.class, this.getClass());
            if (__passwordProcessor == null) {
                throw new NullArgumentException("commons.password_class");
            }
        }
    }

    private Map<String, String> __doGetParams(Map<String, String> moduleCfgs, String prefix) {
        Map<String, String> _classNames = new HashMap<String, String>();
        for (Map.Entry<String, String> _entry : moduleCfgs.entrySet()) {
            if (StringUtils.startsWith(_entry.getKey(), prefix)) {
                String _cfgKey = StringUtils.substring(_entry.getKey(), prefix.length());
                _classNames.put(_cfgKey, _entry.getValue());
            }
        }
        return _classNames;
    }

    public String getCommonsParam(String paramName) {
        if (StringUtils.isBlank(paramName)) {
            throw new NullArgumentException("paramName");
        }
        return __moduleCfgs.get("commons.".concat(paramName));
    }

    public boolean isPasswordEncrypted() {
        return __isPasswordEncrypted;
    }

    public IPasswordProcessor getPasswordProcessor() {
        return __passwordProcessor;
    }

    public Map<String, String> getServiceInitParams(String serviceName) {
        if (StringUtils.isBlank(serviceName)) {
            throw new NullArgumentException("serviceName");
        }
        return __doGetParams(__moduleCfgs, "service.".concat(serviceName).concat("."));
    }
}