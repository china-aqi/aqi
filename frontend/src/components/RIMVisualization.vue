<template>
    <div id="box">
        <transition name="mybox">
            <div class="box" v-show="boxshow">
                <el-table :data="tblData" style="width: 100%">
                    <el-table-column v-for="(val, i) in columns"
                                     :key="i"
                                     :prop="val.prob"
                                     :label="val.label" :width="val.width" ></el-table-column>
                </el-table>
            </div>
        </transition>
    </div>
</template>
<script>
    export default {
        name: "RIMVisualization",
        data() {
            return {
                td: [{prob: 'item', label: '项目', width: 180},
                    {prob: 'y1', label: '2019年', width: 70},
                    {prob: 'y2', label: '2020年', width: 70}],
                tableData: [{
                    item: '每股盈利EPS',
                    y1: null,
                    y2: '-0.078',
                    y3: 0.636,
                    y4: 1.086,
                    y5: 1.3032,
                    y6: 1.564,
                    y7: 1.877,
                    y8: 2.252,
                    y9: 2.702
                }, {
                    item: '2016-05-04',
                    y1: '王小虎',
                    y2: '上海市普陀区金沙江路 1517 弄'
                }, {
                    item: '2016-05-01',
                    y1: '王小虎',
                    y2: '上海市普陀区金沙江路 1519 弄'
                }, {
                    item: '2016-05-03',
                    y1: '王小虎',
                    y2: '上海市普陀区金沙江路 1516 弄'
                }],
                boxshow: true,
                y5Flag: true,
                y6Flag: true,
                y7Flag: true,
                y8Flag: true,
                y9Flag: false
            }
        },
        props: {
            columns: {
                type: Array,
                required: true
            },
            tblData: {
                type: Array,
                required: true
            }
        },
        methods: {
            togglebox: function() {
                this.boxshow = !this.boxshow;
            },

            showFlag() {
                return !this.boxshow;
            }
        }
    };
</script>

<style>
    .box {
        margin-top: 0px;
        /*height: 1500px;*/
        background-color: #f2f6fc;
        overflow: hidden;
        text-align: left;
    }
    .mybox-leave-active,
    .mybox-enter-active {
        transition: all 0.5s ease;
    }
    .mybox-leave-active,
    .mybox-enter {
        height: 0px !important;
    }
    .mybox-leave,
    .mybox-enter-active {
        height: 500px;
    }
    .spreadButton {
        margin-top: 20px;
    }
</style>
