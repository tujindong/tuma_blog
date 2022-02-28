const search = {
    state: {
        searchKey: '', //搜索字段
        isSearchBoxShow: false, //mobile 设置搜索框显示
        selectionTag: {}, //已选择 tag
    },
    mutations: {
        SET_SEARCH_KEY(state, key) {
            state.searchKey = key
        },
        SET_IS_SEARCH_BOX_SHOW(state, key) {
            state.isSearchBoxShow = key
        },
        SET_SELECTION_TAG(state, key) {
            state.selectionTag = key
        }
    },
    actions: {
        setSearchKey(context, key) {
            context.commit('SET_SEARCH_KEY', key)
        },
        setIsSearchBoxShow(context, key) {
            context.commit('SET_IS_SEARCH_BOX_SHOW', key)
        },
        setSelectionTag(context, key) {
            context.commit('SET_SELECTION_TAG', key)
        }
    }
}
export default search;
