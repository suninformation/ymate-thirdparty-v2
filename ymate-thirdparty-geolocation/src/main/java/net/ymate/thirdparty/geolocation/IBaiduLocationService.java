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
package net.ymate.thirdparty.geolocation;

/**
 * 基于百度根据提供的GPS坐标和IP地址等查询位置信息
 *
 * @author 刘镇 (suninformation@163.com) on 16/12/19 上午3:49
 * @version 1.0
 */
public interface IBaiduLocationService {

    String __NAME = "geolocation.baidu";

    /**
     * @param lng 经度坐标
     * @param lat 纬度坐标
     * @return 返回该坐标的逆地址编码信息
     * @throws Exception 可能产生的任何异常
     */
    LocationResult getPositionInfo(String lng, String lat) throws Exception;

    /**
     * @param ipAddr IP地址或域名
     * @return 返回该IP或域名所属的区域
     * @throws Exception 可能产生的任何异常
     */
    LocationResult getIpInfo(String ipAddr) throws Exception;
}
