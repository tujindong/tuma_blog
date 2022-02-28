import { listToTree } from "@/utils/util";
const article = {
    state: {
        articleTitles: []
    },
    mutations: {
        SET_ARTICLE_TITLES(state, key) {
            const articleChildNodes = key.childNodes;
            const nodes = ["H1", "H2", "H3"];
            const flatList = [];
            articleChildNodes.forEach((el, index) => {
                if (nodes.includes(el.nodeName)) {
                    const id = `header-${index}`;
                    const level = Number(el.nodeName.substring(1, 2));
                    el.setAttribute("id", id);
                    flatList.push({ id, title: el.innerHTML, level, nodeName: el.nodeName })
                }
            })
            //查找相邻最小值赋予pid
            for (let i = 0; i < flatList.length; i++) {
                if (flatList[i].level == Math.min(...flatList.map(item => item.level))) flatList[i].pid = '-1';
                for (let j = i; j >= 0; j--) {
                    if (flatList[j].level < flatList[i].level) {
                        flatList[i].pid = flatList[j].id
                        break;
                    }
                }
            }
            state.articleTitles = listToTree(flatList);
        }
    },
    actions: {
        setArticleTitles(context, key) {
            context.commit('SET_ARTICLE_TITLES', key)
        }
    }
}
export default article;
