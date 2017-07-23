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

import net.ymate.platform.core.Version;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.beans.BeanMeta;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.annotation.Module;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.thirdparty.core.annotation.Service;
import net.ymate.thirdparty.core.handle.ServiceHandler;
import net.ymate.thirdparty.core.impl.DefaultModuleCfg;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/12/18 下午8:43
 * @version 1.0
 */
@Module
public class Thirdparty implements IModule, IThirdparty {

    private static final Log _LOG = LogFactory.getLog(Thirdparty.class);

    public static final Version VERSION = new Version(2, 0, 0, Thirdparty.class.getPackage().getImplementationVersion(), Version.VersionType.Alphal);

    private static volatile IThirdparty __instance;

    private YMP __owner;

    private IThirdpartyModuleCfg __moduleCfg;

    private boolean __inited;

    private List<IThirdpartyService> __services = new ArrayList<IThirdpartyService>();

    public static IThirdparty get() {
        if (__instance == null) {
            synchronized (VERSION) {
                if (__instance == null) {
                    __instance = YMP.get().getModule(Thirdparty.class);
                }
            }
        }
        return __instance;
    }

    public String getName() {
        return IThirdparty.MODULE_NAME;
    }

    public void init(YMP owner) throws Exception {
        if (!__inited) {
            //
            _LOG.info("Initializing ymate-thirdparty-core-" + VERSION);
            //
            __owner = owner;
            __moduleCfg = new DefaultModuleCfg(owner);
            __owner.registerHandler(Service.class, new ServiceHandler(this));
            //
            __inited = true;
        }
    }

    public boolean isInited() {
        return __inited;
    }

    public void registerService(Class<? extends IThirdpartyService> targetClass) throws Exception {
        Service _servAnno = targetClass.getAnnotation(Service.class);
        if (_servAnno != null) {
            // 获取服务初始配置参数
            Map<String, String> _initParams = __moduleCfg.getServiceInitParams(StringUtils.defaultIfBlank(_servAnno.value(), targetClass.getName()));
            if (BlurObject.bind(_initParams.get("enabled")).toBooleanValue()) {
                IThirdpartyService _serviceInst = targetClass.newInstance();
                try {
                    _LOG.info("Initializing service class " + targetClass.getName());
                    // 初始化接口服务
                    _serviceInst.init(this, _initParams);
                    // 服务注册
                    __owner.registerBean(BeanMeta.create(_serviceInst, targetClass));
                    __services.add(_serviceInst);
                } catch (Throwable e) {
                    _LOG.error("Service [" + targetClass.getName() + "] initialization failed: ", RuntimeUtils.unwrapThrow(e));
                }
            }
        }
    }

    public void destroy() throws Exception {
        if (__inited) {
            __inited = false;
            //
            for (IThirdpartyService _service : __services) {
                _service.destroy();
            }
            __services = null;
            //
            __moduleCfg = null;
            __owner = null;
        }
    }

    public YMP getOwner() {
        return __owner;
    }

    public IThirdpartyModuleCfg getModuleCfg() {
        return __moduleCfg;
    }
}
