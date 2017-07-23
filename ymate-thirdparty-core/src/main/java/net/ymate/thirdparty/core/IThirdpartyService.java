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

import java.util.Map;

/**
 * 第三方服务集成扩展接口，所有扩展的服务必须实现此接口方能启动并初始化正常
 *
 * @author 刘镇 (suninformation@163.com) on 16/12/18 下午9:51
 * @version 1.0
 */
public interface IThirdpartyService {

    /**
     * 初始化第三方服务
     *
     * @param owner      所属模块管理器
     * @param initParams 初始化参数映射
     * @throws Exception 可以产生的任何异常
     */
    void init(IThirdparty owner, Map<String, String> initParams) throws Exception;

    /**
     * 销毁第三服务
     *
     * @throws Exception 可以产生的任何异常
     */
    void destroy() throws Exception;
}
