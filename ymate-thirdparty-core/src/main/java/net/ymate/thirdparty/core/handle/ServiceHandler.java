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
package net.ymate.thirdparty.core.handle;

import net.ymate.platform.core.beans.IBeanHandler;
import net.ymate.platform.core.util.ClassUtils;
import net.ymate.thirdparty.core.IThirdparty;
import net.ymate.thirdparty.core.IThirdpartyService;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/7/22 上午10:57
 * @version 1.0
 */
public class ServiceHandler implements IBeanHandler {

    private IThirdparty __owner;

    public ServiceHandler(IThirdparty owner) throws Exception {
        __owner = owner;
        __owner.getOwner().registerExcludedClass(IThirdpartyService.class);
    }

    @SuppressWarnings("unchecked")
    public Object handle(Class<?> targetClass) throws Exception {
        if (ClassUtils.isInterfaceOf(targetClass, IThirdpartyService.class)) {
            __owner.registerService((Class<? extends IThirdpartyService>) targetClass);
        }
        return null;
    }
}
