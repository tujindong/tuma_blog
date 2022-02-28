const user = {
    state: {

    },
    mutations: {
        LOGIN() {
        },
        LOGOUT(state, payload) {
            console.log('state', state, 'payload', payload)
        }
    },
    actions: {
        login(context, payload) {
            context.commit('LOGIN', payload)
        },
        logout(context) {
            context.commit('LOGOUT')
        }
    }
}
export default user;
