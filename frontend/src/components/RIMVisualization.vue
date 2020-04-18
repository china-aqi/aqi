<template>
    <div id="box">
        <transition name="mybox">
            <div class="box">
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
</style>
