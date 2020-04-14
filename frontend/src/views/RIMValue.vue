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
                <RIMValueResult :result="calcRimValue(lastIndicator, forecast, RValue, T1Value, G1Value, G2Value)['value']"></RIMValueResult>
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
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="估值模型说明" name="second"><spread :markdown="rimIntroduction" :flag="showflag"></spread></el-tab-pane>
        <el-tab-pane label="估值计算可视化" name="first" v-if="lastIndicator != null">
          <RIMVisualization :columns="getRimVisualizationTblCols(T1Value)"
                            :tblData="toVisData(calcRimValue(lastIndicator, forecast, RValue, T1Value, G1Value, G2Value)['procedure'])"></RIMVisualization>
        </el-tab-pane>
        <el-tab-pane label="公司质量分析" name="fourth">公司质量分析 To Do</el-tab-pane>
        <el-tab-pane label="估值参数建议" name="third">估值参数建议 To Do</el-tab-pane>
      </el-tabs>
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
import RIMVisualization from "../components/RIMVisualization";

const assert = require('assert');

export default {
  name: "Detail",
  components: {
    Spread,
    RIMVisualization,
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
      T1Value: 5,
      activeName: 'second',
      rimIntroduction: "# asdfasdf",
      showflag: false
    };
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
      this.showflag = this.showflag===false ? true : false;
    },
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
    getRimVisualizationTblCols(growingPeriod) {
      let cols = [{prob: 'item', label: '项目', width:180}, {prob: 'y1', label: '2018年', width:70},
        {prob: 'y2', label: '2019年', width:70}, {prob: 'y3', label: '2020年', width:70},
        {prob: 'y4', label: '2021年', width:70}];
      for (let i = 0; i < growingPeriod; i++) {
        cols.push({prob: 'y' + (i+5).toString(), label: (2022 + i).toString() + '年', width:70})
      }
      return cols;
    },
    toVisData(rimValue) {
      let toFixedOrNull = numb => numb != null ? numb.toFixed(3) : null;
      let results = [];
      let resultEPS = {item: '每股盈利EPS'};
      let resultBPS = {item: '每股净资产BPS'};
      let resultRI = {item: '每股剩余收益RI'};
      let resultDiscountedRI = {item: '每股RI折现值'};
      for (let i=0; i<rimValue.length - 1; i++) {
        resultEPS[`y${i+1}`] = toFixedOrNull(rimValue[i].eps);
        resultBPS[`y${i+1}`] = toFixedOrNull(rimValue[i].bps);
        resultRI[`y${i+1}`] = toFixedOrNull(rimValue[i].ri);
        resultDiscountedRI[`y${i+1}`] = toFixedOrNull(rimValue[i].disRi);
      }
      results.push(resultEPS);
      results.push(resultBPS);
      results.push(resultRI);
      results.push(resultDiscountedRI);

      let sum = 0;
      for (let key in resultDiscountedRI) {
          if (key != 'item' && resultDiscountedRI[key] != null) {
              sum += parseFloat(resultDiscountedRI[key]);
          }
      }
      results.push({item: '每股RI折现值之和', y1: sum.toFixed(3)});

      let cvRi = rimValue.slice(-1)[0].ri;
      let cvDiscountedRi = rimValue.slice(-1)[0].disRi;
      let cv = {item: '每股持续价值CV'};
      cv['y' + (rimValue.length-1)] = cvRi.toFixed(3);
      results.push(cv);
      results.push({item: '每股持续价值的折现', y1: cvDiscountedRi.toFixed(3)});
      results.push({item: '每股内在价值', y1: (cvDiscountedRi + sum + rimValue[0].bps).toFixed(3)});

      return results;
    },
    calcRimValue(indicator, forecast, capitalReturn, growthPeriod, growthRate, sustainedGrowRate) {
      if (indicator == null || forecast == null) {
        return null;
      }
      assert(typeof(indicator[0]['value']) == "string");
      assert(typeof(forecast[0]['value']) == "number");
      assert(typeof(capitalReturn) == "number");
      assert(typeof(sustainedGrowRate) == "number");

      let results = [{year: 2018, bps: parseFloat(indicator[0]['value']), eps: null, ri: null, disRi: null, roe: null}];

      // 分析师预测数据推算
      for (let year = 2019; year <= 2021; year++) {
          let lastResult = results.slice(-1)[0];
        let eps = forecast[year-2019]['value'];
        let ri = eps - lastResult.bps * capitalReturn;
        let result = {year: year,
            bps: eps + lastResult.bps,
            eps: eps,
            ri: ri,
            disRi: ri / Math.pow(1 + capitalReturn, year - 2019),
        };
        results.push(result);
      }

      // 盈利成长期推算
      for (let year = 2022; year <= 2021 + growthPeriod; year++) {
        let lastResult = results.slice(-1)[0];
        let eps = lastResult.eps * (1 + growthRate);
        let ri = eps - lastResult.bps * capitalReturn;
        let result = {
            year: year,
            bps: eps + lastResult.bps,
            eps: eps,
            ri: ri,
            disRi: ri / Math.pow(1+capitalReturn, year - 2019)
        };
        results.push(result)
      }

      // 经营持续期的推算
        let lastResult = results.slice(-1)[0];
      let ri = lastResult.ri * (1 + sustainedGrowRate) / (capitalReturn - sustainedGrowRate);
      let result = {
          year: null,
          bps: null,
          eps: null,
          ri: ri,
          disRi: ri / Math.pow(1+capitalReturn, lastResult.year - 2019)
      };
      results.push(result);

      results[0].disRi = results[0].bps;
      let value = results.reduce((x, y) => x + y.disRi, 0);
      results[0].disRi = null;

      return {value: value, procedure: results};
    }
  },
  mounted() {
    // 获取当期财务指标
    axios.get("http://106.15.137.244:8080/indicator/search/getByCode", {params: { code: this.$route.query.code }})
            .then(res => (this.lastIndicator = [{des: "2018BPS", value: res.data["_embedded"]["indicator"][0]["bps"].toFixed(2)},
              {des: "2018EPS", value: res.data["_embedded"]["indicator"][0]["eps"]}]))
    // 获取分析师盈利预测
    axios.get("http://106.15.137.244:8080/forecast/search/getByCode", {params: { code: this.$route.query.code }})
            .then(res => (this.forecast = [{des: "2019EPS", value: res.data["_embedded"]["forecast"][0]["eps2019"]},
              {des: "2020EPS", value: res.data["_embedded"]["forecast"][0]["eps2020"]},
              {des: "2021EPS", value: res.data["_embedded"]["forecast"][0]["eps2021"]}]))
    // 获取公司介绍
    axios.get("http://106.15.137.244:8080/company/search/getByCode", {params: { code: this.$route.query.code }})
            .then(res => (this.company = res.data["_embedded"]["company"][0]))
    // 获取剩余收益模型的介绍
    const url = `http://106.15.137.244:8080/%E5%89%A9%E4%BD%99%E6%94%B6%E7%9B%8A%E4%BC%B0%E5%80%BC.md`;
    axios.get(url).then((response) => {
      this.rimIntroduction = response.data;
    });
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
