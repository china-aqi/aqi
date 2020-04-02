import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import RIMValue from "../views/RIMValue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home
  },
  {
    path: "/RIMValue",
    name: "RIMValue",
    component: RIMValue
  }
];

const router = new VueRouter({
  routes
});

export default router;