import Vue from 'vue'
import Router from 'vue-router'
import { constantRouterMap } from '@/config/router.config'

Vue.use(Router)

export default new Router({
    mode: 'history',
    // mode: 'hash',
    base: '/admin/',
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRouterMap
})