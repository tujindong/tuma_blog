import { createStore } from 'vuex';
import state from './state';
import actions from './actions';
import mutations from './mutations';
import search from './modules/search';
import article from './modules/article';
import user from './modules/user';

export default createStore({
    state,
    mutations,
    actions,
    modules: {
        search,
        article,
        user
    }
})