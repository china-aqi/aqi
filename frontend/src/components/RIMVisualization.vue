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
        // data() {
        //     return {
        //         boxshow: true,
        //         y5Flag: true,
        //         y6Flag: true,
        //         y7Flag: true,
        //         y8Flag: true,
        //         y9Flag: false
        //     }
        // },
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
    /*.mybox-leave-active,*/
    /*.mybox-enter-active {*/
    /*    transition: all 0.5s ease;*/
    /*}*/
    /*.mybox-leave-active,*/
    /*.mybox-enter {*/
    /*    height: 0px !important;*/
    /*}*/
    /*.mybox-leave,*/
    /*.mybox-enter-active {*/
    /*    height: 500px;*/
    /*}*/
    /*.spreadButton {*/
    /*    margin-top: 20px;*/
    /*}*/
</style>
