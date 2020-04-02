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
              <div class="grid-content bg-purple-light">
                <RIMValueResult :result="getRimValue()"></RIMValueResult>
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
            <div class="grid-content bg-purple" v-if="rData != null">
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
      companyInfo: null,
      code: this.$route.query.code,
      name: this.$route.query.name,
      RValue: 0.1,
      G1Value: 0.1,
      G2Value: 0.02,
      T1Value: 7,
      T2Value: 5
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
    getRimValue() {
      if (this.rData != null) {
        let rimParameter2018 = {
          bps2018: this.rData["last_bps"][1],
          predicationEPS: this.rData["analysis_eps"],
          t1: this.T1Value,
          g1: this.G1Value,
          industry_roe: this.rData["industry_roe"],
          r: this.RValue,
          g2: this.G2Value
        };
        let result = [{ year: 2018, bps: this.rData["last_bps"][1] }];
        return this.calcRI2018(rimParameter2018, 2019, result)
          .slice(1)
          .reduce((x, y) => x + y.discounted_ri, this.rData["last_bps"][1])
          .toFixed(2);
      }
    },
    calcRI2018(p, year, result) {
      let eps = null;
      let last_result = result.slice(-1)[0];

      // 计算eps
      if (2019 <= year && year <= 2021) {
        // 分析师预测期
        eps = p.predicationEPS[year - 2019][1];
      } else if (year <= 2018 + p.t1) {
        // 盈利线性成长期
        eps = last_result.eps * (1 + p.g1);
      } else if (year <= 2030) {
        // ROE均值回归期
        let last_roe = last_result.eps / result.slice(-2)[0].bps;
        let delta_roe = (last_roe - p.industry_roe) / (2030 + 1 - year);
        let roe = last_roe - delta_roe;
        eps = roe * last_result.bps;
      }

      if (year <= 2030) {
        // 盈利成长期 + ROE均值回归期
        // 计算bps, 和剩余收益ri
        let bps = last_result.bps + eps;
        let ri = eps - last_result.bps * p.r;
        result.push({
          year: year,
          eps: eps,
          bps: bps,
          ri: ri,
          discounted_ri: ri / Math.pow(1 + p.r, year - 2019)
        });
        return this.calcRI2018(p, year + 1, result);
      } else {
        // 持续期，计算完毕后退出递归运算
        let cv = (last_result.ri * (1 + p.g2)) / (p.r - p.g2);
        result.push({
          year: 2031,
          eps: 0,
          bps: 0,
          ri: cv,
          discounted_ri: cv / Math.pow(1 + p.r, year - 2019 - 1)
        });
        return result;
      }
    },
    getCompanyInfo() {
      if (this.companyInfo != null) {
        return {
          market_value: this.companyInfo["market_value"],
          industry: this.companyInfo["industry"],
          main_business: this.companyInfo["main_business"],
          registered_place: this.companyInfo["registered_place"],
          history: this.companyInfo["history"][0]
        };
      }
    }
  },
  mounted() {
    axios
      .get("http://106.15.137.244:80/v1.0/rim-proposal", {
        params: {
          code: this.$route.query.code
        }
      })
      .then(response => (this.rData = response.data));
    axios
      .get("http://106.15.137.244:80/v1.0/a_public_company_info", {
        params: {
          code: this.$route.query.code
        }
      })
      .then(response => (this.companyInfo = response.data));
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
