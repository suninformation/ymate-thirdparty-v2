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
 * @author 刘镇 (suninformation@163.com) on 2017/7/24 上午9:50
 * @version 1.0
 */
public class LocationResult {

    private String result;

    private String errMsg;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 地区
     */
    private String area;

    /**
     * 邮编
     */
    private String zip;

    /**
     * 详细位置
     */
    private String location;

    /**
     * 运营商
     */
    private String company;

    /**
     * 卡类型
     */
    private String card;

    public String getResult() {
        return result;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getArea() {
        return area;
    }

    public String getZip() {
        return zip;
    }

    public String getLocation() {
        return location;
    }

    public String getCompany() {
        return company;
    }

    public String getCard() {
        return card;
    }

    public static class Builder {

        private LocationResult __target;

        public static Builder create() {
            return new Builder();
        }

        public Builder() {
            __target = new LocationResult();
        }

        public LocationResult build() {
            return __target;
        }

        public Builder result(String result) {
            __target.result = result;
            return this;
        }

        public Builder errMsg(String errMsg) {
            __target.errMsg = errMsg;
            return this;
        }

        public Builder country(String country) {
            __target.country = country;
            return this;
        }

        public Builder province(String province) {
            __target.province = province;
            return this;
        }

        public Builder city(String city) {
            __target.city = city;
            return this;
        }

        public Builder area(String area) {
            __target.area = area;
            return this;
        }

        public Builder zip(String zip) {
            __target.zip = zip;
            return this;
        }

        public Builder location(String location) {
            __target.location = location;
            return this;
        }

        public Builder company(String company) {
            __target.company = company;
            return this;
        }

        public Builder card(String card) {
            __target.card = card;
            return this;
        }
    }
}
