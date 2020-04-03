<template>
  <div>
    <el-card class="box-card" id="RIMValue">
      <div slot="header" class="clearfix">
        <span>{{ name }}: {{ code }}</span>
      </div>
      <div>
        <el-row :gutter="20">
          <el-col :span="11">
            <div class="grid-content bg-purple">
              <div class="grid-content bg-purple-light" v-if="lastIndicator != null">
                <RIMValueResult :result="calcRimValue(lastIndicator, forecast, RValue, T1Value, G1Value, G2Value)"></RIMValueResult>
              </div>
              <el-row :gutter="10">
                <el-col :span="12">
                  <div class="grid-content bg-purple-dark">
                    <RIMValueTable
                            tbl-title="财报数据"
                      :tbl-data="lastIndicator"
                    ></RIMValueTable>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="grid-content bg-purple-dark">
                    <RIMValueTable tbl-title="分析师预测" :tbl-data="forecast"></RIMValueTable>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="grid-content bg-purple">
              <RIMValueIntroduction
                :company-info="company"
              ></RIMValueIntroduction>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="grid-content bg-purple">
              <RIMValueSlider
                :config-info="{ name: '成长期', min: 0, max: 5, step: 1, defaultValue: 5}"
                :caption-fn="v => '盈利成长期持续: ' + v + '年'"
                @func="getValueT1T2FormSon"
              ></RIMValueSlider>
              <RIMValueSlider
                      :config-info="{ name: 'g1', min: 0.05, max: 1, step: 0.01, defaultValue: 0.20 }"
                      :caption-fn="v => '盈利成长期的增长率: ' + (v * 100).toFixed(1) + '%'"
                      @func="getValueG1FormSon"
              ></RIMValueSlider>
              <RIMValueSlider
                :config-info="{name: '资本必要回报率', min: 0.07, max: 0.13, step: 0.005, defaultValue: 0.10}"
                :caption-fn="v => '资本必要回报率：' + (v * 100).toFixed(1) + '%'"
                @func="getValueRFormSon"
              ></RIMValueSlider>
              <RIMValueSlider
                :config-info="{ name: '持续期剩余收益增长率', min: 0, max: 0.045, step: 0.005, defaultValue: 0.04}"
                :caption-fn="
                  v => '持续经营期剩余收益增长率：' + (v * 100).toFixed(1) + '%'
                "
                @func="getValueG2FormSon"
              ></RIMValueSlider>
            </div>
          </el-col>
        </el-row>
      </div>
      <Spread></Spread>
    </el-card>
  </div>
</template>

<script>
import axios from "axios";
import RIMValueResult from "../components/RIMValueResult";
import RIMValueTable from "../components/RIMValueTable";
import RIMValueSlider from "../components/RIMValueSlider";
import RIMValueIntroduction from "../components/RIMValueIntroduction";
import Spread from "../components/Spread";

const assert = require('assert');

export default {
  name: "Detail",
  components: {
    Spread,
    RIMValueIntroduction,
    RIMValueTable,
    RIMValueResult,
    RIMValueSlider
  },
  data() {
    return {
      rData: null,
      lastIndicator: null,
      forecast: null,
      company: null,
      code: this.$route.query.code,
      name: this.$route.query.name,
      RValue: 0.1,
      G1Value: 0.2,
      G2Value: 0.02,
      T1Value: 5
    };
  },
  methods: {
    getValueT1T2FormSon(data) {
      this.T1Value = data;
      this.T2Value = this.rData["t1_t2"] - this.T1Value;
    },
    getValueRFormSon(data) {
      this.RValue = data;
    },
    getValueG1FormSon(data) {
      this.G1Value = data;
    },
    getValueG2FormSon(data) {
      this.G2Value = data;
    },
    calcRimValue(indicator, forecast, capitalReturn, growthPeriod, growthRate, sustainedGrowRate) {
      assert(typeof(indicator[0]['value']) == "string")
      assert(typeof(forecast[0]['value']) == "number")
      assert(typeof(capitalReturn) == "number")
      assert(typeof(sustainedGrowRate) == "number")

      let lastBPS = parseFloat(indicator[0]['value']);
      let value = lastBPS;

      // 分析师预测数据推算
      let eps = 0;
      let year = 0;
      for (let i = 0; i <= 2; i++) {
        eps = forecast[i]['value'];
        let ri = eps - lastBPS * capitalReturn;
        year++;
        let discounted_ri = ri / Math.pow(1 + capitalReturn, year);
        value += discounted_ri;
        lastBPS = eps + lastBPS;
        value += eps;
      }

      // 盈利成长期推算
      for (let j = 0; j < growthPeriod; j++) {
        eps = eps * (1 + growthRate);
        let ri = eps - lastBPS * capitalReturn;
        year++;
        let discounted_ri = ri / Math.pow(1 + capitalReturn, year)
        value += discounted_ri;
        lastBPS = eps + lastBPS;
      }

      // 经营持续期的推算
      let ri = eps * (1 + sustainedGrowRate) / (capitalReturn - sustainedGrowRate);
      let discounted_ri = ri / Math.pow(1 + capitalReturn, year)
      value += discounted_ri;

      return value;
    }
  },
  mounted() {
    // 获取当期财务指标
    axios.get("http://192.168.0.7:1303/indicator/search/getByCode", {params: { code: this.$route.query.code }})
            .then(res => (this.lastIndicator = [{des: "2018BPS", value: res.data["_embedded"]["indicator"][0]["bps"].toFixed(2)},
              {des: "2018EPS", value: res.data["_embedded"]["indicator"][0]["eps"]}]))
    // 获取分析师盈利预测
    axios.get("http://192.168.0.7:1303/forecast/search/getByCode", {params: { code: this.$route.query.code }})
            .then(res => (this.forecast = [{des: "2019EPS", value: res.data["_embedded"]["forecast"][0]["eps2019"]},
              {des: "2020EPS", value: res.data["_embedded"]["forecast"][0]["eps2020"]},
              {des: "2021EPS", value: res.data["_embedded"]["forecast"][0]["eps2021"]}]))
    // 获取公司介绍
    axios.get("http://192.168.0.7:1303/company/search/getByCode", {params: { code: this.$route.query.code }})
            .then(res => (this.company = res.data["_embedded"]["company"][0]))
  }
};
</script>

<style>
#RIMValue {
  width: 1200px;
  margin: auto;
}

.el-row {
  margin-bottom: 5px;
}
.el-col {
  border-radius: 4px;
}

.bg-purple-dark {
}

.bg-purple {
}

.bg-purple-light {
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
</style>
